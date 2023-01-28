package org.vined.ikea.modules.dupes;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.vehicle.ChestBoatEntity;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;

public class IKEADupe extends Module {
    public net.minecraft.client.network.ClientPlayNetworkHandler handler;
    private final TimerUtils timer;

    public IKEADupe() {
        super(IKEA.DUPES, "ikea-dupe",
                "Does the boat dupe. (Make sure an alt or your friend is in render distance for it to work)");

        this.timer = new TimerUtils();

        this.sgGeneral = this.settings.getDefaultGroup();
        this.rotate = this.sgGeneral.add(new BoolSetting.Builder()
                .name("rotate")
                .description("Faces the boat.")
                .defaultValue(Boolean.valueOf(true))
                .build());
    }

    private final SettingGroup sgGeneral;
    private final Setting<Boolean> rotate;

    public void onActivate() {
        assert this.mc.getNetworkHandler() != null;
        this.handler = this.mc.getNetworkHandler();
    }

    public void onDeactivate() {
        this.timer.reset();
    }

    @EventHandler(priority = 200)
    private void onTickBoat(TickEvent.Post event) {
        assert this.mc.world != null;
        assert this.mc.player != null;

        for (Entity entity : this.mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity) {
                ChestBoatEntity nearestBoat = (ChestBoatEntity) entity;
                if (PlayerUtils.distanceTo(nearestBoat.getPos()) > 5.5D)
                    continue;
                if (!nearestBoat.hasPassenger((Entity) this.mc.player)) {
                    if (this.timer.hasReached(100L)) {
                        sit(nearestBoat);
                        this.timer.reset();
                    }
                    continue;
                }
                boatInventory();

                if (this.timer.hasReached(100L)) {
                    sendDismountPackets(nearestBoat);
                    if (this.mc.currentScreen instanceof HandledScreen) {
                        HandledScreen<?> handledScreen = (HandledScreen<?>) this.mc.currentScreen;
                        if (handledScreen instanceof GenericContainerScreen
                                && nearestBoat.hasPassenger((Entity) this.mc.player)) {
                            sendDismountPackets(nearestBoat);
                        }
                    }

                    this.timer.reset();
                }
            }

        }
    }

    @EventHandler(priority = 100)
    private void onTickThrow(TickEvent.Post event) {
        assert this.mc.world != null;
        assert this.mc.interactionManager != null;
        assert this.mc.player != null;

        for (Entity entity : this.mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity) {
                ChestBoatEntity nearestBoat = (ChestBoatEntity) entity;
                if (PlayerUtils.distanceTo(nearestBoat.getPos()) <= 5.5D
                        && !nearestBoat.hasPassenger((Entity) this.mc.player)) {
                    if (this.mc.currentScreen instanceof HandledScreen) {
                        HandledScreen<?> handledScreen = (HandledScreen<?>) this.mc.currentScreen;
                        if (handledScreen instanceof GenericContainerScreen) {
                            GenericContainerScreen container = (GenericContainerScreen) handledScreen;
                            Inventory inv = ((GenericContainerScreenHandler) container
                                    .getScreenHandler()).getInventory();
                            if (!inv.isEmpty()) {
                                for (int i = 0; i < inv.size(); i++) {
                                    InvUtils.drop().slotId(i);
                                }
                            }
                        }
                    }

                }
            }

        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        toggle();
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen instanceof DisconnectedScreen) {
            toggle();
        }
    }

    public void sit(ChestBoatEntity boat) {
        interact((Entity) boat);
    }

    private void interact(Entity entity) {
        assert this.mc.interactionManager != null;
        if (((Boolean) this.rotate.get()).booleanValue()) {
            Rotations.rotate(Rotations.getYaw(entity), Rotations.getPitch(entity), -100,
                    () -> this.mc.interactionManager.interactEntity(
                            (PlayerEntity) this.mc.player, entity,
                            Hand.MAIN_HAND));
        } else {
            this.mc.interactionManager.interactEntity((PlayerEntity) this.mc.player, entity,
                    Hand.MAIN_HAND);
        }

    }

    private void boatInventory() {
        assert this.mc.player != null;
        if (this.mc.currentScreen instanceof HandledScreen) {
            HandledScreen<?> handledScreen = (HandledScreen<?>) this.mc.currentScreen;
            if (handledScreen instanceof GenericContainerScreen)
                return;
        }
        this.mc.player.openRidingInventory();
    }

    private void sendDismountPackets(ChestBoatEntity boat) {
        assert this.mc.player != null;
        assert this.handler != null;
        this.handler.sendPacket((Packet<?>) new PlayerInputC2SPacket(1.0F, 1.0F, false, true));
        this.handler.sendPacket((Packet<?>) new VehicleMoveC2SPacket(boat));
        this.handler.sendPacket((Packet<?>) new BoatPaddleStateC2SPacket(false, false));
        this.handler.sendPacket((Packet<?>) new TeleportConfirmC2SPacket(1));
        this.handler.sendPacket(
                (Packet<?>) new ClientCommandC2SPacket(this.mc.player, ClientCommandC2SPacket.Mode.OPEN_INVENTORY));
    }
}
package org.vined.ikea.modules.dupes;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.vehicle.ChestBoatEntity;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;

public class IKEADupe extends meteordevelopment.meteorclient.systems.modules.Module {
    public net.minecraft.client.network.ClientPlayNetworkHandler handler;
    private final TimerUtils timer;

    public IKEADupe() {
        super(IKEA.DUPES, "ikea-dupe",
                "Does the boat dupe. (Make sure an alt or your friend is in render distance for it to work)");

        /* 34 */ this.timer = new TimerUtils();

        /* 36 */ this.sgGeneral = this.settings.getDefaultGroup();
        /* 37 */ this.rotate = this.sgGeneral.add((new BoolSetting.Builder()
                /* 38 */ .name("rotate"))
                /* 39 */ .description("Faces the boat.")
                /* 40 */ .defaultValue(Boolean.valueOf(true))
                /* 41 */ .build());
    }

    private final SettingGroup sgGeneral;
    private final Setting<Boolean> rotate;

    public void onActivate() {
        /* 46 */ assert this.mc.getNetworkHandler() != null;
        /* 47 */ this.handler = this.mc.getNetworkHandler();
    }

    public void onDeactivate() {
        /* 52 */ this.timer.reset();
    }

    @EventHandler(priority = 200)
    private void onTickBoat(TickEvent.Post event) {
        /* 57 */ assert this.mc.world != null;
        /* 58 */ assert this.mc.player != null;

        /* 60 */ for (net.minecraft.entity.Entity entity : this.mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity) {
                ChestBoatEntity nearestBoat = (ChestBoatEntity) entity;
                if (PlayerUtils.distanceTo(nearestBoat.getPos()) > 5.5D)
                    continue;
                if (!nearestBoat.hasPassenger((net.minecraft.entity.Entity) this.mc.player)) {
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
                        /* 74 */ if (handledScreen instanceof GenericContainerScreen
                                && nearestBoat.hasPassenger((Entity) this.mc.player)) {
                            /* 76 */ sendDismountPackets(nearestBoat);
                        }
                    }

                    /* 80 */ this.timer.reset();
                }
            }

        }
    }

    @EventHandler(priority = 100)
    private void onTickThrow(TickEvent.Post event) {
        /* 89 */ assert this.mc.world != null;
        /* 90 */ assert this.mc.interactionManager != null;
        /* 91 */ assert this.mc.player != null;

        for (net.minecraft.entity.Entity entity : this.mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity) {
                ChestBoatEntity nearestBoat = (ChestBoatEntity) entity;
                if (PlayerUtils.distanceTo(nearestBoat.getPos()) <= 5.5D
                        && !nearestBoat.hasPassenger((net.minecraft.entity.Entity) this.mc.player)) {
                    if (this.mc.currentScreen instanceof HandledScreen) {
                        HandledScreen<?> handledScreen = (HandledScreen<?>) this.mc.currentScreen;
                        /* 98 */ if (handledScreen instanceof GenericContainerScreen) {
                            GenericContainerScreen container = (GenericContainerScreen) handledScreen;
                            /* 99 */ net.minecraft.inventory.Inventory inv = ((GenericContainerScreenHandler) container
                                    .getScreenHandler()).getInventory();
                            /* 100 */ if (!inv.isEmpty()) {
                                /* 101 */ for (int i = 0; i < inv.size(); i++) {
                                    /* 102 */ InvUtils.drop().slotId(i);
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
        /* 114 */ toggle();
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        /* 119 */ if (event.screen instanceof DisconnectedScreen) {
            /* 120 */ toggle();
        }
    }

    public void sit(ChestBoatEntity boat) {
        /* 125 */ interact((Entity) boat);
    }

    private void interact(Entity entity) {
        /* 128 */ assert this.mc.interactionManager != null;
        /* 129 */ if (((Boolean) this.rotate.get()).booleanValue()) {
            Rotations.rotate(Rotations.getYaw(entity), Rotations.getPitch(entity), -100,
                    () -> this.mc.interactionManager.interactEntity(
                            (PlayerEntity) this.mc.player, entity,
                            Hand.MAIN_HAND));
        }
        /* 130 */ else {
            this.mc.interactionManager.interactEntity((PlayerEntity) this.mc.player, entity,
                    Hand.MAIN_HAND);
        }

    }

    private void boatInventory() {
        /* 134 */ assert this.mc.player != null;
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
        this.handler.sendPacket((Packet<?>) new VehicleMoveC2SPacket((Entity) boat));
        this.handler.sendPacket((Packet<?>) new BoatPaddleStateC2SPacket(
                false, false));
        /* 147 */ this.handler.sendPacket(
                (Packet<?>) new TeleportConfirmC2SPacket(
                        1));
        /* 148 */ this.handler.sendPacket(
                (Packet<?>) new ClientCommandC2SPacket(
                        (Entity) this.mc.player,
                        ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
    }
}
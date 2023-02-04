package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;

import org.vined.ikea.IKEA;

public final class NoChestRender extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgParty = this.settings.createGroup("Party Mode");

    private final Setting<Integer> radius = this.sgGeneral
            .add(new IntSetting.Builder()
                    .name("render-radius")
                    .description("The radius in which the storage blocks will render.")
                    .defaultValue(0)
                    .min(0)
                    .sliderMax(128)
                    .build());

    private final Setting<Integer> partyModeMin = this.sgParty
            .add(new IntSetting.Builder()
                    .name("party-min-radius")
                    .description("The minimum radius party mode will change to.")
                    .defaultValue(0)
                    .min(0)
                    .max(128)
                    .sliderMax(128)
                    .build());

    private final Setting<Integer> partyModeMax = this.sgParty
            .add(new IntSetting.Builder()
                    .name("party-max-radius")
                    .description("The maximum radius party mode will change to.")
                    .defaultValue(64)
                    .min(1)
                    .max(128)
                    .sliderMax(128)
                    .build());

    private final Setting<Boolean> partyMode = this.sgParty.add(new BoolSetting.Builder()
            .name("party-mode")
            .description("Constantly changes the radius")
            .defaultValue(false)
            .build());

    private boolean retreat = false;

    public NoChestRender() {
        super(IKEA.UTILITY, "no-chest-render", "Doesn't render chests.");
    }

    @EventHandler
    private final void onTick(TickEvent.Pre event) {
        if (this.partyMode.get()) {
            if (this.partyModeMin.get() > this.partyModeMax.get()) {
                info("The party mode minimum can't be bigger than the party mode maximum, setting the minimum to 0...");
                this.partyModeMin.set(0);
                return;
            }
            if (this.retreat) {
                if (this.radius.get() == this.partyModeMin.get()) {
                    this.retreat = false;
                    return;
                }
                this.radius.set(this.radius.get() - 1);
            } else if (this.radius.get() >= this.partyModeMax.get()) {
                this.retreat = true;
            } else {
                this.radius.set(this.radius.get() + 1);
            }
        }
    }

    @EventHandler
    private final void onRenderBlockEntity(RenderBlockEntityEvent event) {
        BlockEntity block = event.blockEntity;
        if (block instanceof ChestBlockEntity && PlayerUtils.distanceTo(block.getPos()) > this.radius.get())
            event.cancel();
    }
}
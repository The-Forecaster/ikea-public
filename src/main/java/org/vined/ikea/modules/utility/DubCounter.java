
package org.vined.ikea.modules.utility;

import java.util.ArrayList;
import java.util.List;
import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;

public class DubCounter extends Module {
        private final List<BlockPos> coords = new ArrayList<>();
        private final TimerUtils timer = new TimerUtils();

        private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
        private final Setting<Integer> loadTime = this.sgGeneral.add(new IntSetting.Builder()
                        .name("load-time")
                        .description("How much time it's going to take to load all the dubs. (useful if there are dubs out of render distance so you can load them or if you have a performance mod)")
                        .defaultValue(1)
                        .min(1)
                        .sliderMax(60)
                        .build());

        public DubCounter() {
                super(IKEA.UTILITY, "dub-counter", "Counts how many dubs are in render distance.");
        }

        public void onActivate() {
                this.timer.reset();
                info("Please wait " + Formatting.RED + this.loadTime.get() + Formatting.GREEN + " second(s)...");
        }

        public void onDeactivate() {
                this.coords.clear();
        }

        @EventHandler
        private void onTick(TickEvent.Post event) {
                if (this.timer.hasReached(this.loadTime.get() * 1000L)) {
                        int length = this.coords.size();
                        if (length % 2 == 0) {
                                int dubs = length / 2;
                                info("There are roughly " + Formatting.BOLD + dubs
                                                + Formatting.GOLD + " (" + length
                                                + " normal chests) rendered double chests.");
                                toggle();
                        } else {
                                int fixed = length - 1;
                                int dubs = fixed / 2;
                                info("There are roughly " + Formatting.GOLD + dubs
                                                + Formatting.BOLD + " (" + fixed
                                                + " normal chests) rendered double chests.");
                                toggle();
                        }
                        this.timer.reset();
                }
        }

        @EventHandler
        private void onRenderBlockEntity(RenderBlockEntityEvent event) {
                BlockEntity block = event.blockEntity;
                if (block instanceof ChestBlockEntity) {
                        ChestBlockEntity chest = (ChestBlockEntity) block;
                        if (!this.coords.contains(chest.getPos()))
                                this.coords.add(chest.getPos());
                }

        }
}
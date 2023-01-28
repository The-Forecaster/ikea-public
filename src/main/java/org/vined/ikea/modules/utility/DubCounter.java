/*    */
package org.vined.ikea.modules.utility;

/*    */
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
/*    */ import meteordevelopment.meteorclient.events.world.TickEvent;
/*    */ import meteordevelopment.meteorclient.settings.IntSetting;
/*    */ import meteordevelopment.meteorclient.settings.Setting;
/*    */ import meteordevelopment.meteorclient.settings.SettingGroup;
/*    */ import meteordevelopment.meteorclient.systems.modules.Module;
/*    */ import meteordevelopment.orbit.EventHandler;
/*    */ import net.minecraft.util.Formatting;
/*    */ import net.minecraft.util.math.BlockPos;
/*    */ import net.minecraft.block.entity.BlockEntity;
/*    */ import net.minecraft.block.entity.ChestBlockEntity;
/*    */ import org.vined.ikea.IKEA;
/*    */ import org.vined.ikea.utils.TimerUtils;

/*    */
/*    */
/*    */ public class DubCounter
                /*    */ extends Module
/*    */ {
        /* 23 */ public List<net.minecraft.util.math.BlockPos> coords = new ArrayList<>();
        /* 24 */ public TimerUtils timer = new TimerUtils();
        /*    */
        /* 26 */ public SettingGroup sgGeneral = this.settings.getDefaultGroup();
        /* 27 */ private final Setting<Integer> loadTime = this.sgGeneral.add(new IntSetting.Builder()
                        /* 28 */ .name("load-time")
                        /* 29 */ .description(
                                        "How much time it's going to take to load all the dubs. (useful if there are dubs out of render distance so you can load them or if you have a performance mod)")
                        /* 30 */ .defaultValue(Integer.valueOf(1))
                        /* 31 */ .min(1)
                        /* 32 */ .sliderMax(60)
                        /* 33 */ .build());

        /*    */
        /*    */
        /*    */
        /*    */ public DubCounter() {
                /* 38 */ super(IKEA.UTILITY, "dub-counter", "Counts how many dubs are in render distance.");
                /*    */ }

        /*    */
        /*    */
        /*    */ public void onActivate() {
                /* 43 */ this.timer.reset();
                /* 44 */ info("Please wait " + net.minecraft.util.Formatting.RED + this.loadTime.get()
                                + net.minecraft.util.Formatting.GREEN + " second(s)...", new Object[0]);
                /*    */ }

        /*    */
        /*    */
        /*    */ public void onDeactivate() {
                /* 49 */ this.coords.clear();
                /*    */ }

        /*    */
        /*    */ @EventHandler
        /*    */ private void onTick(TickEvent.Post event) {
                /* 54 */ if (this.timer.hasReached(((Integer) this.loadTime.get()).intValue() * 1000L)) {
                        /* 55 */ int length = this.coords.size();
                        /* 56 */ if (length % 2 == 0) {
                                /* 57 */ int dubs = length / 2;
                                /* 58 */ info("There are roughly " + net.minecraft.util.Formatting.BOLD + dubs
                                                + net.minecraft.util.Formatting.GOLD + " (" + length
                                                + " normal chests) rendered double chests.", new Object[0]);
                                /* 59 */ toggle();
                                /*    */ } else {
                                /* 61 */ int fixed = length - 1;
                                /* 62 */ int dubs = fixed / 2;
                                /* 63 */ info("There are roughly " + net.minecraft.util.Formatting.GOLD + dubs
                                                + net.minecraft.util.Formatting.BOLD + " (" + fixed
                                                + " normal chests) rendered double chests.", new Object[0]);
                                /* 64 */ toggle();
                                /*    */ }
                        /* 66 */ this.timer.reset();
                        /*    */ }
                /*    */ }

        /*    */
        /*    */ @EventHandler
        /*    */ private void onRenderBlockEntity(RenderBlockEntityEvent event) {
                /* 72 */ net.minecraft.block.entity.BlockEntity block = event.blockEntity;
                /* 73 */ if (block instanceof net.minecraft.block.entity.ChestBlockEntity) {
                        net.minecraft.block.entity.ChestBlockEntity chest = (net.minecraft.block.entity.ChestBlockEntity) block;
                        /* 74 */ if (!this.coords.contains(chest.getPos()))
                                /* 75 */ this.coords.add(chest.getPos());
                }
                /*    */
                /*    */ }
        /*    */ }
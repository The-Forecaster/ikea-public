/*    */
package org.vined.ikea.modules.utility;

/*    */
/*    */ import java.util.Objects;
/*    */ import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
/*    */ import meteordevelopment.meteorclient.events.world.TickEvent;
/*    */ import meteordevelopment.meteorclient.settings.BoolSetting;
/*    */ import meteordevelopment.meteorclient.settings.IntSetting;
/*    */ import meteordevelopment.meteorclient.settings.Setting;
/*    */ import meteordevelopment.meteorclient.settings.SettingGroup;
/*    */ import meteordevelopment.meteorclient.systems.modules.Module;
/*    */ import meteordevelopment.meteorclient.utils.player.PlayerUtils;
/*    */ import meteordevelopment.orbit.EventHandler;
/*    */ import net.minecraft.block.entity.BlockEntity;
/*    */ import org.vined.ikea.IKEA;

/*    */
/*    */
/*    */
/*    */
/*    */ public class NoChestRender
        /*    */ extends Module
/*    */ {
    /* 22 */ public SettingGroup sgGeneral = this.settings.getDefaultGroup();
    /* 23 */ public SettingGroup sgParty = this.settings.createGroup("Party Mode");
    /*    */
    /* 25 */ private final Setting<Integer> radius = this.sgGeneral
            .add((Setting) ((IntSetting.Builder) ((IntSetting.Builder) ((IntSetting.Builder) (new IntSetting.Builder())
                    /* 26 */ .name("render-radius"))
                    /* 27 */ .description("The radius in which the storage blocks will render."))
                    /* 28 */ .defaultValue(Integer.valueOf(0)))
                    /* 29 */ .min(0)
                    /* 30 */ .sliderMax(128)
                    /* 31 */ .build());
    /*    */
    /*    */
    /* 34 */ private final Setting<Integer> partyModeMin = this.sgParty
            .add((Setting) ((IntSetting.Builder) ((IntSetting.Builder) ((IntSetting.Builder) (new IntSetting.Builder())
                    /* 35 */ .name("party-min-radius"))
                    /* 36 */ .description("The minimum radius party mode will change to."))
                    /* 37 */ .defaultValue(Integer.valueOf(0)))
                    /* 38 */ .min(0)
                    /* 39 */ .max(128)
                    /* 40 */ .sliderMax(128)
                    /* 41 */ .build());
    /*    */
    /*    */
    /* 44 */ private final Setting<Integer> partyModeMax = this.sgParty
            .add((Setting) ((IntSetting.Builder) ((IntSetting.Builder) ((IntSetting.Builder) (new IntSetting.Builder())
                    /* 45 */ .name("party-max-radius"))
                    /* 46 */ .description("The maximum radius party mode will change to."))
                    /* 47 */ .defaultValue(Integer.valueOf(64)))
                    /* 48 */ .min(1)
                    /* 49 */ .max(128)
                    /* 50 */ .sliderMax(128)
                    /* 51 */ .build());
    /*    */
    /*    */
    /* 54 */ private final Setting<Boolean> partyMode = this.sgParty.add(
            (Setting) ((BoolSetting.Builder) ((BoolSetting.Builder) ((BoolSetting.Builder) (new BoolSetting.Builder())
                    /* 55 */ .name("party-mode"))
                    /* 56 */ .description("Constantly changes the radius"))
                    /* 57 */ .defaultValue(Boolean.valueOf(false)))
                    /* 58 */ .build());
    /*    */
    /*    */ public boolean retreat = false;

    /*    */
    /*    */
    /*    */ public NoChestRender() {
        /* 64 */ super(IKEA.UTILITY, "no-chest-render", "Doesn't render chests.");
        /*    */ }

    /*    */
    /*    */ @EventHandler
    /*    */ private void onTick(TickEvent.Pre event) {
        /* 69 */ if (((Boolean) this.partyMode.get()).booleanValue()) {
            /* 70 */ if (((Integer) this.partyModeMin.get()).intValue() > ((Integer) this.partyModeMax.get())
                    .intValue()) {
                /* 71 */ info(
                        "The party mode minimum can't be bigger than the party mode maximum, setting the minimum to 0...",
                        new Object[0]);
                /* 72 */ this.partyModeMin.set(Integer.valueOf(0));
                /*    */ return;
                /*    */ }
            /* 75 */ if (this.retreat) {
                /* 76 */ if (Objects.equals(this.radius.get(), this.partyModeMin.get())) {
                    /* 77 */ this.retreat = false;
                    /*    */ return;
                    /*    */ }
                /* 80 */ this.radius.set(Integer.valueOf(((Integer) this.radius.get()).intValue() - 1));
                /*    */ }
            /* 82 */ else if (((Integer) this.radius.get()).intValue() >= ((Integer) this.partyModeMax.get())
                    .intValue()) {
                /* 83 */ this.retreat = true;
                /*    */ } else {
                /* 85 */ this.radius.set(Integer.valueOf(((Integer) this.radius.get()).intValue() + 1));
                /*    */ }
            /*    */ }
        /*    */ }

    /*    */
    /*    */
    /*    */ @EventHandler
    /*    */ private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        /* 93 */ net.minecraft.block.entity.BlockEntity block = event.blockEntity;
        /* 94 */ if (block instanceof net.minecraft.block.entity.ChestBlockEntity &&
        /* 95 */ PlayerUtils.distanceTo(block.getPos()) > ((Integer) this.radius.get()).intValue())
            /* 96 */ event.cancel();
        /*    */ }
    /*    */ }
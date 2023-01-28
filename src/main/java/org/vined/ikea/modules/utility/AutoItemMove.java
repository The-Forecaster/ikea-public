/*    */ package org.vined.ikea.modules.utility;
/*    */ 
/*    */ import java.util.List;
/*    */ import meteordevelopment.meteorclient.events.world.TickEvent;
/*    */ import meteordevelopment.meteorclient.settings.ItemListSetting;
/*    */ import meteordevelopment.meteorclient.settings.Setting;
/*    */ import meteordevelopment.meteorclient.settings.SettingGroup;
/*    */ import meteordevelopment.meteorclient.systems.modules.Module;
/*    */ import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
/*    */ import meteordevelopment.meteorclient.utils.player.InvUtils;
/*    */ import meteordevelopment.orbit.EventHandler;
/*    */ import net.minecraft.screen.ScreenHandler;
/*    */ import net.minecraft.screen.GenericContainerScreenHandler;
/*    */ import net.minecraft.screen.slot.Slot;
/*    */ import net.minecraft.item.Item;
/*    */ import org.vined.ikea.IKEA;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AutoItemMove
/*    */   extends Module
/*    */ {
/* 23 */   public SettingGroup sgGeneral = this.settings.getDefaultGroup();
/* 24 */   private final Setting<List<net.minecraft.item.Item>> items = this.sgGeneral.add((Setting)((ItemListSetting.Builder)((ItemListSetting.Builder)(new ItemListSetting.Builder())
/* 25 */       .name("items"))
/* 26 */       .description("Which items to put in the container."))
/* 27 */       .build());
/*    */ 
/*    */   
/*    */   public AutoItemMove() {
/* 31 */     super(IKEA.UTILITY, "auto-item-move", "Automatically puts items in a container.");
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   private void onTick(TickEvent.Post event) {
/* 36 */     assert this.mc.player != null;
/* 37 */     net.minecraft.screen.ScreenHandler handler = this.mc.player.currentScreenHandler;
/* 38 */     if (handler instanceof net.minecraft.screen.GenericContainerScreenHandler || handler instanceof net.minecraft.screen.ShulkerBoxScreenHandler) {
/* 39 */       moveItems(handler);
/*    */     }
/*    */   }
/*    */   
/*    */   private int getRows(net.minecraft.screen.ScreenHandler handler) {
/* 44 */     return (handler instanceof net.minecraft.screen.GenericContainerScreenHandler) ? ((net.minecraft.screen.GenericContainerScreenHandler)handler).getRows() : 3;
/*    */   }
/*    */   
/*    */   public void moveItems(net.minecraft.screen.ScreenHandler handler) {
/* 48 */     int playerInvOffset = getRows(handler) * 9;
/* 49 */     MeteorExecutor.execute(() -> moveSlots(handler, playerInvOffset, playerInvOffset + 36));
/*    */   }
/*    */   
/*    */   private void moveSlots(net.minecraft.screen.ScreenHandler handler, int start, int end) {
/* 53 */     for (int i = start; i < end; i++) {
/* 54 */       net.minecraft.screen.slot.Slot slot = handler.getSlot(i);
/* 55 */       if (slot.hasStack() && (
/* 56 */         (List)this.items.get()).contains(slot.getStack().getItem())) {
/* 57 */         if (this.mc.currentScreen == null)
/*    */           break; 
/* 59 */         InvUtils.quickMove().slotId(i);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }
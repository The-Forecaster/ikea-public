/*    */ package org.vined.ikea;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import meteordevelopment.meteorclient.addons.MeteorAddon;
/*    */ import meteordevelopment.meteorclient.systems.modules.Category;
/*    */ import meteordevelopment.meteorclient.systems.modules.Module;
/*    */ import meteordevelopment.meteorclient.systems.modules.Modules;
/*    */ import org.slf4j.Logger;
/*    */ import org.vined.ikea.modules.dupes.IKEADupe;
/*    */ import org.vined.ikea.modules.utility.AutoItemMove;
/*    */ import org.vined.ikea.modules.utility.DubCounter;
/*    */ import org.vined.ikea.modules.utility.NoChestRender;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IKEA
/*    */   extends MeteorAddon
/*    */ {
/* 19 */   public static final Logger LOG = LogUtils.getLogger();
/* 20 */   public static final Category UTILITY = new Category("IKEA Utility");
/* 21 */   public static final Category DUPES = new Category("IKEA Dupes");
/* 22 */   public static final Category MISC = new Category("IKEA Misc");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onInitialize() {
/* 28 */     LOG.info("Initializing IKEA Addon");
/*    */ 
/*    */     
/* 31 */     Modules.get().add((Module)new IKEADupe());
/* 32 */     Modules.get().add((Module)new NoChestRender());
/* 33 */     Modules.get().add((Module)new DubCounter());
/* 34 */     Modules.get().add((Module)new AutoItemMove());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onRegisterCategories() {
/* 40 */     Modules.registerCategory(UTILITY);
/* 41 */     Modules.registerCategory(DUPES);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPackage() {
/* 46 */     return "org.vined.ikea";
/*    */   }
/*    */ }


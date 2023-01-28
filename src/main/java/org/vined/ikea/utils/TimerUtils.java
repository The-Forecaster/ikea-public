/*    */ package org.vined.ikea.utils;
/*    */ 
/*    */ public class TimerUtils {
/*  4 */   private long lastMS = 0L;
/*    */   
/*    */   public boolean hasReached(long ms) {
/*  7 */     return (System.currentTimeMillis() - this.lastMS >= ms);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 11 */     this.lastMS = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public long getLastMS() {
/* 15 */     return this.lastMS;
/*    */   }
/*    */   
/*    */   public void setLastMS(long lastMS) {
/* 19 */     this.lastMS = lastMS;
/*    */   }
/*    */   
/*    */   public long getDifference() {
/* 23 */     return System.currentTimeMillis() - this.lastMS;
/*    */   }
/*    */   
/*    */   public long getDifference(long time) {
/* 27 */     return System.currentTimeMillis() - time;
/*    */   }
/*    */   
/*    */   public long getDifferenceLastMS(long lastMS) {
/* 31 */     return System.currentTimeMillis() - lastMS;
/*    */   }
/*    */   
/*    */   public boolean hasReached(long ms, boolean reset) {
/* 35 */     if (System.currentTimeMillis() - this.lastMS >= ms) {
/* 36 */       if (reset) {
/* 37 */         reset();
/*    */       }
/* 39 */       return true;
/*    */     } 
/* 41 */     return false;
/*    */   }
/*    */   
/*    */   public boolean hasReached(long ms, long lastMS) {
/* 45 */     return (System.currentTimeMillis() - lastMS >= ms);
/*    */   }
/*    */   
/*    */   public boolean hasReached(long ms, long lastMS, boolean reset) {
/* 49 */     if (System.currentTimeMillis() - lastMS >= ms) {
/* 50 */       if (reset) {
/* 51 */         reset();
/*    */       }
/* 53 */       return true;
/*    */     } 
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   public boolean hasReached(long ms, boolean reset, long lastMS) {
/* 59 */     if (System.currentTimeMillis() - lastMS >= ms) {
/* 60 */       if (reset) {
/* 61 */         reset();
/*    */       }
/* 63 */       return true;
/*    */     } 
/* 65 */     return false;
/*    */   }
/*    */   
/*    */   public boolean hasReached(long ms, long lastMS, long difference) {
/* 69 */     return (System.currentTimeMillis() - lastMS >= ms);
/*    */   }
/*    */   
/*    */   public boolean hasReached(long ms, long lastMS, long difference, boolean reset) {
/* 73 */     if (System.currentTimeMillis() - lastMS >= ms) {
/* 74 */       if (reset) {
/* 75 */         reset();
/*    */       }
/* 77 */       return true;
/*    */     } 
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getTimeSinceLastReset(long ms) {
/* 84 */     return System.currentTimeMillis() - ms;
/*    */   }
/*    */ }
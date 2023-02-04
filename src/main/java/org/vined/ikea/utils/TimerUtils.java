package org.vined.ikea.utils;

public final class TimerUtils {
    private long lastMS = 0L;

    public final boolean hasReached(long ms) {
        return System.currentTimeMillis() - this.lastMS >= ms;
    }

    public final void reset() {
        this.lastMS = System.currentTimeMillis();
    }

    public final long getLastMS() {
        return this.lastMS;
    }

    public final void setLastMS(long lastMS) {
        this.lastMS = lastMS;
    }

    public final long getDifference() {
        return System.currentTimeMillis() - this.lastMS;
    }

    public final long getDifference(long time) {
        return System.currentTimeMillis() - time;
    }

    public final long getDifferenceLastMS(long lastMS) {
        return System.currentTimeMillis() - lastMS;
    }

    public final boolean hasReached(long ms, boolean reset) {
        if (System.currentTimeMillis() - this.lastMS >= ms) {
            if (reset) {
                reset();
            }
            return true;
        }
        return false;
    }

    public final boolean hasReached(long ms, long lastMS) {
        return (System.currentTimeMillis() - lastMS >= ms);
    }

    public final boolean hasReached(long ms, long lastMS, boolean reset) {
        if (System.currentTimeMillis() - lastMS >= ms) {
            if (reset) {
                reset();
            }
            return true;
        }
        return false;
    }

    public final boolean hasReached(long ms, boolean reset, long lastMS) {
        if (System.currentTimeMillis() - lastMS >= ms) {
            if (reset) {
                reset();
            }
            return true;
        }
        return false;
    }

    public final boolean hasReached(long ms, long lastMS, long difference) {
        return (System.currentTimeMillis() - lastMS >= ms);
    }

    public final boolean hasReached(long ms, long lastMS, long difference, boolean reset) {
        if (System.currentTimeMillis() - lastMS >= ms) {
            if (reset) {
                reset();
            }
            return true;
        }
        return false;
    }

    public final long getTimeSinceLastReset(long ms) {
        return System.currentTimeMillis() - ms;
    }
}
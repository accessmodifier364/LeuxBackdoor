/*
 * Decompiled with CFR 0.151.
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok;

public class TurokTick {
    private long ticks = -1L;

    public void reset() {
        this.ticks = System.currentTimeMillis();
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public long getTicks() {
        return this.ticks;
    }

    public float getCurrentTicks() {
        return System.currentTimeMillis() - this.ticks;
    }

    public int getCurrentTicksCount(double speed) {
        return (int) ((double) (System.currentTimeMillis() - this.ticks) / speed);
    }

    public boolean isPassedMS(float ms) {
        return (float) (System.currentTimeMillis() - this.ticks) >= ms;
    }

    public boolean isPassedSI(float si) {
        return (float) (System.currentTimeMillis() - this.ticks) >= si * 1000.0f;
    }
}


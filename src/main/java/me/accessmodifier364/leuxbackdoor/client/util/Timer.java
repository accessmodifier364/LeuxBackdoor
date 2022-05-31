package me.accessmodifier364.leuxbackdoor.client.util;

public class Timer {

    private long time;

    public Timer() {
        this.time = -1L;
    }

    public boolean passed(final long ms) {
        return this.getTime(System.nanoTime() - this.time) >= ms;
    }

    public boolean passed(double ms) {
        return (double) (System.currentTimeMillis() - this.time) >= ms;
    }

    public boolean passedS(double s) {
        return this.passedMs((long) s * 1000L);
    }

    public boolean passedMs(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public long getPassedTimeMs() {
        return this.getTime(System.nanoTime() - this.time);
    }

    public boolean passedNS(long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }

    public void setMs(long ms) {
        this.time = System.nanoTime() - this.convertToNS(ms);
    }

    public Timer reset() {
        this.time = System.nanoTime();
        return this;
    }

    public long getTime(final long time) {
        return time / 1000000L;
    }

    public void resetTimeSkipTo(long p_MS) {
        this.time = System.currentTimeMillis() + p_MS;
    }
}
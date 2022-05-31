/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok.mouse;

import org.lwjgl.input.Mouse;

public class TurokMouse {
    private int scroll;
    private final int x;
    private final int y;

    public TurokMouse(int mx, int my) {
        this.x = mx;
        this.y = my;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    public int getScroll() {
        return this.scroll;
    }

    public boolean hasWheel() {
        return Mouse.hasWheel();
    }
}


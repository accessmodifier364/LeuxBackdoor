/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class TurokDisplay {
    private final Minecraft mc;
    private int scaleFactor;
    private float partialTicks;

    public TurokDisplay(Minecraft mc) {
        this.mc = mc;
        this.scaleFactor = 1;
    }

    public int getWidth() {
        return this.mc.displayWidth;
    }

    public int getHeight() {
        return this.mc.displayHeight;
    }

    public int getScaledWidth() {
        this.onUpdate();
        return (int) ((double) this.mc.displayWidth / (double) this.scaleFactor);
    }

    public int getScaledHeight() {
        this.onUpdate();
        return (int) ((double) this.mc.displayHeight / (double) this.scaleFactor);
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks(GuiScreen screen) {
        return this.partialTicks;
    }

    protected void onUpdate() {
        boolean isUnicode = this.mc.isUnicode();
        int minecraftScale = this.mc.gameSettings.guiScale;
        if (minecraftScale == 0) {
            minecraftScale = 1000;
        }
        while (this.scaleFactor < minecraftScale && this.getWidth() / (this.scaleFactor + 1) >= 320 && this.getHeight() / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (isUnicode && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
    }
}


/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.opengl.GL11
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok.render.image.management;

import me.accessmodifier364.leuxbackdoor.client.util.turok.render.image.TurokImage;
import me.accessmodifier364.leuxbackdoor.client.util.turok.render.opengl.TurokRenderGL;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TurokImageManager {
    public static void render(TurokImage image, int x, int y, float xx, float yy, int w, int h, float ww, float hh, Color color) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        TurokRenderGL.enableState(3553);
        TurokRenderGL.enableState(2884);
        TurokRenderGL.disableState(2929);
        GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(image.getResourceLocation());
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        TurokRenderGL.drawTextureInterpolated(x, y, xx, yy, w, h, ww, hh);
        TurokRenderGL.disableState(3042);
        TurokRenderGL.disableState(3553);
        TurokRenderGL.disableState(2884);
        TurokRenderGL.enableState(2929);
    }

    public static void render(TurokImage image, int x, int y, int w, int h, Color color) {
        TurokImageManager.render(image, x, y, w, h, 0, 0, 1.0f, 1.0f, color);
    }
}


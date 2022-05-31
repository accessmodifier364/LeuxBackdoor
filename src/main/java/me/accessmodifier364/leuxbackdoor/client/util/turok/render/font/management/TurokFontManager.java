/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok.render.font.management;

import me.accessmodifier364.leuxbackdoor.client.util.turok.render.font.TurokFont;
import me.accessmodifier364.leuxbackdoor.client.util.turok.render.opengl.TurokRenderGL;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class TurokFontManager {
    public static void render(TurokFont fontRenderer, String string, int x, int y, boolean shadow, Color color) {
        TurokRenderGL.enableState(3553);
        TurokRenderGL.enableState(3042);
        TurokRenderGL.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        if (shadow) {
            if (fontRenderer.isRenderingCustomFont()) {
                fontRenderer.drawStringWithShadow(string, x, y, color.getRGB());
            } else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, (float) x, (float) y, color.getRGB());
            }
        } else if (fontRenderer.isRenderingCustomFont()) {
            fontRenderer.drawString(string, x, y, color.getRGB());
        } else {
            Minecraft.getMinecraft().fontRenderer.drawString(string, x, y, color.getRGB());
        }
        TurokRenderGL.disableState(3553);
    }

    public static int getStringWidth(TurokFont fontRenderer, String string) {
        return fontRenderer.isRenderingCustomFont() ? fontRenderer.getStringWidth(string) : Minecraft.getMinecraft().fontRenderer.getStringWidth(string);
    }

    public static int getStringHeight(TurokFont fontRenderer, String string) {
        return fontRenderer.isRenderingCustomFont() ? fontRenderer.getStringHeight(string) : Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * fontRenderer.getFontSize();
    }
}


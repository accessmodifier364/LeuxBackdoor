package me.accessmodifier364.leuxbackdoor.client.util;

import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;

public class FontUtil {
    private static final Minecraft mc;

    public static float drawStringWithShadow(final boolean customFont, final String text, final int x, final int y, final int color) {
        if (customFont) {
            return ModLoader.fontRenderer.drawStringWithShadow(text, (float) x, (float) y, color);
        }
        return (float) mc.fontRenderer.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public static float drawStringWithShadow(final boolean customFont, final String text, final double x, final double y, final int color) {
        if (customFont) {
            return ModLoader.fontRenderer.drawStringWithShadow(text, (float) x, (float) y, color);
        }
        return (float) mc.fontRenderer.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    public static int getStringWidth(final boolean customFont, final String str) {
        if (customFont) {
            return ModLoader.fontRenderer.getStringWidth(str);
        }
        return mc.fontRenderer.getStringWidth(str);
    }

    public static int getFontHeight(final boolean customFont) {
        if (customFont) {
            return ModLoader.fontRenderer.getHeight();
        }
        return mc.fontRenderer.FONT_HEIGHT;
    }

    public static float drawKeyStringWithShadow(final boolean customFont, final String text, final int x, final int y, final int color) {
        if (customFont) {
            return ModLoader.fontRenderer.drawStringWithShadow(text, x, y, color);
        }
        return (float) mc.fontRenderer.drawStringWithShadow(text, (float) x, (float) y, color);
    }

    static {
        mc = Minecraft.getMinecraft();
    }
}

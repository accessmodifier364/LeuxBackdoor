package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font;

import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;

public class FontUtil {
    protected static Minecraft mc;
    
    public static void drawString(final String text, final float x, final float y, final int colour) {
            ModLoader.latoFont.drawString(text, x, y, colour);
    }
    
    public static void drawStringWithShadow(final String text, final float x, final float y, final int colour) {
            ModLoader.latoFont.drawStringWithShadow(text, x, y, colour);
    }
    
    public static void drawText(final String text, final float x, final float y, final int colour) {
            drawStringWithShadow(text, x, y, colour);
    }
    
    public static int getFontHeight() {
            return ModLoader.latoFont.getHeight();
    }
    
    static {
        FontUtil.mc = Minecraft.getMinecraft();
    }
}

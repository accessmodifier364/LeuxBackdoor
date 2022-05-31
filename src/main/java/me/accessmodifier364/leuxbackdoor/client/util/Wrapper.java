package me.accessmodifier364.leuxbackdoor.client.util;

import me.accessmodifier364.leuxbackdoor.client.modules.client.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
    final static Minecraft mc = Minecraft.getMinecraft();

    public static EntityPlayerSP GetPlayer() {
        return mc.player;
    }

    // NEW THINGS
    private static FontRenderer fontRenderer;

    public static void init() {
        fontRenderer = ClickGUI.mc.fontRenderer;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static World getWorld() {
        return (getMinecraft()).world;
    }

    public static int getKey(String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }

    public static FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public static EntityPlayerSP getPlayer() {
        return Wrapper.getMinecraft().player;
    }
}

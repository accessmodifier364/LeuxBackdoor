package me.accessmodifier364.leuxbackdoor.client.guiscreen.render;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.font.CFontRenderer;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokOld;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import me.accessmodifier364.leuxbackdoor.client.util.turok.task.Rect;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Arrays;

public class Draw {
	private static final FontRenderer font_renderer = Minecraft.getMinecraft().fontRenderer;
	private static final CFontRenderer custom_font = ModLoader.fontRenderer;
	public static Draw INSTANCE;
	private final float size;

	public Draw(float size) {
		this.size = size;
		INSTANCE = this;
	}

	public static float drawString(String text, float x, float y, int color, boolean shadow) {
			if (shadow) {
				custom_font.drawStringWithShadow(text, x, y, color);
			} else {
				custom_font.drawString(text, x, y, color);
			}
			return x;
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a) {
		Gui.drawRect(x, y, w, h, new ClientColor(r, g, b, a).hex());
	}

	public static void draw_rect(int x, int y, int w, int h, int r, int g, int b, int a, int size, String type) {
		if (Arrays.asList(type.split("-")).contains("up")) {
			Draw.draw_rect(x, y, x + w, y + size, r, g, b, a);
		}
		if (Arrays.asList(type.split("-")).contains("down")) {
			Draw.draw_rect(x, y + h - size, x + w, y + h, r, g, b, a);
		}
		if (Arrays.asList(type.split("-")).contains("left")) {
			Draw.draw_rect(x, y, x + size, y + h, r, g, b, a);
		}
		if (Arrays.asList(type.split("-")).contains("right")) {
			Draw.draw_rect(x + w - size, y, x + w, y + h, r, g, b, a);
		}
	}

	public static void draw_rect(Rect rect, int r, int g, int b, int a) {
		Gui.drawRect(rect.get_x(), rect.get_y(), rect.get_screen_width(), rect.get_screen_height(), new ClientColor(r, g, b, a).hex());
	}

	public static void draw_string_shadow(String string, int x, int y, int r, int g, int b, int a) {
		FontUtil.drawText(string, (float) x, (float) y, new ClientColor(r, g, b, a).hex());
	}

	public static void draw_string(String string, int x, int y, int r, int g, int b, int a) {
		FontUtil.drawText(string, (float)x, (float)y, new ClientColor(r, g, b, a).hex());
	}

	public void draw_string_gl(String string, int x, int y, int r, int g, int b) {
		TurokOld resize_gl = new TurokOld("Resize");
		resize_gl.resize(x, y, this.size);
		custom_font.drawString(string, x, y, new ClientColor(r, g, b).hex());
		resize_gl.resize(x, y, this.size, "end");
		GL11.glPushMatrix();
		GL11.glEnable(3553);
		GL11.glEnable(3042);
		GlStateManager.enableBlend();
		GL11.glPopMatrix();
		RenderHelp.release_gl();
	}

	public int get_string_height() {
		CFontRenderer fontRenderer = custom_font;
		return (int)((float)fontRenderer.getHeight() * this.size);
	}

	public int get_string_width(String string) {
		CFontRenderer fontRenderer = custom_font;
		return (int)((float)fontRenderer.getStringWidth(string) * this.size);
	}

	public int get_other_string_width(String string, boolean smoth) {
		CFontRenderer fontRenderer = custom_font;
		if (smoth) {
			return custom_font.getStringWidth(string);
		}
		return (int)((float)fontRenderer.getStringWidth(string) * this.size);
	}

	public static class ClientColor extends Color {
		public ClientColor(int r, int g, int b, int a) {
			super(r, g, b, a);
		}

		public ClientColor(int r, int g, int b) {
			super(r, g, b);
		}

		public int hex() {
			return getRGB();
		}
	}
}
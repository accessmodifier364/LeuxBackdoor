package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.client.modules.client.ClickHUD;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Logo extends Pinnable {

	public static ResourceLocation logo = new ResourceLocation("custom/logo.png");

	public Logo() {
		super("Logo", "Logo", 1, 0, 0);
	}

	@Override
	public void render() {
		this.renderLogo();
	}

	public void renderLogo() {
		int width = ClickHUD.INSTANCE.width.get_value(1);
		int height = ClickHUD.INSTANCE.heigth.get_value(1);
		int x = ClickHUD.INSTANCE.x.get_value(1);
		int y = ClickHUD.INSTANCE.y.get_value(1);
		mc.renderEngine.bindTexture(logo);
		GlStateManager.color(255.0F, 255.0F, 255.0F);
		Gui.drawScaledCustomSizeModalRect(x - 2, y - 36, 7.0F, 7.0F, width - 7, height - 7, width, height, (float) width, (float) height);
	}
}
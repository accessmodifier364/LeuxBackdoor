package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiModule extends Module {

	public GuiModule() {
		super(Category.client);
		this.name = "GuiModule";
		this.description = "Secondary GUI";
		set_bind(ModLoader.KEY_GUI);
	}

	Setting blur = create("Blur", "Blur", false);

	Setting red = create("Red", "Red", 255, 0, 255);
	Setting green = create("Green", "Green", 40, 0, 255);
	Setting blue = create("Blue", "Blue", 40, 0, 255);
	Setting alpha = create("Alpha", "Alpha", 128, 0, 255);

	Setting rainbow = create("Rainbow", "Rainbow", true);
	Setting saturation = create("Saturation", "Saturation", 80, 0, 100);
	Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
	Setting speed = create("Speed", "Speed", 40, 1, 100);

	@Override
	protected void enable() {
		if (mc.player != null && mc.world != null) {
			if (blur.get_value(true)) {
				mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
			mc.displayGuiScreen(ModLoader.new_gui);
		}
	}

	@Override
	protected void disable() {
		if (mc.player != null && mc.world != null) {
			try {
				if (mc.entityRenderer.isShaderActive()) {
					mc.entityRenderer.getShaderGroup().deleteShaderGroup();
				}
			} catch (Exception ignored) {
			}
			mc.displayGuiScreen(null);
		}
	}

	@Override
	public void update() {
		if (rainbow.get_value(true)) {
			Color rainbowColor = new Color(RenderUtil.getRainbow(speed.get_value(1) * 100, 0, (float) saturation.get_value(1) / 100.0f, (float) brightness.get_value(1) / 100.0f));
			red.set_value(rainbowColor.getRed());
			green.set_value(rainbowColor.getGreen());
			blue.set_value(rainbowColor.getBlue());
		}
	}

}
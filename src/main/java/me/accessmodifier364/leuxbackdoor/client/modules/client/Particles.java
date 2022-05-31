package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.NewGui;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;

import java.awt.*;

public class Particles extends Module {
	public Particles() {
		super(Category.client);
		this.name = "Particles";
		this.description = "superblaubeere27";
	}

	Setting particlesAround = create("ParticlesAround", "ParticlesAround", false);

	Setting size = create("Size", "Size", 8.0, 4.0, 12.0);

	Setting lineDistance = create("LineDistance", "LineDistance", 60, 0, 100);
	Setting ticks = create("Ticks", "Ticks", 	15, 0, 30);
	Setting quantity = create("Quantity", "Quantity", 250, 0, 500);
	Setting lineWidth = create("LineWidth", "LineWidth", 1.0f, 0.0f, 5.0f);

	Setting red = create("Red", "Red", 255, 0, 255);
	Setting green = create("Green", "Green", 40, 0, 255);
	Setting blue = create("Blue", "Blue", 40, 0, 255);
	Setting rainbow = create("Rainbow", "Rainbow", true);
	Setting saturation = create("Saturation", "Saturation", 50, 0, 100);
	Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
	Setting speed = create("Speed", "Speed", 40, 1, 100);

	@Override
	public void update() {
		if (rainbow.get_value(true)) {
			Color rainbowColor = new Color(RenderUtil.getRainbow(speed.get_value(1) * 100, 0, (float) saturation.get_value(1) / 100.0f, (float) brightness.get_value(1) / 100.0f));
			red.set_value(rainbowColor.getRed());
			green.set_value(rainbowColor.getGreen());
			blue.set_value(rainbowColor.getBlue());
		}
	}

	@Override
	public void enable() {
		NewGui.particleSystem.addParticles(quantity.get_value(1));
	}

	@Override
	public void disable() {
		NewGui.particleSystem.deleteAllParticles();
	}

}
package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;


import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;


public class Watermark extends Pinnable {

	public static String line = ModLoader.CLIENT_NAME + ModLoader.g + " v" + ModLoader.get_version();

	public Watermark() {
		super("Watermark", "Watermark", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
		int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
		int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
		int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

		if (ModLoader.get_setting_manager().get_setting_with_tag("Settings", "RainbowAura").get_value(true)) {
			line = ModLoader.CLIENT_NAME + " v" + ModLoader.get_version();
			create_rainbow_line(line, this.docking(1, line), 2);
		} else {
			line = ModLoader.CLIENT_NAME + ModLoader.g + " v" + ModLoader.get_version();
			create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);
		}

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}
}
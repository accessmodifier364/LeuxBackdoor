package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;


import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;

public class FPS extends Pinnable {
    
    public FPS() {
        super("Fps", "Fps", 1, 0, 0);
    }

    @Override
	public void render() {
		int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
		int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
		int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
		int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

		String line = "FPS: " + get_fps();

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
	}
	
    public String get_fps() {
		int fps = Minecraft.getDebugFPS();
		if (fps >= 50) {
			return "\u00A7a" + fps;
		} else if (fps >= 25) {
			return "\u00A7e" + fps;
		} else {
			return "\u00A7c" + fps;
		}
    }

}
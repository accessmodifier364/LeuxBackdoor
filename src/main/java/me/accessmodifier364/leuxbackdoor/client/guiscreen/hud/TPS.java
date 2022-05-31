package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import me.accessmodifier364.leuxbackdoor.client.event.EventHandler;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class TPS extends Pinnable {

    public TPS() {
        super("TPS", "TPS", 1, 0, 0);
    }

    @Override
	public void render() {
		int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
		int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
		int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
		int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

		String line = "TPS: " + getTPS();

		create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);

		this.set_width(this.get(line, "width") + 2);
		this.set_height(this.get(line, "height") + 2);
    }

    public String getTPS() {
        try {
            int tps = Math.round(EventHandler.INSTANCE.get_tick_rate());
        if (tps >= 16) {
            return "\u00A7a"+ tps;
        } else if (tps >= 10) {
            return "\u00A7e"+ tps;
        } else {
            return "\u00A7c"+ tps;
        }
        } catch (Exception e) {
            return "oh no " +e;
        }
    }
    
}
package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;


public class Coordinates extends Pinnable {
	ChatFormatting g = ChatFormatting.GRAY;
	ChatFormatting r = ChatFormatting.RED;

	public Coordinates() {
		super("Coordinates", "Coordinates", 1, 0, 0);
	}

	@Override
	public void render() {
		int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
		int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
		int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
		int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

		String line;

			String x = g + "[" + ModLoader.r + (int) (mc.player.posX) + g + "]" + ModLoader.r;
			String y = g + "[" + ModLoader.r + (int) (mc.player.posY) + g + "]" + ModLoader.r;
			String z = g + "[" + ModLoader.r + (int) (mc.player.posZ) + g + "]" + ModLoader.r;

			String x_nether = g + "[" + r + Math.round(mc.player.dimension != -1 ? (mc.player.posX / 8) : (mc.player.posX * 8)) + g + "]" + ModLoader.r;
			String z_nether = g + "[" + r + Math.round(mc.player.dimension != -1 ? (mc.player.posZ / 8) : (mc.player.posZ * 8)) + g + "]" + ModLoader.r;

			line = "XYZ " + x + y + z + r + " XZ " + x_nether + z_nether;

			create_line(line, this.docking(1, line), 2, nl_r, nl_g, nl_b, nl_a);


		this.set_width(this.get(line, "width"));
		this.set_height(this.get(line, "height") + 2);
	}
}
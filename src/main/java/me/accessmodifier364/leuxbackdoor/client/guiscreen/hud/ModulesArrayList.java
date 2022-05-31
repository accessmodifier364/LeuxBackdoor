package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import com.google.common.collect.Lists;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.DrawnUtil;
import me.accessmodifier364.leuxbackdoor.client.util.FontUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ModulesArrayList extends Pinnable {
	public ModulesArrayList() {
		super("Array List", "ArrayList", 1, 0, 0);
	}

	boolean flag = true;

	private int scaled_width;
	private int scaled_height;

    @Override
	public void render() {
		updateResolution();
		int position_update_y = 2;

		int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
		int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
		int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
		int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

		List<Module> pretty_modules = ModLoader.get_hack_manager().get_array_active_hacks().stream()
			.sorted(Comparator.comparing(modules -> get(modules.array_detail() == null ? modules.get_tag() : modules.get_tag() + ModLoader.g + " [" + ModLoader.r + modules.array_detail() + ModLoader.g + "]" + ModLoader.r, "width")))
			.collect(Collectors.toList());

		int count = 0;

		if (ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Top R") || ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Top L")) {
			pretty_modules = Lists.reverse(pretty_modules);
		}

		for (Module modules : pretty_modules) {

			flag = true;

			if (modules.get_category().get_name().equals("Client")) {
				continue;
			}

			for (String s : DrawnUtil.hidden_tags) {
				if (modules.get_tag().equalsIgnoreCase(s)) {
					flag = false;
					break;
				}
				if (!flag) break;
			}
			
			if (flag) {
				String module_name = (
					modules.array_detail() == null ? modules.get_tag() :
					modules.get_tag() + ModLoader.g + " [" + ModLoader.r + modules.array_detail() + ModLoader.g + "]" + ModLoader.r
				);

				if (ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Free")) {
					create_line(module_name, this.docking(2, module_name), position_update_y, nl_r, nl_g, nl_b, nl_a);

					position_update_y += get(module_name, "height") + 2;

					if (get(module_name, "width") > this.get_width()) {
						this.set_width(get(module_name, "width") + 2);
					}

					this.set_height(position_update_y);
				} else {
					if (ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Top R")) {
						FontUtil.drawStringWithShadow(true, module_name, scaled_width - 2 - FontUtil.getStringWidth(true, module_name), 3 + count * 10, new Draw.ClientColor(nl_r,nl_g,nl_b,nl_a).hex());
						count++;
					}
					if (ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Top L")) {
						FontUtil.drawStringWithShadow(true, module_name, 2, 3 + count * 10, new Draw.ClientColor(nl_r,nl_g,nl_b,nl_a).hex());
						count++;
					}
					if (ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Bottom R")) {
						FontUtil.drawStringWithShadow(true, module_name, scaled_width - 2 - FontUtil.getStringWidth(true, module_name), scaled_height - (count * 10), new Draw.ClientColor(nl_r,nl_g,nl_b,nl_a).hex());
						count++;
					}
					if (ModLoader.get_setting_manager().get_setting_with_tag("HUD", "HUDArrayList").in("Bottom L")) {
						FontUtil.drawStringWithShadow(true, module_name, 2, scaled_height - (count * 10), new Draw.ClientColor(nl_r,nl_g,nl_b,nl_a).hex());
						count++;
					}
				}


			}			
		}
	}

	public void updateResolution() {
		this.scaled_width = mc.displayWidth;
		this.scaled_height = mc.displayHeight;
        int scale_factor = 1;
		final boolean flag = mc.isUnicode();
		int i = mc.gameSettings.guiScale;
		if (i == 0) {
			i = 1000;
		}
		while (scale_factor < i && this.scaled_width / (scale_factor + 1) >= 320 && this.scaled_height / (scale_factor + 1) >= 240) {
			++scale_factor;
		}
		if (flag && scale_factor % 2 != 0 && scale_factor != 1) {
			--scale_factor;
		}
		final double scaledWidthD = this.scaled_width / (double) scale_factor;
		final double scaledHeightD = this.scaled_height / (double) scale_factor;
		this.scaled_width = MathHelper.ceil(scaledWidthD);
		this.scaled_height = MathHelper.ceil(scaledHeightD);
	}
}
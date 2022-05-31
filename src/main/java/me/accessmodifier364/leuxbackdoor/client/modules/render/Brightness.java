package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class Brightness extends Module {
	public Brightness() {
		super(Category.render);
		this.name = "Brightness";
		this.description = "be like my dog :3";
	}

	Setting mode = create("Mode", "Mode", "Potion", combobox("Gamma", "Potion"));
	float old_gamma_value;

	public void enable() {
		old_gamma_value = mc.gameSettings.gammaSetting;
	}

	public void update() {
		if (this.mode.in("Gamma")) {
			mc.gameSettings.gammaSetting = 20.0F;
		} else {
			mc.gameSettings.gammaSetting = 1.0F;
			mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 5210));
		}
	}

	public void disable() {
		mc.gameSettings.gammaSetting = old_gamma_value;
		mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
	}

	@Override
	public String array_detail() {
		return mode.get_current_value();
	}
}

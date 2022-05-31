package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class ClickHUD extends Module {
	public ClickHUD() {
		super(Category.client);
		this.name        = "HUD";
		this.description = "gui for pinnables";
		INSTANCE = this;
	}

	public static ClickHUD INSTANCE;

	Setting r = create("Color R", "ColorR", 80, 0, 255);
	Setting g = create("Color G", "ColorG", 80, 0, 255);
	Setting b = create("Color B", "ColorB", 200, 0, 255);
	Setting a = create("Color A", "ColorA", 255, 0, 255);

	Setting tabcolor = create("Tab Color", "TabColor", false);
	Setting rainbowPrefix = create("RainbowPrefix", "RainbowPrefix", false);

	public Setting width = create("LogoWidth", "Width", 100, 0, 400);
	public Setting heigth = create("LogoHeigth", "Heigth", 100, 0, 400);
	public Setting x = create("LogoPosX", "PosX", 10, 0, 200);
	public Setting y = create("LogoPosY", "PosY", 10, 0, 200);

	Setting compass_scale = create("Compass Scale", "HUDCompassScale", 16, 1, 60);
	Setting arraylist_mode = create("ArrayList", "HUDArrayList", "Top R", combobox("Free", "Top R", "Top L", "Bottom R", "Bottom L"));
	Setting show_all_pots = create("All Potions", "HUDAllPotions", true);
	Setting max_player_list = create("Max Players", "HUDMaxPlayers", 10, 1, 64);

	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			ModLoader.get_hack_manager().get_module_with_tag("GUI").set_active(false);
			ModLoader.click_hud.back = false;
			mc.displayGuiScreen(ModLoader.click_hud);
		}
	}
}
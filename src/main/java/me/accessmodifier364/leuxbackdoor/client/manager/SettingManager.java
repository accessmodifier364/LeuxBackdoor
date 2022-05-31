package me.accessmodifier364.leuxbackdoor.client.manager;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

import java.util.ArrayList;


public class SettingManager {

	public ArrayList<Setting> array_setting;

	public SettingManager() {
		this.array_setting = new ArrayList<>();
	}

	public void register(Setting setting) {
		this.array_setting.add(setting);
	}

	public ArrayList<Setting> get_array_settings() {
		return this.array_setting;
	}

	public Setting get_setting_with_tag(Module module, String tag) {
		Setting setting_requested = null;
		for (Setting settings : get_array_settings()) {
			if (settings.get_master().equals(module) && settings.get_tag().equalsIgnoreCase(tag)) {
				setting_requested = settings;
			}
		}
		return setting_requested;
	}

	public Setting get_setting_with_tag(String tag, String tag_) {
		Setting setting_requested = null;

		for (Setting settings : get_array_settings()) {
			if (settings.get_master().get_tag().equalsIgnoreCase(tag) && settings.get_tag().equalsIgnoreCase(tag_)) {
				setting_requested = settings;
				break;
			}
		}

		return setting_requested;
	}

	public ArrayList<Setting> get_settings_with_hack(Module module) {
		ArrayList<Setting> setting_requesteds = new ArrayList<>();

		for (Setting settings : get_array_settings()) {
			if (settings.get_master().equals(module)) {
				setting_requesteds.add(settings);
			}
		}

		return setting_requesteds;
	}
}
package me.accessmodifier364.leuxbackdoor.client.modules;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderEntityModel;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.Listenable;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module implements Listenable {
	public Category category;

	public String name;
	public String description;

	public int bind;

	public boolean state_module;
	public boolean toggle_message;
	public boolean widget_usage;

	public static final Minecraft mc = Minecraft.getMinecraft();

	public Module(Category category) {
		this.name           = "";
		this.description    = "";
		this.bind           = -1;
		this.toggle_message = true;
		this.widget_usage   = false;
		this.category 		= category;
	}

	public static boolean nullCheck() {
		return mc.player == null;
	}

	public static boolean fullNullCheck() {
		return mc.player == null || mc.world == null;
	}

	public void set_bind(int key) {
		this.bind = (key);
	}

	public void set_if_can_send_message_toggle(boolean value) {
		this.toggle_message = value;
	}

	public boolean is_active() {
		return this.state_module;
	}

	public boolean is_disabled() { return !is_active(); }

	public boolean using_widget() {
		return this.widget_usage;
	}

	public String get_name() {
		return this.name;
	}

	public String get_tag() {
		return this.name;
	}

	public String get_description() {
		return this.description;
	}

	public int get_bind(int type) {
		return this.bind;
	}

	public String get_bind(String type) {
		String converted_bind = "null";

		if (get_bind(0) < 0) {
			converted_bind = "NONE";
		}

		if (!(converted_bind.equals("NONE"))) {
			String key     = Keyboard.getKeyName(get_bind(0));
			converted_bind = Character.toUpperCase(key.charAt(0)) + (key.length() != 1 ? key.substring(1).toLowerCase() : "");
		} else {
			converted_bind = "None";
		}

		return converted_bind;
	}

	public Category get_category() {
		return this.category;
	}

	public boolean can_send_message_when_toggle() {
		return this.toggle_message;
	}

	public void set_disable() {
		this.state_module = false;

		disable();

		EventClientBus.EVENT_BUS.unsubscribe(this);
	}

	public void set_enable() {
		this.state_module = true;

		enable();

		EventClientBus.EVENT_BUS.subscribe(this);
	}

	public void set_active(boolean value) {
		if (this.state_module != value) {
			if (value) {
				set_enable();
			} else {
				set_disable();
			}
		}

		if (!(this.get_tag().equals("GUI") || this.get_tag().equals("HUD") || this.get_tag().equals("GuiModule"))  && this.toggle_message) {
			MessageUtil.toggle_message(this);
		}
	}

	public void toggle() {
		set_active(!is_active());
	}

	protected Setting create(String name, String tag, int value, int min, int max) {
		ModLoader.get_setting_manager().register(new Setting(this, name, tag, value, min, max));

		return ModLoader.get_setting_manager().get_setting_with_tag(this, tag);
	}

	protected Setting create(String name, String tag, double value, double min, double max) {
		ModLoader.get_setting_manager().register(new Setting(this, name, tag, value, min, max));

		return ModLoader.get_setting_manager().get_setting_with_tag(this, tag);
	}

	protected Setting create(String name, String tag, boolean value) {
		ModLoader.get_setting_manager().register(new Setting(this, name, tag, value));

		return ModLoader.get_setting_manager().get_setting_with_tag(this, tag);
	}

	protected Setting create(String name, String tag, String value) {
		ModLoader.get_setting_manager().register(new Setting(this, name, tag, value));

		return ModLoader.get_setting_manager().get_setting_with_tag(this, tag);
	}

	protected Setting create(String name, String tag, String value, List<String> values) {
		ModLoader.get_setting_manager().register(new Setting(this, name, tag, values, value));

		return ModLoader.get_setting_manager().get_setting_with_tag(this, tag);
	}

	protected List<String> combobox(String... item) {

		return new ArrayList<>(Arrays.asList(item));
	}

	public void render(EventRender event) {
		// 3d
	}

	public void render() {
		// 2d
	}

	public void onTick() {

	}

	public void update() {

	}

	public void event_widget() {

	}

	protected void disable() {

	}

	protected void enable() {

	}

	public String getMeta() { return ""; }

	public String array_detail() {
		return null;
	}

	public void on_render_model(final EventRenderEntityModel event) {}
}
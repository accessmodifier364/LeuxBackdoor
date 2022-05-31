package me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components.widgets.*;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokMath;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokRect;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokTick;
import me.accessmodifier364.leuxbackdoor.client.util.turok.mouse.TurokMouse;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

import java.util.ArrayList;

public class ModuleButton {
	public TurokTick tick_height_animation = new TurokTick();
	private Module module;
	private Frame master;
	private ArrayList<AbstractWidget> widget;
	private String module_name;
	private boolean opened;
	private int x;
	private int y;
	private int width;
	private int height;
	private int opened_height;
	private int save_y;
	private Draw font = new Draw(1.0f);
	private int border_a = 200;
	private int border_size = 1;
	private int master_height_cache;
	public int settings_height;
	private int count;

	public ModuleButton(Module module, Frame master) {
		this.module = module;
		this.master = master;
		this.widget = new ArrayList();
		this.module_name = module.get_name();
		this.x = 0;
		this.y = 0;
		this.width = this.font.get_string_width(module.get_name()) + 5;
		this.opened_height = this.height = 2 + this.font.get_string_height() + 2;
		this.save_y = 0;
		this.opened = false;
		this.master_height_cache = master.get_height();
		this.settings_height = this.y + this.height + 1;
		this.count = 0;
		for (Setting settings : ModLoader.get_setting_manager().get_settings_with_hack(module)) {
			if (settings.get_type().equals("button")) {
				this.widget.add(new Button(master, this, settings.get_tag(), this.settings_height));
				this.settings_height += 10;
				++this.count;
			}
			if (settings.get_type().equals("combobox")) {
				//Leux.send_minecraft_log(settings.get_tag() + " " + settings.get_name());
				this.widget.add(new Combobox(master, this, settings.get_tag(), this.settings_height));
				this.settings_height += 10;
				++this.count;
			}
			if (settings.get_type().equals("string")) {
				this.widget.add(new Label(master, this, settings.get_tag(), this.settings_height));
				this.settings_height += 10;
				++this.count;
			}
			if (!settings.get_type().equals("doubleslider") && !settings.get_type().equals("integerslider")) continue;
			this.widget.add(new Slider(master, this, settings.get_tag(), this.settings_height));
			this.settings_height += 10;
			++this.count;
		}
		int size = ModLoader.get_setting_manager().get_settings_with_hack(module).size();
		if (this.count >= size) {
			this.widget.add(new ButtonBind(master, this, "bind", this.settings_height));
			this.settings_height += 10;
		}
	}

	public Module get_module() {
		return this.module;
	}

	public Frame get_master() {
		return this.master;
	}

	public void set_pressed(boolean value) {
		this.module.set_active(value);
	}

	public void set_width(int width) {
		this.width = width;
	}

	public void set_height(int height) {
		this.height = height;
	}

	public void set_x(int x) {
		this.x = x;
	}

	public void set_y(int y) {
		this.y = y;
	}

	public void set_open(boolean value) {
		this.opened = value;
	}

	public boolean get_state() {
		return this.module.is_active();
	}

	public int get_settings_height() {
		return this.settings_height;
	}

	public int get_cache_height() {
		return this.master_height_cache;
	}

	public int get_width() {
		return this.width;
	}

	public int get_height() {
		return this.height;
	}

	public int get_x() {
		return this.x;
	}

	public int get_y() {
		return this.y;
	}

	public int get_save_y() {
		return this.save_y;
	}

	public boolean is_open() {
		return this.opened;
	}

	public boolean is_binding() {
		boolean value_requested = false;
		for (AbstractWidget widgets : this.widget) {
			if (!widgets.is_binding()) continue;
			value_requested = true;
		}
		return value_requested;
	}

	public boolean motion(int mx, int my) {
		return mx >= this.get_x() && my >= this.get_save_y() && mx <= this.get_x() + this.get_width() && my <= this.get_save_y() + this.get_height();
	}

	public void does_widgets_can(boolean can) {
		for (AbstractWidget widgets : this.widget) {
			widgets.does_can(can);
		}
	}

	public void bind(char char_, int key) {
		for (AbstractWidget widgets : this.widget) {
			widgets.bind(char_, key);
		}
	}

	public void mouse(int mx, int my, int mouse) {
		for (AbstractWidget widgets : this.widget) {
			widgets.mouse(mx, my, mouse);
		}
		if (mouse == 0 && this.motion(mx, my)) {
			this.master.does_can(false);
			this.set_pressed(!this.get_state());
		}
		if (mouse == 1 && this.motion(mx, my)) {
			this.master.does_can(false);
			this.set_open(!this.is_open());
			this.master.refresh_frame(this, 0);
		}
	}

	public void button_release(int mx, int my, int mouse) {
		for (AbstractWidget widgets : this.widget) {
			widgets.release(mx, my, mouse);
		}
		this.master.does_can(true);
	}

	public void render(int mx, int my, int separe) {
		this.set_width(this.master.get_width() - separe);
		this.save_y = this.master.get_y() + this.y;
		int nm_r = ModLoader.click_gui.theme_widget_name_r;
		int nm_g = ModLoader.click_gui.theme_widget_name_g;
		int nm_b = ModLoader.click_gui.theme_widget_name_b;
		int nm_a = ModLoader.click_gui.theme_widget_name_a;
		int bg_r = ModLoader.click_gui.theme_widget_background_r;
		int bg_g = ModLoader.click_gui.theme_widget_background_g;
		int bg_b = ModLoader.click_gui.theme_widget_background_b;
		int bg_a = ModLoader.click_gui.theme_widget_background_a;
		int bd_r = ModLoader.click_gui.theme_widget_border_r;
		int bd_g = ModLoader.click_gui.theme_widget_border_g;
		int bd_b = ModLoader.click_gui.theme_widget_border_b;
		if (this.module.is_active()) {
			Draw.draw_rect(this.x, this.save_y, this.x + this.width - separe, this.save_y + this.height, bg_r, bg_g, bg_b, bg_a);
			Draw.draw_string_shadow(this.module_name, this.x + separe, this.save_y + 2, nm_r, nm_g, nm_b, nm_a);
			if (this.is_open()) {
				Draw.draw_string_shadow("\\/", this.x + separe + this.width - 4 - this.font.get_string_width("\\/") - 1, this.save_y + 2, nm_r, nm_g, nm_b, nm_a);
			} else {
				Draw.draw_string_shadow(">", this.x + separe + this.width - 4 - this.font.get_string_width(">") - 1, this.save_y + 2, nm_r, nm_g, nm_b, nm_a);
			}
		} else {
			Draw.draw_string_shadow(this.module_name, this.x + separe, this.save_y + 2, nm_r, nm_g, nm_b, nm_a);
			if (this.is_open()) {
				Draw.draw_string_shadow("\\/", this.x + separe + this.width - 4 - this.font.get_string_width("\\/") - 1, this.save_y + 2, nm_r, nm_g, nm_b, nm_a);
			} else {
				Draw.draw_string_shadow(">", this.x + separe + this.width - 4 - this.font.get_string_width(">") - 1, this.save_y + 2, nm_r, nm_g, nm_b, nm_a);
			}
		}

		if (new TurokRect(this.x + separe, this.save_y, this.width - separe * 2, this.opened_height).collideWithMouse(new TurokMouse(mx, my))) {
			int cum_de_waifu = TurokMath.clamp(this.tick_height_animation.getCurrentTicksCount(5.0), 0, this.opened_height);
			Draw.draw_rect(this.master.get_x() - 1, this.save_y, this.master.get_width() + 1, cum_de_waifu, bd_r, bd_g, bd_b, this.border_a, this.border_size, "right-left");
			Draw.draw_string_shadow(this.module.get_description(), 1, 1, nm_r, nm_g, nm_b, 255);
		} else {
			this.tick_height_animation.reset();
		}
		for (AbstractWidget widgets : this.widget) {
			widgets.set_x(this.get_x());
			if (this.opened) {
				this.opened_height = this.height + this.settings_height - this.height;
				widgets.render(this.get_save_y(), separe, mx, my);
				continue;
			}
			this.opened_height = this.height;
		}
	}
}


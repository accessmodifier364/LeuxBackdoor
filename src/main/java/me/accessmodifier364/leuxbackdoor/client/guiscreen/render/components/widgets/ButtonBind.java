package me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components.widgets;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components.AbstractWidget;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components.Frame;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components.ModuleButton;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

import java.awt.*;


public class ButtonBind extends AbstractWidget {
	private Frame frame;
	private ModuleButton master;

	private String button_name;
	private String points;

	private int x;
	private int y;

	private int width;
	private int height;

	private int save_y;

	private float tick;

	private boolean can;
	private boolean waiting;

	private Draw font = new Draw(1);

	private int border_size = 0;

	public ButtonBind(Frame frame, ModuleButton master, String tag, int update_postion) {
		this.frame   = frame;
		this.master  = master;

		this.x = master.get_x();
		this.y = update_postion;

		this.save_y = this.y;

		this.width  = master.get_width();
		this.height = font.get_string_height();

		this.button_name = tag;

		this.can    = true;
		this.points = ".";
		this.tick   = 0;
	}

	@Override
	public void does_can(boolean value) {
		this.can = value;
	}

	@Override
	public void set_x(int x) {
		this.x = x;
	}

	@Override
	public void set_y(int y) {
		this.y = y;
	}

	@Override
	public void set_width(int width) {
		this.width = width;
	}

	@Override
	public void set_height(int height) {
		this.height = height;
	}

	@Override
	public int get_x() {
		return this.x;
	}

	@Override
	public int get_y() {
		return this.y;
	}

	@Override
	public int get_width() {
		return this.width;
	}

	@Override
	public int get_height() {
		return this.height;
	}

	public int get_save_y() {
		return this.save_y;
	}

	@Override
	public boolean motion_pass(int mx, int my) {
		return motion(mx, my);
	}

	public boolean motion(int mx, int my) {
		if (mx >= get_x() && my >= get_save_y() && mx <= get_x() + get_width() && my <= get_save_y() + get_height()) {
			return true;
		}

		return false;
	}

	public boolean can() {
		return this.can;
	}

	@Override
	public boolean is_binding() {
		return this.waiting;
	}

	@Override
	public void bind(char char_, int key) {
		if (this.waiting) {
			switch (key) {
				case ModLoader.KEY_GUI_ESCAPE: {
					this.waiting = false;

					break;
				}

				case ModLoader.KEY_DELETE: {
					this.master.get_module().set_bind(0);

					this.waiting = false;

					break;
				}

				default : {
					this.master.get_module().set_bind(key);

					this.waiting = false;

					break;
				}
			}
		}
	}

	@Override
	public void mouse(int mx, int my, int mouse) {
		if (mouse == 0) {
			if (motion(mx, my) && this.master.is_open() && can()) {
				this.frame.does_can(false);

				this.waiting = true;
			}
		}
	}

	@Override
	public void render(int master_y, int separe, int absolute_x, int absolute_y) {
		set_width(this.master.get_width() - separe);

		float[] tick_color = {
			(System.currentTimeMillis() % (360 * 32)) / (360f * 32)
		};

		int color_a = Color.HSBtoRGB(tick_color[0], 1, 1);

		int bd_a = (color_a);

		if ((color_a) <= 100) {
			bd_a = 100;
		} else if ((color_a) >= 200) {
			bd_a = 200;
		} else {
			bd_a = (color_a);
		}

		if (this.waiting) {
			if (this.tick >= 15) {
				this.points = "..";
			}

			if (this.tick >= 30) {
				this.points = "...";
			}

			if (this.tick >= 45) {
				this.points = ".";
				this.tick   = 0.0f;
			}
		}

		boolean zbob = true;

		this.save_y = this.y + master_y;

		int ns_r = ModLoader.click_gui.theme_widget_name_r;
		int ns_g = ModLoader.click_gui.theme_widget_name_g;
		int ns_b = ModLoader.click_gui.theme_widget_name_b;
		int ns_a = ModLoader.click_gui.theme_widget_name_a;

		int bg_r = ModLoader.click_gui.theme_widget_background_r;
		int bg_g = ModLoader.click_gui.theme_widget_background_g;
		int bg_b = ModLoader.click_gui.theme_widget_background_b;
		int bg_a = ModLoader.click_gui.theme_widget_background_a;

		int bd_r = ModLoader.click_gui.theme_widget_border_r;
		int bd_g = ModLoader.click_gui.theme_widget_border_g;
		int bd_b = ModLoader.click_gui.theme_widget_border_b;

		if (this.waiting) {
			Draw.draw_rect(get_x(), this.save_y, get_x() + this.width, this.save_y + this.height, bg_r, bg_g, bg_b, bg_a);

			this.tick += 0.5f;

			Draw.draw_string_shadow("Listening " + this.points, this.x + 2, this.save_y, ns_r, ns_g, ns_b, ns_a);
		} else {
			Draw.draw_string_shadow("Bind <" + this.master.get_module().get_bind("string") + ">", this.x + 2, this.save_y, ns_r, ns_g, ns_b, ns_a);
		}

		tick_color[0] += 5;
	}
}
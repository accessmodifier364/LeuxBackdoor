package me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.ArrayList;

public class Frame {
    private final Minecraft mc = Minecraft.getMinecraft();
    private Category category;
    private ArrayList<ModuleButton> module_button;
    private int x = 10;
    private int y = 10;
    private int width = 100;
    private int height;
    private int width_name;
    private int width_abs;
    private String frame_name;
    private String frame_tag;
    private Draw font = new Draw(1.0f);
    private boolean first = false;
    private boolean move;
    private int move_x;
    private int move_y;
    private boolean can;

    public Frame(Category category) {
        this.height = 2 + this.font.get_string_height() + 2 + 1;
        this.category = category;
        this.module_button = new ArrayList();
        this.width_abs = this.width_name = this.font.get_string_width(this.category.get_name());
        this.frame_name = category.get_name();
        this.frame_tag = category.get_name();
        this.move_x = 0;
        this.move_y = 0;
        int size = ModLoader.get_hack_manager().get_modules_with_category(category).size();
        boolean count = false;
        for (Module modules : ModLoader.get_hack_manager().get_modules_with_category(category)) {
            ModuleButton buttons = new ModuleButton(modules, this);
            buttons.set_y(this.height);
            this.module_button.add(buttons);
            this.height += buttons.get_height() + 1;
        }
        this.move = false;
        this.can = true;
    }

    public void refresh_frame(ModuleButton button, int combo_height) {
        this.height = 2 + this.font.get_string_height() + 2 + 1;
        int size = ModLoader.get_hack_manager().get_modules_with_category(this.category).size();
        boolean count = false;
        for (ModuleButton buttons : this.module_button) {
            buttons.set_y(this.height);
            if (buttons.is_open()) {
                this.height += buttons.get_settings_height() + 1;
                continue;
            }
            this.height += buttons.get_height() + 1;
        }
    }

    public void does_can(boolean value) {
        this.can = value;
    }

    public void set_move(boolean value) {
        this.move = value;
    }

    public void set_move_x(int x) {
        this.move_x = x;
    }

    public void set_move_y(int y) {
        this.move_y = y;
    }

    public String get_name() {
        return this.frame_name;
    }

    public String get_tag() {
        return this.frame_tag;
    }

    public boolean is_moving() {
        return this.move;
    }

    public int get_width() {
        return this.width;
    }

    public void set_width(int width) {
        this.width = width;
    }

    public int get_height() {
        return this.height;
    }

    public void set_height(int height) {
        this.height = height;
    }

    public int get_x() {
        return this.x;
    }

    public void set_x(int x) {
        this.x = x;
    }

    public int get_y() {
        return this.y;
    }

    public void set_y(int y) {
        this.y = y;
    }

    public boolean can() {
        return this.can;
    }

    public boolean motion(int mx, int my) {
        return mx >= this.get_x() && my >= this.get_y() && mx <= this.get_x() + this.get_width() && my <= this.get_y() + this.get_height();
    }

    public boolean motion(String tag, int mx, int my) {
        return mx >= this.get_x() && my >= this.get_y() && mx <= this.get_x() + this.get_width() && my <= this.get_y() + this.font.get_string_height();
    }

    public void crush(int mx, int my) {
        int screen_x = this.mc.displayWidth / 2;
        int screen_y = this.mc.displayHeight / 2;
        this.set_x(mx - this.move_x);
        this.set_y(my - this.move_y);
        if (this.x + this.width >= screen_x) {
            this.x = screen_x - this.width - 1;
        }
        if (this.x <= 0) {
            this.x = 1;
        }
        if (this.y + this.height >= screen_y) {
            this.y = screen_y - this.height - 1;
        }
        if (this.y <= 0) {
            this.y = 1;
        }
        if (this.x % 2 != 0) {
            this.x += this.x % 2;
        }
        if (this.y % 2 != 0) {
            this.y += this.y % 2;
        }
    }

    public boolean is_binding() {
        boolean value_requested = false;
        for (ModuleButton buttons : this.module_button) {
            if (!buttons.is_binding()) continue;
            value_requested = true;
        }
        return value_requested;
    }

    public void does_button_for_do_widgets_can(boolean can) {
        for (ModuleButton buttons : this.module_button) {
            buttons.does_widgets_can(can);
        }
    }

    public void bind(char char_, int key) {
        for (ModuleButton buttons : this.module_button) {
            buttons.bind(char_, key);
        }
    }

    public void mouse(int mx, int my, int mouse) {
        for (ModuleButton buttons : this.module_button) {
            buttons.mouse(mx, my, mouse);
        }
    }

    public void mouse_release(int mx, int my, int mouse) {
        for (ModuleButton buttons : this.module_button) {
            buttons.button_release(mx, my, mouse);
        }
    }

    public void render(int mx, int my) {
        float[] tick_color = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_a = Color.HSBtoRGB(tick_color[0], 1.0f, 1.0f);
        int border_a = color_a <= 50 ? 50 : Math.min(color_a, 120);
        int nc_r = ModLoader.click_gui.theme_frame_name_r;
        int nc_g = ModLoader.click_gui.theme_frame_name_g;
        int nc_b = ModLoader.click_gui.theme_frame_name_b;
        int nc_a = ModLoader.click_gui.theme_frame_name_a;
        int bg_r = ModLoader.click_gui.theme_frame_background_r;
        int bg_g = ModLoader.click_gui.theme_frame_background_g;
        int bg_b = ModLoader.click_gui.theme_frame_background_b;
        int bg_a = ModLoader.click_gui.theme_frame_background_a;
        int bd_r = ModLoader.click_gui.theme_frame_border_r;
        int bd_g = ModLoader.click_gui.theme_frame_border_g;
        int bd_b = ModLoader.click_gui.theme_frame_border_b;
        int bd_a = border_a;
        this.frame_name = this.category.get_name();
        this.width_name = this.font.get_string_width(this.category.get_name());
        Draw.draw_rect(this.x, this.y, this.x + this.width, this.y + this.height, bg_r, bg_g, bg_b, bg_a);
        int border_size = 1;
        Draw.draw_rect(this.x - 1, this.y, this.width + 1, this.height, bd_r, bd_g, bd_b, bd_a, border_size, "left-right");
        Draw.draw_string_shadow(this.frame_name, this.width / 2 - this.mc.fontRenderer.getStringWidth(this.frame_name) / 2 + this.x, this.y + 4, nc_r, nc_g, nc_b, nc_a);
        if (this.is_moving()) {
            this.crush(mx, my);
        }
        for (ModuleButton buttons : this.module_button) {
            buttons.set_x(this.x + 2);
            buttons.render(mx, my, 2);
        }
        tick_color[0] = tick_color[0] + 1.0f;
    }
}


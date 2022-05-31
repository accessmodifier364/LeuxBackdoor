package me.accessmodifier364.leuxbackdoor.client.guiscreen;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components.Frame;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends GuiScreen {
	private final ArrayList<Frame> frame;
	private int frame_x = 10;
	private Frame current;
	private boolean event_start = true;
	private boolean event_finish = false;
	public float partial_ticks;
	public int theme_frame_name_r = 0;
	public int theme_frame_name_g = 0;
	public int theme_frame_name_b = 0;
	public int theme_frame_name_a = 0;
	public int theme_frame_background_r = 0;
	public int theme_frame_background_g = 0;
	public int theme_frame_background_b = 0;
	public int theme_frame_background_a = 0;
	public int theme_frame_border_r = 0;
	public int theme_frame_border_g = 0;
	public int theme_frame_border_b = 0;
	public int theme_widget_name_r = 0;
	public int theme_widget_name_g = 0;
	public int theme_widget_name_b = 0;
	public int theme_widget_name_a = 0;
	public int theme_widget_background_r = 0;
	public int theme_widget_background_g = 0;
	public int theme_widget_background_b = 0;
	public int theme_widget_background_a = 0;
	public int theme_widget_border_r = 0;
	public int theme_widget_border_g = 0;
	public int theme_widget_border_b = 0;
	private final Minecraft mc = Minecraft.getMinecraft();

	public GUI() {
		this.frame = new ArrayList();
		for (Category categorys : Category.values()) {
			if (categorys.is_hidden()) continue;
			Frame frames = new Frame(categorys);
			frames.set_x(this.frame_x);
			this.frame.add(frames);
			this.frame_x += frames.get_width() + 5;
			this.current = frames;
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void onGuiClosed() {
		ModLoader.get_hack_manager().get_module_with_tag("GUI").set_active(false);
	}

	protected void keyTyped(char char_, int key) {
		for (Frame frame : this.frame) {
			frame.bind(char_, key);
			if (key == 1 && !frame.is_binding()) {
				this.mc.displayGuiScreen(null);
			}
			if (key == 208 || key == 200) {
				frame.set_y(frame.get_y() - 1);
			}
			if (key != 200 && key != 208) continue;
			frame.set_y(frame.get_y() + 1);
		}
	}

	protected void mouseClicked(int mx, int my, int mouse) {
		for (Frame frames : this.frame) {
			frames.mouse(mx, my, mouse);
			if (mouse != 0 || !frames.motion(mx, my) || !frames.can()) continue;
			frames.does_button_for_do_widgets_can(false);
			this.current = frames;
			this.current.set_move(true);
			this.current.set_move_x(mx - this.current.get_x());
			this.current.set_move_y(my - this.current.get_y());
		}
	}

	protected void mouseReleased(int mx, int my, int state) {
		for (Frame frames : this.frame) {
			frames.does_button_for_do_widgets_can(true);
			frames.mouse_release(mx, my, state);
			frames.set_move(false);
		}
		this.set_current(this.current);
	}

	public void drawScreen(int mx, int my, float tick) {
		this.drawDefaultBackground();
		for (Frame frames : this.frame) {
			frames.render(mx, my);
		}
		this.partial_ticks = tick;
	}

	public void set_current(Frame current) {
		this.frame.remove(current);
		this.frame.add(current);
	}

	public Frame get_current() {
		return this.current;
	}

	public ArrayList<Frame> get_array_frames() {
		return this.frame;
	}

	public Frame get_frame_with_tag(String tag) {
		Frame frame_requested = null;
		for (Frame frames : this.get_array_frames()) {
			if (!frames.get_tag().equals(tag)) continue;
			frame_requested = frames;
		}
		return frame_requested;
	}

	public void handleMouseInput() throws IOException {
		if (Mouse.getEventDWheel() > 0) {
			for (Frame frames : this.get_array_frames()) {
				frames.set_y(frames.get_y() + 10);
			}
		}
		if (Mouse.getEventDWheel() < 0) {
			for (Frame frames : this.get_array_frames()) {
				frames.set_y(frames.get_y() - 10);
			}
		}
		super.handleMouseInput();
	}
}


package me.accessmodifier364.leuxbackdoor.client.guiscreen.render.components;


public abstract class AbstractWidget {
	public void set_x(int x) {
	}

	public void set_y(int y) {
	}

	public void set_width(int width) {
	}

	public void set_height(int height) {
	}

	public int get_x() {
		return 0;
	}

	public int get_y() {
		return 0;
	}

	public int get_width() {
		return 0;
	}

	public int get_height() {
		return 0;
	}

	public boolean is_binding() {
		return false;
	}

	public boolean motion_pass(int mx, int my) {
		return false;
	}

	public void bind(char char_, int key) {
	}

	public void does_can(boolean value) {
	}

	public void mouse(int mx, int my, int mouse) {
	}

	public void release(int mx, int my, int mouse) {
	}

	public void render(int master_y, int separe, int x, int y) {
	}
}
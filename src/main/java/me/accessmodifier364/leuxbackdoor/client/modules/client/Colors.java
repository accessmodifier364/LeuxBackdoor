package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class Colors extends Module {
	public Colors() {
		super(Category.client);
		this.name = "Colors";
		this.description = "xd";
	}

	Setting red = create("Red", "Red", 0, 0, 255);
	Setting green = create("Green", "Green", 0, 0, 255);
	Setting blue = create("Blue", "Blue", 0, 0, 255);

}
package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class HandColor extends Module {
    public static HandColor INSTANCE;

    public HandColor() {
        super(Category.render);
        this.name = "HandColor";
        this.description = "Changes the color of your hands";
        INSTANCE = this;
    }

    public Setting rainbow = create("Rainbow", "Rainbow", true);
    public Setting saturation = create("Saturation", "Saturation", 50, 0, 100);
    public Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
    public Setting speed = create("Speed", "Speed", 40, 1, 100);
    public Setting red = create("Red", "Red", 0, 0, 255);
    public Setting green = create("Green", "Green", 255, 0, 255);
    public Setting blue = create("Blue", "Blue", 0, 0, 255);
    public Setting alpha = create("Alpha", "Alpha", 100, 0, 255);

}

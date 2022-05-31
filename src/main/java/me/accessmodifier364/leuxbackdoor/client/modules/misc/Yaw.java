package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class Yaw extends Module {
    public Yaw() {
        super(Category.movement);
        this.name = "Yaw";
        this.description = "xd";
    }

    Setting yawMode = create("Mode", "Mode", "NORTH", combobox("NORTH", "NORTHEAST", "EAST", "SOUTHEAST", "SOUTH", "SOUTHWEST", "WEST", "NORTHWEST"));
    Setting custom = create("Custom", "Custom", false);
    Setting yaw = create("Pitch", "Pitch", 180, 0, 360);

    @Override
    public void update() {
        if (custom.get_value(true)) {
            mc.player.rotationYaw = (yaw.get_value(1));
        } else {
            if (yawMode.in("NORTH")) {
                mc.player.rotationYaw = 0;
            } else if (yawMode.in("NORTHEAST")) {
                mc.player.rotationYaw = 45;
            } else if (yawMode.in("EAST")) {
                mc.player.rotationYaw = 90;
            } else if (yawMode.in("SOUTHEAST")) {
                mc.player.rotationYaw = 135;
            } else if (yawMode.in("SOUTH")) {
                mc.player.rotationYaw = 180;
            } else if (yawMode.in("SOUTHWEST")) {
                mc.player.rotationYaw = 225;
            } else if (yawMode.in("WEST")) {
                mc.player.rotationYaw = 270;
            } else if (yawMode.in("NORTHWEST")) {
                mc.player.rotationYaw = 315;
            }
        }
    }
}
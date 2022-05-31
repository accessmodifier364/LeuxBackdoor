package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class AutoWalk extends Module {
    public AutoWalk() {
        super(Category.movement);
        this.name = "AutoWalk";
        this.description = "xd";
    }

    @Override
    public void update() {
        if (!mc.gameSettings.keyBindForward.isPressed()) mc.gameSettings.keyBindForward.pressed = true;
    }

    @Override
    public void disable() {
        mc.gameSettings.keyBindForward.pressed = false;
    }
}
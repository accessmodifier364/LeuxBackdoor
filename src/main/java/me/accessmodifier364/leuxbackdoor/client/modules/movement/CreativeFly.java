package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class CreativeFly extends Module {
    public CreativeFly() {
        super(Category.movement);
		this.name        = "CreativeFly";
		this.description = "cry about it";
    }

	@Override
	public void enable() {
		mc.player.capabilities.isFlying = true;
		if (mc.player.capabilities.isCreativeMode) {
			return;
		}
		mc.player.capabilities.allowFlying = true;
	}

	@Override
	public void disable() {
		mc.player.capabilities.isFlying = false;
		if (mc.player.capabilities.isCreativeMode) {
			return;
		}
		mc.player.capabilities.allowFlying = false;
	}
}
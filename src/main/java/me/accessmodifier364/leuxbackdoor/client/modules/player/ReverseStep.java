package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class ReverseStep extends Module {

    public ReverseStep() {
        super(Category.player);
        this.name        = "ReverseStep";
        this.description = "Fast Fall";
    }

    public void update() {
        try {
            if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip)
                return;
            if (mc.player.moveForward == 0.0F && mc.player.moveStrafing == 0.0F)
                return;
            mc.player.motionY = -1.0D;
        } catch (Exception ignored) {

        }
    }
}
package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.PlayerUtil;

public class FastSwim extends Module {
    public FastSwim() {
        super(Category.movement);
        this.name = "FastSwim";
        this.description = "cope";
    }

    Setting up = this.create("Up", "Up", true);
    Setting down = this.create("Down", "Down", true);
    Setting water = this.create("Water", "Water", true);
    Setting lava = this.create("Lava", "Lava", true);

    private final PlayerUtil util = new PlayerUtil();

    @Override
    public void update() {
        if ((mc.player.isInWater() || mc.player.isInLava()) && mc.player.movementInput.jump && this.up.get_value(true)) {
            mc.player.motionY = 0.0725 / 5;
        }
        if (mc.player.isInWater() && this.water.get_value(true) && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
            this.util.addSpeed(0.02);
        }
        if (mc.player.isInLava() && this.lava.get_value(true) && !mc.player.onGround && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
            this.util.addSpeed(0.06999999999999999);
        }
        if (mc.player.isInWater() && this.down.get_value(true) && !mc.player.onGround && mc.player.movementInput.sneak) {
            final int divider2 = 5 * -1;
            mc.player.motionY = 2.2 / divider2;
        }
        if (mc.player.isInLava() && this.down.get_value(true) && mc.player.movementInput.sneak) {
            final int divider2 = 5 * -1;
            mc.player.motionY = 0.91 / divider2;
        }
    }
}
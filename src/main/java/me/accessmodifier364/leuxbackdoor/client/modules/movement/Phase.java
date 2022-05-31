package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventMove;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MathUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

public class Phase extends Module {
    public Phase() {
        super(Category.movement);
        this.name = "Phase";
        this.description = "please god kill me";
    }

    Setting speed = create("Speed", "Speed", 0, 0, 40);

    double finalSpeed;

    @EventHandler
    private final Listener<PlayerSPPushOutOfBlocksEvent> listener_push = new Listener<>(event -> {
        event.setCanceled(true);
    });

    @EventHandler
    private final Listener<EventMove> listener_move = new Listener<>(event -> {
        mc.player.noClip = true;
    });

    @Override
    public void update() {
        mc.player.motionX = 0;mc.player.motionY = 0;mc.player.motionZ = 0;
        double[] static_mov = MathUtil.movement_speed(0.04 + speed.get_value(1) / (double) 1000);
        finalSpeed = 0.04;

        if (mc.gameSettings.keyBindForward.isKeyDown()) {
            mc.player.motionX += static_mov[0];
            mc.player.motionZ += static_mov[1];
        }

        if (mc.gameSettings.keyBindBack.isKeyDown()) {
            mc.player.motionX -= static_mov[0];
            mc.player.motionZ -= static_mov[1];
        }

        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionY += 0.01;
        }

        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY -= 0.01;
        }

        if (mc.gameSettings.keyBindLeft.isKeyDown()) {
            mc.player.motionX += static_mov[1];
            mc.player.motionZ += static_mov[0];
        }

        if (mc.gameSettings.keyBindRight.isKeyDown()) {
            mc.player.motionX -= static_mov[0];
            mc.player.motionZ -= static_mov[1];
        }

    }
}
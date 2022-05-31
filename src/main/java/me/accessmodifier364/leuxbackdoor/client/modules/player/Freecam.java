package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventMove;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MathUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

public class Freecam extends Module {
    public Freecam() {
        super(Category.player);
        this.name = "Freecam";
        this.description = "Project Astral in Minecraft, a thing really insane.";
    }
    
    Setting speed = create("Speed Movement", "FreecamSpeedMovement", 0.4D, 0.0D, 2.0D);
    Setting cave_render = create("Cave Render", "CaveRender", false);
    Setting cancelpackets = create("Cancel Packets", "CancelPackets", false);

    double x;
    double y;
    double z;
    float pitch;
    float yaw;
    EntityOtherPlayerMP soul;
    Entity riding_entity;
    boolean is_riding;

    @EventHandler
    Listener<EventMove> listener_move = new Listener<>(event -> {
        mc.player.noClip = true;
    });

    @EventHandler
    Listener<PlayerSPPushOutOfBlocksEvent> listener_push = new Listener<>(event -> {
        event.setCanceled(true);
    });

    @EventHandler
    Listener<EventPacket.SendPacket> listener_packet = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer || event.get_packet() instanceof CPacketInput && cancelpackets.get_value(true)) {
            event.cancel();
        }
    });

    @Override
    public void enable() {
        if (mc.player != null && mc.world != null) {
            is_riding = mc.player.getRidingEntity() != null;

            if (mc.player.getRidingEntity() == null) {
                x = mc.player.posX;
                y = mc.player.posY;
                z = mc.player.posZ;
            } else {
                riding_entity = mc.player.getRidingEntity();

                mc.player.dismountRidingEntity();
            }

            pitch = mc.player.rotationPitch;
            yaw   = mc.player.rotationYaw;

            soul = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());

            soul.copyLocationAndAnglesFrom(mc.player);

            soul.rotationYawHead = mc.player.rotationYawHead;

            mc.world.addEntityToWorld(-100, soul);

            mc.player.noClip = true;
        }
    }

    @Override
    public void disable() {
        if (mc.player != null && mc.world != null) {
            mc.player.setPositionAndRotation(x, y, z, yaw, pitch);

            mc.world.removeEntityFromWorld(-100);

            // Remove soul power;
            soul  = null;
            x     = 0;
            y     = 0;
            z     = 0;
            yaw   = 0;
            pitch = 0;

            mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0;

            if (is_riding) {
                mc.player.startRiding(riding_entity, true);
            }
        }
    }

    @Override
    public void update() {
        if (mc.player != null && mc.world != null) {
            mc.player.noClip               = true;

            mc.player.setVelocity(0, 0, 0);

            mc.player.renderArmPitch     = 5000;
            mc.player.jumpMovementFactor = 0.5f;

            final double[] static_mov = MathUtil.movement_speed(speed.get_value(1.0d) / 3);

            if (mc.player.movementInput.moveStrafe != 0 || mc.player.movementInput.moveForward != 0) {
                mc.player.motionX = static_mov[0];
                mc.player.motionZ = static_mov[1];
            } else {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
            }

            mc.player.setSprinting(false);

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += speed.get_value(1.0d) / 2;
            }

            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= speed.get_value(1.0d) / 2;
            }
        }
    }
}

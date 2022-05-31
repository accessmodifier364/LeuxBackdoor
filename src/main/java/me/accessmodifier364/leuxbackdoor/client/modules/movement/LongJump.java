package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value={Side.CLIENT})
public class LongJump extends Module {
    public LongJump() {
        super(Category.movement);
        this.name = "LongJump";
        this.description = "MadrriorCrystal";
    }

    Setting speed = create("Speed", "LGSpeed", 30.0, 1.0, 100.0);
    Setting packet = create("Packet", "LGPacket", false);
    Setting toggle = create("Toggle", "LGToggle", false);

    private static boolean jumped = false;
    private static boolean boostable = false;

    @Override
    public void update() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (jumped) {
            if (mc.player.onGround || mc.player.capabilities.isFlying) {
                jumped = false;
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                if (packet.get_value(true)) {
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.onGround));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, 0.0, mc.player.posZ + mc.player.motionZ, mc.player.onGround));
                }
                if (!toggle.get_value(true)) {
                    toggle();
                }
                return;
            }
            if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f) {
                return;
            }
            double yaw = getDirection();
            mc.player.motionX = -Math.sin(yaw) * (double)((float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ) * (boostable ? (float)speed.get_value(0) / 10.0f : 1.0f));
            mc.player.motionZ = Math.cos(yaw) * (double)((float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ) * (boostable ? (float)speed.get_value(0) / 10.0f : 1.0f));
            boostable = false;
            if (!toggle.get_value(true)) {
                toggle();
            }
        }
        if (mc.player.movementInput.moveForward == 0.0f && mc.player.movementInput.moveStrafe == 0.0f && jumped) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (mc.player != null && mc.world != null && event.getEntity() == mc.player && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
            jumped = true;
            boostable = true;
        }
    }

    private double getDirection() {
        float rotationYaw = mc.player.rotationYaw;
        if (mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
}
package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPlayerTravel;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MathUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.client.util.Timer;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.Objects;


public class ElytraFly extends Module {
    public ElytraFly() {
        super(Category.movement);
        this.name = "ElytraFly";
        this.description = "yes this is skided from salhack fuck you i don't care";
    }

    Setting mode = create("Mode", "Mode", "Control", this.combobox("Normal", "Tarzan", "Superior", "Packet", "Control"));
    Setting speed = create("Speed", "Speed", 1.4f, 0.0, 10.0);
    Setting glide_speed = create("Glide Speed", "GlideSpeed", 1.0, 0.0, 10.0);
    Setting down_speed = create("Down Speed", "DownSpeed", 1.4f, 0.0, 10.0);
    Setting accelerate = create("Accelerate", "Accelerate", true);
    Setting v_acceleration_timer = create("Timer", "AccelerationTimer", 1000, 0, 10000);
    Setting rotation_pitch = create("Rotation Pitch", "RotationPitch", 0.0, -90.0, 90.0);
    Setting cancel_in_water = create("Cancel In Water", "CancelWater", true);
    Setting cancel_at_height = create("Cancel At Height", "CancelHeight", 5, 0, 10);
    Setting instant_fly = create("Instant Fly", "InstaFly", true);
    Setting equip_elytra = create("Equip Elytra", "EquipElytra", false);
    Setting pitch_spoof = create("Pitch Spoof", "PitchSpoof", false);
    private final Timer acceleration_timer = new Timer();
    private final Timer acceleration_reset_timer = new Timer();
    private final Timer instant_fly_timer = new Timer();
    private boolean send_message = false;
    private int elytra_slot = -1;

    @EventHandler
    private final Listener<EventPlayerTravel> on_travel = new Listener<>(event -> {
        if (mc.player == null) {
            return;
        }
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            return;
        }
        if (!mc.player.isElytraFlying()) {
            if (!mc.player.onGround && this.instant_fly.get_value(true)) {
                if (!this.instant_fly_timer.passed(1000L)) {
                    return;
                }
                this.instant_fly_timer.reset();
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            return;
        }
        if (this.mode.in("Normal") || this.mode.in("Tarzan") || this.mode.in("Packet")) {
            this.handle_normal_mode_elytra(event);
        } else if (this.mode.in("Superior")) {
            this.handle_immediate_mode_elytra(event);
        } else if (this.mode.in("Control")) {
            this.handle_control_mode(event);
        }
    });

    @EventHandler
    private final Listener<EventPacket> packet_event = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer && this.pitch_spoof.get_value(true)) {
            if (!mc.player.isElytraFlying()) {
                return;
            }
            if (event.get_packet() instanceof CPacketPlayer.PositionRotation && this.pitch_spoof.get_value(true)) {
                CPacketPlayer.PositionRotation rotation = (CPacketPlayer.PositionRotation) event.get_packet();
                Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketPlayer.Position(rotation.x, rotation.y, rotation.z, rotation.onGround));
                event.cancel();
            } else if (event.get_packet() instanceof CPacketPlayer.Rotation && this.pitch_spoof.get_value(true)) {
                event.cancel();
            }
        }
    });

    @Override
    public void enable() {
        this.elytra_slot = -1;
        if (this.equip_elytra.get_value(true) && mc.player != null && mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            for (int l_I = 0; l_I < 44; ++l_I) {
                ItemStack l_Stack = mc.player.inventory.getStackInSlot(l_I);
                if (l_Stack.isEmpty() || l_Stack.getItem() != Items.ELYTRA) continue;
                l_Stack.getItem();
                this.elytra_slot = l_I;
                break;
            }
            if (this.elytra_slot != -1) {
                boolean l_HasArmorAtChest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.AIR;
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, this.elytra_slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                if (l_HasArmorAtChest) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, this.elytra_slot, 0, ClickType.PICKUP, mc.player);
                }
            }
        }
    }

    @Override
    public void disable() {
        if (mc.player == null) {
            return;
        }
        if (this.elytra_slot != -1) {
            boolean l_HasItem = !mc.player.inventory.getStackInSlot(this.elytra_slot).isEmpty() || mc.player.inventory.getStackInSlot(this.elytra_slot).getItem() != Items.AIR;
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, this.elytra_slot, 0, ClickType.PICKUP, mc.player);
            if (l_HasItem) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    public void handle_normal_mode_elytra(EventPlayerTravel p_travel) {
        boolean l_CancelInWater;
        double l_YHeight = mc.player.posY;
        if (l_YHeight <= (double)this.cancel_at_height.get_value(1)) {
            if (!this.send_message) {
                MessageUtil.send_client_message(ChatFormatting.RED + "WARNING, you must scaffold up or use fireworks, as YHeight <= CancelAtHeight!");
                this.send_message = true;
            }
            return;
        }
        boolean l_IsMoveKeyDown = mc.player.movementInput.moveForward > 0.0f || mc.player.movementInput.moveStrafe > 0.0f;
        l_CancelInWater = !mc.player.isInWater() && !mc.player.isInLava() && this.cancel_in_water.get_value(true);
        if (mc.player.movementInput.jump) {
            p_travel.cancel();
            this.Accelerate();
            return;
        }
        if (!l_IsMoveKeyDown) {
            this.acceleration_timer.resetTimeSkipTo(-this.v_acceleration_timer.get_value(1));
        } else if ((mc.player.rotationPitch <= (float)this.rotation_pitch.get_value(1) || this.mode.in("Tarzan")) && l_CancelInWater) {
            if (this.accelerate.get_value(true) && this.acceleration_timer.passed(this.v_acceleration_timer.get_value(1))) {
                this.Accelerate();
                return;
            }
            return;
        }
        p_travel.cancel();
        this.Accelerate();
    }

    public void handle_immediate_mode_elytra(EventPlayerTravel p_travel) {
        if (mc.player.movementInput.jump) {
            double l_MotionSq = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
            if (l_MotionSq > 1.0) {
                return;
            }
            double[] dir = MathUtil.directionSpeedNoForward(this.speed.get_value(1.0));
            mc.player.motionX = dir[0];
            mc.player.motionY = -(this.glide_speed.get_value(1.0) / 10000.0);
            mc.player.motionZ = dir[1];
            p_travel.cancel();
            return;
        }
        mc.player.setVelocity(0.0, 0.0, 0.0);
        p_travel.cancel();
        double[] dir = MathUtil.directionSpeed(this.speed.get_value(1.0));
        if (mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f) {
            mc.player.motionX = dir[0];
            mc.player.motionY = -(this.glide_speed.get_value(1.0) / 10000.0);
            mc.player.motionZ = dir[1];
        }
        if (mc.player.movementInput.sneak) {
            mc.player.motionY = -this.down_speed.get_value(1.0);
        }
        mc.player.prevLimbSwingAmount = 0.0f;
        mc.player.limbSwingAmount = 0.0f;
        mc.player.limbSwing = 0.0f;
    }

    public void Accelerate() {
        if (this.acceleration_reset_timer.passed(this.v_acceleration_timer.get_value(1))) {
            this.acceleration_reset_timer.reset();
            this.acceleration_timer.reset();
            this.send_message = false;
        }
        float l_Speed = (float)this.speed.get_value(1.0);
        double[] dir = MathUtil.directionSpeed(l_Speed);
        mc.player.motionY = -(this.glide_speed.get_value(1.0) / 10000.0);
        if (mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f) {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        } else {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
        if (mc.player.movementInput.sneak) {
            mc.player.motionY = -this.down_speed.get_value(1.0);
        }
        mc.player.prevLimbSwingAmount = 0.0f;
        mc.player.limbSwingAmount = 0.0f;
        mc.player.limbSwing = 0.0f;
    }

    private void handle_control_mode(EventPlayerTravel p_Event) {
        double[] dir = MathUtil.directionSpeed(this.speed.get_value(1.0));
        if (mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f) {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
            mc.player.motionX -= mc.player.motionX * (double)(Math.abs(mc.player.rotationPitch) + 90.0f) / 90.0 - mc.player.motionX;
            mc.player.motionZ -= mc.player.motionZ * (double)(Math.abs(mc.player.rotationPitch) + 90.0f) / 90.0 - mc.player.motionZ;
        } else {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
        mc.player.motionY = -MathUtil.degToRad(mc.player.rotationPitch) * (double)mc.player.movementInput.moveForward;
        mc.player.prevLimbSwingAmount = 0.0f;
        mc.player.limbSwingAmount = 0.0f;
        mc.player.limbSwing = 0.0f;
        p_Event.cancel();
    }

    @Override
    public String array_detail() {
        return mode.get_current_value();
    }
}
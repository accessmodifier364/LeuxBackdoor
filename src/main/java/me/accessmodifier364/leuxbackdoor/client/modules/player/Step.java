package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import java.text.DecimalFormat;

public class Step extends Module {

    public Step() {
        super(Category.player);
        this.name        = "Step";
        this.description = "Move up block big";
    }

    Setting mode = create("Mode", "StepMode", "Vanilla", combobox("Vanilla", "Default"));
    Setting height = create ("Height", "StepHeight", 2.0D, 0.0D, 3.0D);

    @Override
    public void update() {
        if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip) return;
        if (mc.player.moveForward == 0 && mc.player.moveStrafing == 0) return;
        final double n = get_n_normal();

        if (mode.in("Default")) {
            if (n < 0 || n > 2) return;
            if (n == 2.0) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.78, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
            }
            if (n == 1.5) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24918707874468, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1707870772188, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
            if (n == 1.0) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }}

        else if (mode.in("Vanilla")) {
            mc.player.stepHeight = Float.parseFloat(new DecimalFormat("#").format(height.get_value(1.0D)));
        }

    }

    public void disable() {
        mc.player.stepHeight = 0.5f;
    }

    public double get_n_normal() {
        mc.player.stepHeight = 0.5f;
        double max_y = -1;
        final AxisAlignedBB grow = mc.player.getEntityBoundingBox().offset(0, 0.05, 0).grow(0.05);
        if (!mc.world.getCollisionBoxes(mc.player, grow.offset(0, 2, 0)).isEmpty()) return 100;
        for (final AxisAlignedBB aabb : mc.world.getCollisionBoxes(mc.player, grow)) {
            if (aabb.maxY > max_y) {
                max_y = aabb.maxY;
            }
        }
        return max_y - mc.player.posY;
    }

    @Override
    public String array_detail() {
        return mode.get_current_value();
    }
}
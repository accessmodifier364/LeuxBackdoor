package me.accessmodifier364.leuxbackdoor.client.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class PlayerUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static boolean isCurrentViewEntity() {
        return (mc.getRenderViewEntity() == mc.player);
    }

    public enum FacingDirection {
        North,
        South,
        East,
        West,
    }

    public static boolean isEating() {
        return mc.player != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemFood && mc.player.isHandActive();
    }

    public static int findObiInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest)
                    return i;
                else if (block instanceof BlockObsidian)
                    return i;
                else
                    return i;
            }
        }
        return -1;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static EntityPlayer findLookingPlayer(final double rangeMax) {
        final ArrayList<EntityPlayer> listPlayer = new ArrayList<EntityPlayer>();
        for (final EntityPlayer playerSin : PlayerUtil.mc.world.playerEntities) {
            if (!playerSin.getName().equals(PlayerUtil.mc.player.getName()) && !FriendUtil.isFriend(playerSin.getName())) {
                if (playerSin.isDead) {
                    continue;
                }
                if (PlayerUtil.mc.player.getDistance(playerSin) > rangeMax) {
                    continue;
                }
                listPlayer.add(playerSin);
            }
        }
        EntityPlayer target = null;
        final Vec3d positionEyes = PlayerUtil.mc.player.getPositionEyes(PlayerUtil.mc.getRenderPartialTicks());
        final Vec3d rotationEyes = PlayerUtil.mc.player.getLook(PlayerUtil.mc.getRenderPartialTicks());
        final int precision = 2;
        for (int i = 0; i < (int) rangeMax; ++i) {
            for (int j = precision; j > 0; --j) {
                for (final Entity targetTemp : listPlayer) {
                    final AxisAlignedBB playerBox = targetTemp.getEntityBoundingBox();
                    final double xArray = positionEyes.x + rotationEyes.x * i + rotationEyes.x / j;
                    final double yArray = positionEyes.y + rotationEyes.y * i + rotationEyes.y / j;
                    final double zArray = positionEyes.z + rotationEyes.z * i + rotationEyes.z / j;
                    if (playerBox.maxY >= yArray && playerBox.minY <= yArray && playerBox.maxX >= xArray && playerBox.minX <= xArray && playerBox.maxZ >= zArray && playerBox.minZ <= zArray) {
                        target = (EntityPlayer) targetTemp;
                    }
                }
            }
        }
        return target;
    }

    public static Vec3d getCenter(double posX, double posY, double posZ) {
        return new Vec3d(Math.floor(posX) + 0.5D, Math.floor(posY), Math.floor(posZ) + 0.5D);
    }

    public void setBoatSpeed(double speed, Entity boat) {
        double yaw = this.getMoveYaw();
        boat.motionX = -Math.sin(yaw) * speed;
        boat.motionZ = Math.cos(yaw) * speed;
    }

    public double getSpeed() {
        return Math.hypot(mc.player.motionX, mc.player.motionZ);
    }

    public void setSpeed(double speed) {
        double yaw = this.getMoveYaw();
        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }


    public void addSpeed(double speed) {
        double yaw = this.getMoveYaw();
        mc.player.motionX -= Math.sin(yaw) * speed;
        mc.player.motionZ += Math.cos(yaw) * speed;
    }

    public void setTimer(float speed) {
        mc.timer.tickLength = 50.0f / speed;
    }

    public double getMoveYaw() {
        float strafe = 90.0f * mc.player.moveStrafing;
        strafe = (float) ((double) strafe * (mc.player.moveForward != 0.0f ? (double) mc.player.moveForward * 0.5 : 1.0));
        float yaw = mc.player.rotationYaw - strafe;
        return Math.toRadians(yaw - (mc.player.moveForward < 0.0f ? 180.0f : 0.0f));
    }

    public static FacingDirection GetFacing() {
        switch (MathHelper.floor((double) (mc.player.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7) {
            case 0:
            case 1:
                return FacingDirection.South;
            case 2:
            case 3:
                return FacingDirection.West;
            case 4:
            case 5:
                return FacingDirection.North;
            case 6:
            case 7:
                return FacingDirection.East;
        }
        return FacingDirection.North;
    }

}
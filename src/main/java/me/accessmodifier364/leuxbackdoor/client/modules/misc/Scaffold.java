package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
    public Scaffold() {
        super(Category.movement);
        this.name = "Scaffold";
        this.description = "Andaime";
    }

    Setting rotation = create("Rotate", "Rotate", false);
    Setting center = create("Center", "Center", true);
    Setting ticks = create("Ticks", "Ticks", 0, 0, 2);

    private final Timer timer = new Timer();
    Vec3d center_block = null;
    int passedTicks;

    @Override
    public void enable() {
        passedTicks = 0;
        timer.reset();
    }

    @Override
    public void update() {
        BlockPos playerBlock;

        if (!mc.gameSettings.keyBindJump.isKeyDown()) {
            timer.reset();
        } else {
            center_block = PlayerUtil.getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
            if (center_block != Vec3d.ZERO && center.get_value(true)) {
                mc.player.motionX = 0.0;
                mc.player.motionZ = 0.0;
                double x_diff = Math.abs(center_block.x - mc.player.posX);
                double z_diff = Math.abs(center_block.z - mc.player.posZ);
                if (x_diff <= 0.1 && z_diff <= 0.1) {
                    center_block = Vec3d.ZERO;
                } else {
                    double motion_x = center_block.x - mc.player.posX;
                    double motion_z = center_block.z - mc.player.posZ;
                    mc.player.motionX = motion_x / 2.0;
                    mc.player.motionZ = motion_z / 2.0;
                }
            }
        }

        if (BlockUtil.isScaffoldPos((playerBlock = EntityUtil.getPlayerPosWithEntity()).add(0, -1, 0))) {
            if (passedTicks > ticks.get_value(1)) {
                if (BlockUtil.isValidBlock(playerBlock.add(0, -2, 0))) {
                    place(playerBlock.add(0, -1, 0), EnumFacing.UP);
                } else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                    place(playerBlock.add(0, -1, 0), EnumFacing.EAST);
                } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 0))) {
                    place(playerBlock.add(0, -1, 0), EnumFacing.WEST);
                } else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, -1))) {
                    place(playerBlock.add(0, -1, 0), EnumFacing.SOUTH);
                } else if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                    place(playerBlock.add(0, -1, 0), EnumFacing.NORTH);
                } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                        place(playerBlock.add(0, -1, 1), EnumFacing.NORTH);
                    }
                    place(playerBlock.add(1, -1, 1), EnumFacing.EAST);
                } else if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(-1, -1, 0))) {
                        place(playerBlock.add(0, -1, 1), EnumFacing.WEST);
                    }
                    place(playerBlock.add(-1, -1, 1), EnumFacing.SOUTH);
                } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                        place(playerBlock.add(0, -1, 1), EnumFacing.SOUTH);
                    }
                    place(playerBlock.add(1, -1, 1), EnumFacing.WEST);
                } else if (BlockUtil.isValidBlock(playerBlock.add(1, -1, 1))) {
                    if (BlockUtil.isValidBlock(playerBlock.add(0, -1, 1))) {
                        place(playerBlock.add(0, -1, 1), EnumFacing.EAST);
                    }
                    place(playerBlock.add(1, -1, 1), EnumFacing.NORTH);
                }
                passedTicks = 0;
            }
            ++passedTicks;
        }

    }

    public void place(BlockPos posI, EnumFacing face) {
        BlockPos pos = posI;
        if (face == EnumFacing.UP) {
            pos = pos.add(0, -1, 0);
        } else if (face == EnumFacing.NORTH) {
            pos = pos.add(0, 0, 1);
        } else if (face == EnumFacing.SOUTH) {
            pos = pos.add(0, 0, -1);
        } else if (face == EnumFacing.EAST) {
            pos = pos.add(-1, 0, 0);
        } else if (face == EnumFacing.WEST) {
            pos = pos.add(1, 0, 0);
        }
        int oldSlot = mc.player.inventory.currentItem;
        int newSlot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (InventoryUtil.isNull(stack) || !(stack.getItem() instanceof ItemBlock) || !Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) continue;
            newSlot = i;
            break;
        }
        if (newSlot == -1) {
            return;
        }
        boolean crouched = false;
        if (!mc.player.isSneaking() && BlockInteractHelper.blackList.contains(mc.world.getBlockState(pos).getBlock())) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            crouched = true;
        }
        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(newSlot));
            mc.player.inventory.currentItem = newSlot;
            mc.playerController.updateController();
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.motionX *= 0.3;
            mc.player.motionZ *= 0.3;
            mc.player.jump();
            if (timer.passedMs(1500L)) {
                mc.player.motionY = -0.28;
                timer.reset();
            }
        }
        if (rotation.get_value(true)) {
            float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float)pos.getX() + 0.5f, (float)pos.getY() - 0.5f, (float)pos.getZ() + 0.5f));
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(angle[0], (float) MathHelper.normalizeAngle((int)angle[1], 360), mc.player.onGround));
        }
        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, face, new Vec3d(0.5, 0.5, 0.5), EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        mc.player.inventory.currentItem = oldSlot;
        mc.playerController.updateController();
        if (crouched) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
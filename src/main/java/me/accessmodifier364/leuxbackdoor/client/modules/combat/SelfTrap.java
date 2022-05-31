package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BlockInteractHelper;
import me.accessmodifier364.leuxbackdoor.client.util.MathUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SelfTrap extends Module {
    public SelfTrap() {
        super(Category.combat);
		this.name        = "SelfTrap";
        this.description = "oh 'eck, ive trapped me sen again";
    }

    Setting toggle = create("Toggle", "SelfTrapToggle", false);
    Setting rotate = create("Rotate", "SelfTrapRotate", false);
    Setting render = create("Render", "SelfTrapRender", true);
    Setting r = create("Red", "SelfTrapRed", 0, 0, 255);
    Setting g = create("Green", "SelfTrapGreen", 100, 0, 255);
    Setting b = create("Blue", "SelfTrapBlue", 100, 0, 255);
    Setting a = create("Alpha", "SelfTrapAlpha", 30, 0, 255);
    Setting renderMode = create("Render Mode", "SelfTrapMode", "Both", combobox("Quads", "Lines", "Both"));
    private BlockPos trap_pos;
    private boolean sneak;

    @Override
    public void disable() {
        if (sneak) {
            sneak = false;
            SelfTrap.mc.player.connection.sendPacket(new CPacketEntityAction(SelfTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }

    @Override
    public void update() {
        Vec3d pos = MathUtil.interpolateEntity(SelfTrap.mc.player, mc.getRenderPartialTicks());
        trap_pos = new BlockPos(pos.x, pos.y + 2.0, pos.z);
        if (is_trapped() && !toggle.get_value(true)) {
            set_disable();
        } else {
            int last_slot = SelfTrap.mc.player.inventory.currentItem;
            int slot = find_in_hotbar();
            if (slot == -1) {
                toggle();
            } else {
                SelfTrap.mc.player.inventory.currentItem = slot;
                SelfTrap.mc.playerController.updateController();
                BlockInteractHelper.ValidResult result = BlockInteractHelper.valid(trap_pos);
                if (result == BlockInteractHelper.ValidResult.AlreadyBlockThere && !SelfTrap.mc.world.getBlockState(trap_pos).getMaterial().isReplaceable()) {
                    SelfTrap.mc.player.inventory.currentItem = last_slot;
                } else if (result == BlockInteractHelper.ValidResult.NoNeighbors) {
                    for (BlockPos pos_ : new BlockPos[]{trap_pos.north(), trap_pos.south(), trap_pos.east(), trap_pos.west(), trap_pos.up(), trap_pos.down().west()}) {
                        BlockInteractHelper.ValidResult result_ = BlockInteractHelper.valid(pos_);
                        if (result_ == BlockInteractHelper.ValidResult.NoNeighbors || result_ == BlockInteractHelper.ValidResult.NoEntityCollision || !place_blocks(pos_)) continue;
                        SelfTrap.mc.player.inventory.currentItem = last_slot;
                        return;
                    }
                    SelfTrap.mc.player.inventory.currentItem = last_slot;
                } else {
                    place_blocks(trap_pos);
                    SelfTrap.mc.player.inventory.currentItem = last_slot;
                }
            }
        }
    }

    public boolean is_trapped() {
        if (trap_pos == null) {
            return false;
        }
        IBlockState state = SelfTrap.mc.world.getBlockState(trap_pos);
        return state.getBlock() != Blocks.AIR && state.getBlock() != Blocks.WATER && state.getBlock() != Blocks.LAVA;
    }

    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = SelfTrap.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockEnderChest) {
                return i;
            }
            if (!(block instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }

    private boolean place_blocks(BlockPos pos) {
        if (pos == null) {
            set_disable();
            return false;
        }
        if (!SelfTrap.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractHelper.checkForNeighbours(pos)) {
            return false;
        }
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!BlockInteractHelper.canBeClicked(neighbor)) continue;
            if (BlockInteractHelper.blackList.contains(SelfTrap.mc.world.getBlockState(neighbor).getBlock())) {
                SelfTrap.mc.player.connection.sendPacket(new CPacketEntityAction(SelfTrap.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                sneak = true;
            }
            Vec3d hitVec = new Vec3d(neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
            if (rotate.get_value(true)) {
                BlockInteractHelper.faceVectorPacketInstant(hitVec);
            }
            SelfTrap.mc.playerController.processRightClickBlock(SelfTrap.mc.player, SelfTrap.mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
            SelfTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    @Override
    public void render(EventRender event) {
        if (render.get_value(true) && trap_pos != null) {
            if (renderMode.in("Quads")) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), trap_pos.getX(), trap_pos.getY(), trap_pos.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            } else if (renderMode.in("Lines")) {
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), trap_pos.getX(), trap_pos.getY(), trap_pos.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            } else if (renderMode.in("Both")) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), trap_pos.getX(), trap_pos.getY(), trap_pos.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), trap_pos.getX(), trap_pos.getY(), trap_pos.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            }
        }
    }
}
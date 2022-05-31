package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BlockInteractHelper;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoleFill extends Module {
    public HoleFill() {
        super(Category.combat);
        this.name = "HoleFill";
        this.description = "Turn holes into floors";
    }

    Setting fillMode = create("Mode", "Mode", "Normal", combobox("Normal", "Smart", "Always"));
    Setting switchMode = create("Switch", "Switch", "Classic", combobox("Classic", "Silent"));
    Setting dual_enable = create("Dual", "Dual", true);
    Setting hole_rotate = create("Rotate", "Rotate", false);
    Setting hole_range = create("Range", "Range", 4, 1, 6);
    Setting smart_range = create("SmartRange", "SmartRange", 2, 1, 5);
    Setting render = create("Render", "Render", true);
    Setting r = create("Red", "Red", 0, 0, 255);
    Setting g = create("Green", "Green", 100, 0, 255);
    Setting b = create("Blue", "Blue", 100, 0, 255);
    Setting a = create("Alpha", "Alpha", 30, 0, 255);
    Setting renderMode = create("Render Mode", "Mode", "Both", combobox("Quads", "Lines", "Both"));

    private final ArrayList<BlockPos> holes = new ArrayList<>();
    Map<BlockPos, Integer> dual_hole_sides = new HashMap<>();
    public BlockPos pos_to_fill;

    @Override
    public void enable() {
        if (find_in_hotbar() == -1) {
            set_disable();
        }
        find_new_holes();
    }

    @Override
    public void update() {
        if (find_in_hotbar() == -1) {
            disable();
        } else {
            if (holes.isEmpty()) {
                if (fillMode.in("Normal")) {
                    set_disable();
                    return;
                }
                find_new_holes();
            }
            pos_to_fill = null;

            for (BlockPos pos : new ArrayList<>(holes)) {
                if (pos == null) continue;

                BlockInteractHelper.ValidResult result = BlockInteractHelper.valid(pos);
                if (result == BlockInteractHelper.ValidResult.Ok) {
                    if (fillMode.in("Smart")) {
                        for (EntityPlayer target : mc.world.playerEntities) {
                            if (target == mc.player || FriendUtil.isFriend(target.getName())) continue;

                            double distance = target.getDistance(pos.getX(), pos.getY(), pos.getZ());
                            if (distance < smart_range.get_value(1)) {
                                pos_to_fill = pos;
                            }

                        /*
                        if (target.getDistanceSq(pos) <= smart_range.get_value(1.0) * 4
                                && target.getDistanceSq(pos) < mc.player.getDistanceSq(pos)) {
                            pos_to_fill = pos;
                        }
                         */
                        }
                    } else {
                        pos_to_fill = pos;
                    }
                    break;
                }
                holes.remove(pos);
            }

            int obi_slot = find_in_hotbar();
            if (obi_slot == -1) {
                disable();
            } else if (pos_to_fill != null) {
                int last_slot = mc.player.inventory.currentItem;
                if (switchMode.in("Silent")) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(obi_slot));
                    if (place_blocks(pos_to_fill)) {
                        holes.remove(pos_to_fill);
                    }
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(last_slot));
                } else {
                    mc.player.inventory.currentItem = obi_slot;
                    mc.playerController.updateController();
                    if (place_blocks(pos_to_fill)) {
                        holes.remove(pos_to_fill);
                    }
                    mc.player.inventory.currentItem = last_slot;
                }
            }
        }
    }

    @Override
    public void disable() {
        holes.clear();
        dual_hole_sides.clear();
    }
    
    public void find_new_holes() {
        holes.clear();
        dual_hole_sides.clear();
        int colapso_range = (int) Math.ceil(hole_range.get_value(1));

        List<BlockPos> spheres = sphere(player_as_blockpos(), colapso_range);

        for (BlockPos pos : spheres) {
            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            boolean possible = true;

            int air_orient = -1;
            int counter = 0;

            for (BlockPos seems_blocks : new BlockPos[] {
                    new BlockPos( 0, -1,  0),
                    new BlockPos( 0,  0, -1),
                    new BlockPos( 1,  0,  0),
                    new BlockPos( 0,  0,  1),
                    new BlockPos(-1,  0,  0)
            }) {
                Block block = mc.world.getBlockState(pos.add(seems_blocks)).getBlock();

                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    possible = false;

                    if (counter == 0) break;

                    if (air_orient != -1) {
                        air_orient = -1;
                        break;
                    }

                    if (block.equals(Blocks.AIR)) {
                        air_orient = counter;
                    } else {
                        break;
                    }
                }

                counter++;
            }

            if (possible) {
                    holes.add(pos);
            }

            if (!dual_enable.get_value(true) || air_orient < 0) continue;
            BlockPos second_pos = pos.add(orientConv(air_orient));

            if (checkDual(second_pos, air_orient)) {
                boolean low_ceiling_hole = !mc.world.getBlockState(second_pos.add(0,1,0)).getBlock().equals(Blocks.AIR);
                    if (low_ceiling_hole) {
                        holes.add(pos);
                    } else {
                        if (!dual_hole_sides.containsKey(pos)) {
                            holes.add(pos);
                            dual_hole_sides.put(pos, air_orient);
                        }
                        if (!dual_hole_sides.containsKey(second_pos)) {
                            holes.add(second_pos);
                            dual_hole_sides.put(second_pos, oppositeIntOrient(air_orient));
                        }
                    }
            }
        }
    }

    private int oppositeIntOrient(int orient_count) {

        int opposite = 0;

        switch(orient_count)
        {
            case 0:
                opposite = 5;
                break;
            case 1:
                opposite = 3;
                break;
            case 2:
                opposite = 4;
                break;
            case 3:
                opposite = 1;
                break;
            case 4:
                opposite = 2;
                break;
        }
        return opposite;
    }

    private BlockPos orientConv(int orient_count) {
        BlockPos converted = null;

        switch(orient_count) {
            case 0:
                converted = new BlockPos( 0, -1,  0);
                break;
            case 1:
                converted = new BlockPos( 0,  0, -1);
                break;
            case 2:
                converted = new BlockPos( 1,  0,  0);
                break;
            case 3:
                converted = new BlockPos( 0,  0,  1);
                break;
            case 4:
                converted = new BlockPos(-1,  0,  0);
                break;
            case 5:
                converted = new BlockPos(0,  1,  0);
                break;
        }
        return converted;
    }

    private boolean checkDual(BlockPos second_block, int counter) {
        int i = -1;

		/*
			lets check down from second block to not have esp of a dual hole of one space
			missing a bottom block
		*/
        for (BlockPos seems_blocks : new BlockPos[] {
                new BlockPos( 0,  -1, 0), //Down
                new BlockPos( 0,  0, -1), //N
                new BlockPos( 1,  0,  0), //E
                new BlockPos( 0,  0,  1), //S
                new BlockPos(-1,  0,  0)  //W
        }) {
            i++;
            //skips opposite direction check, since its air
            if(counter == oppositeIntOrient(i)) {
                continue;
            }

            Block block = mc.world.getBlockState(second_block.add(seems_blocks)).getBlock();
            if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                return false;
            }
            
        }
        return true;
    }

    @Override
    public void render(EventRender event) {
        if (render.get_value(true) && pos_to_fill != null) {
            if (renderMode.in("Quads")) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            } else if (renderMode.in("Lines")) {
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            } else if (renderMode.in("Both")) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            }
        }
    }
    
    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                return i;
            }
            if (!(block instanceof BlockEnderChest)) continue;
            return i;
        }
        return -1;
    }

    public List<BlockPos> sphere(BlockPos pos, float r) {
        int plus_y = 0;

        List<BlockPos> sphere_block = new ArrayList<>();

        int cx = pos.getX();
        int cy = pos.getY();
        int cz = pos.getZ();

        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = cy - (int)r; y < cy + r; ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (cy - y) * (cy - y);
                    if (dist < r * r) {
                        BlockPos spheres = new BlockPos(x, y + plus_y, z);
                        sphere_block.add(spheres);
                    }
                }
            }
        }

        return sphere_block;
    }

    public BlockPos player_as_blockpos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    private boolean place_blocks(BlockPos pos) {
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractHelper.checkForNeighbours(pos)) {
            return false;
        }
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!BlockInteractHelper.canBeClicked(neighbor)) continue;
            if (BlockInteractHelper.blackList.contains(mc.world.getBlockState(neighbor).getBlock())) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            Vec3d hitVec = new Vec3d(neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
            if (hole_rotate.get_value(true)) {
                BlockInteractHelper.faceVectorPacketInstant(hitVec);
            }
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return false;
    }

}


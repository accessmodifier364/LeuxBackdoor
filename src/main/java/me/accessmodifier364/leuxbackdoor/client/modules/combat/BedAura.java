package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BlockInteractHelper;
import me.accessmodifier364.leuxbackdoor.client.util.BlockUtil;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Collectors;

public class BedAura extends Module {
    public BedAura() {
        super(Category.combat);
        this.name        = "BedAura";
        this.description = "fucking endcrystal.me";
    }

    Setting delay = create("Delay", "BedAuraDelay", 2, 0 , 20);
    Setting range = create("Range", "BedAuraRange", 5, 0, 6);
    Setting hard = create("Hard Rotate", "BedAuraRotate", false);
    Setting swing = create("Swing", "BedAuraSwing", "Offhand", combobox("Mainhand", "Offhand", "Both", "None"));

    private BlockPos render_pos;
    private int counter;
    private spoof_face spoof_looking;


    @Override
    protected void enable() {
        render_pos = null;
        counter = 0;
    }

    @Override
    protected void disable() {
        render_pos = null;
    }

    @Override
    public void update() {

        if (mc.player == null) return;

        if (counter > delay.get_value(1)) {
            counter = 0;
            place_bed();
            break_bed();
            refill_bed();
        }

        counter++;

    }

    public void refill_bed() {
        if (!(mc.currentScreen instanceof GuiContainer)) {
            if (is_space()) {
                for (int i = 9; i < 35; ++i) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                        break;
                    }
                }
            }
        }
    }

    private boolean is_space() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                return true;
            }
        }
        return false;
    }

    public void place_bed() {

        if (find_bed() == -1) {
            return;
        }

        int bed_slot = find_bed();

        BlockPos best_pos = null;
        EntityPlayer best_target = null;
        float best_distance = (float) range.get_value(1);

        for (EntityPlayer player : mc.world.playerEntities.stream().filter(entityPlayer -> !FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {

            if (player == mc.player) continue;

            if (best_distance < mc.player.getDistance(player)) continue;

            boolean face_place = true;

            BlockPos pos = get_pos_floor(player).down();
            BlockPos pos2 = check_side_block(pos);

            if (pos2 != null) {
                best_pos = pos2.up();
                best_target = player;
                best_distance = mc.player.getDistance(player);
                face_place = false;
            }

            if (face_place) {
                BlockPos upos = get_pos_floor(player);
                BlockPos upos2 = check_side_block(upos);

                if (upos2 != null) {
                    best_pos = upos2.up();
                    best_target = player;
                    best_distance = mc.player.getDistance(player);
                }
            }
        }

        if (best_target == null) {
            return;
        }

        render_pos = best_pos;

        if (spoof_looking == spoof_face.NORTH) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = 180;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(180, 0, mc.player.onGround));
        } else if (spoof_looking == spoof_face.SOUTH) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = 0;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 0, mc.player.onGround));
        } else if (spoof_looking == spoof_face.WEST) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = 90;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(90, 0, mc.player.onGround));
        } else if (spoof_looking == spoof_face.EAST) {
            if (hard.get_value(true)) {
                mc.player.rotationYaw = -90;
            }
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(-90, 0, mc.player.onGround));
        }

        BlockUtil.placeBlock(best_pos, bed_slot, false, false, swing);

    }

    public void break_bed() {

        for (BlockPos pos : BlockInteractHelper.getSphere(get_pos_floor(mc.player), range.get_value(1), range.get_value(1), false, true, 0)
                .stream().filter(BedAura::is_bed).collect(Collectors.toList())) {

            if (mc.player.isSneaking()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            BlockUtil.openBlock(pos);

        }

    }

    public int find_bed() {

        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                return i;
            }
        }
        return -1;
    }

    public BlockPos check_side_block(BlockPos pos) {

        if (mc.world.getBlockState(pos.east()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.WEST;
            return pos.east();
        }
        if (mc.world.getBlockState(pos.north()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.SOUTH;
            return pos.north();
        }
        if (mc.world.getBlockState(pos.west()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.EAST;
            return pos.west();
        }
        if (mc.world.getBlockState(pos.south()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.south().up()).getBlock() == Blocks.AIR) {
            spoof_looking = spoof_face.NORTH;
            return pos.south();
        }

        return null;

    }

    public static BlockPos get_pos_floor(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static boolean is_bed(final BlockPos pos) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        return block == Blocks.BED;
    }

    @Override
    public void render(EventRender event) {

        if (render_pos == null) return;

        RenderHelp.prepare("lines");
        RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
                render_pos.getX(), render_pos.getY(), render_pos.getZ(),
                1, .2f, 1,
                255, 20, 20, 180,
                "all"
        );
        RenderHelp.release();


    }

    enum spoof_face {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }

}

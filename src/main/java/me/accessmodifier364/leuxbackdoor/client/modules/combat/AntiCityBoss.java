package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BlockInteractHelper;
import me.accessmodifier364.leuxbackdoor.client.util.BlockUtil;
import me.accessmodifier364.leuxbackdoor.client.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class AntiCityBoss extends Module {

    public AntiCityBoss() {
		super(Category.combat);
		this.name        = "AntiCityBoss";
		this.description = "Protects your feet";
    }

    Setting rotate = create("Rotate", "AntiCityBossRotate", false);
    Setting swing = create("Swing", "AntiCityBossSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));

    @Override
    protected void enable() {
        if (find_in_hotbar() == -1) {
            set_disable();
        }
    }

    @Override
	public void update() {
        final int slot = find_in_hotbar();
        if (slot == -1) return;
        BlockPos center_pos = PlayerUtil.GetLocalPlayerPosFloored();
        ArrayList<BlockPos> blocks_to_fill = new ArrayList<>();
        switch (PlayerUtil.GetFacing())
        {
            case East:
                blocks_to_fill.add(center_pos.east().east());
                blocks_to_fill.add(center_pos.east().east().up());
                blocks_to_fill.add(center_pos.east().east().east());
                blocks_to_fill.add(center_pos.east().east().east().up());
                break;
            case North:
                blocks_to_fill.add(center_pos.north().north());
                blocks_to_fill.add(center_pos.north().north().up());
                blocks_to_fill.add(center_pos.north().north().north());
                blocks_to_fill.add(center_pos.north().north().north().up());
                break;
            case South:
                blocks_to_fill.add(center_pos.south().south());
                blocks_to_fill.add(center_pos.south().south().up());
                blocks_to_fill.add(center_pos.south().south().south());
                blocks_to_fill.add(center_pos.south().south().south().up());
                break;
            case West:
                blocks_to_fill.add(center_pos.west().west());
                blocks_to_fill.add(center_pos.west().west().up());
                blocks_to_fill.add(center_pos.west().west().west());
                blocks_to_fill.add(center_pos.west().west().west().up());
                break;
            default:
                break;
        }
        BlockPos pos_to_fill = null;
        for (BlockPos pos : blocks_to_fill) {
            BlockInteractHelper.ValidResult result = BlockInteractHelper.valid(pos);
            if (result != BlockInteractHelper.ValidResult.Ok) continue;
            if (pos == null) continue;
            pos_to_fill = pos;
            break;
        }
        BlockUtil.placeBlock(pos_to_fill, find_in_hotbar(), rotate.get_value(true), rotate.get_value(true), swing);
    }

    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockEnderChest)
                    return i;
                else if (block instanceof BlockObsidian)
                    return i;
            }
        }
        return -1;
    }

}
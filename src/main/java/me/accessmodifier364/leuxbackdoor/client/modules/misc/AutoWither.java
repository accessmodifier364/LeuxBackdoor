package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class AutoWither extends Module {
    public AutoWither() {
        super(Category.misc);
        this.name = "AutoWither";
        this.description = "makes withers";
    }

    @Override
    public void enable() {
        if (!has_blocks()) {
            MessageUtil.send_client_message("You don't have materials");
            set_disable();
            return;
        }

        MessageUtil.send_client_message("This module isn't finished");
    }

    public boolean has_blocks() {
        int count = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (block instanceof BlockSoulSand) {
                    count++;
                    break;
                }

            }
        }
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.SKULL && stack.getItemDamage() == 1) {
                count++;
                break;
            }
        }
        return count == 2;
    }
}

package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BreakUtil;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Collectors;

public class BurrowBreaker extends Module {
    public BurrowBreaker() {
        super(Category.combat);
        this.name        = "BurrowBreaker";
        this.description = "sex";
    }

    Setting range = create("Range", "MineRange", 5, 1, 6);
    Setting swap = create("Swap to Pick","MineSwap", false);

    BlockPos target_block = null;

    @Override
    public void enable() {
        for (EntityPlayer player : mc.world.playerEntities.stream().filter(entityPlayer -> !FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            if (mc.player.getDistance(player) > range.get_value(1) || mc.player == player) continue;

            BlockPos p = new BlockPos(player.posX, player.posY, player.posZ);

            if (mc.world.getBlockState(p).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(p).getBlock().equals(Blocks.ENDER_CHEST)) {
                target_block = p;
            }

        }


        if (target_block == null) {
            MessageUtil.send_client_message("Can't find block");
            set_disable();
        }

        int pickSlot = findPickaxe();
        if (swap.get_value(true) && pickSlot != -1) {
            mc.player.inventory.currentItem = pickSlot;
        }

        BreakUtil.setblock(target_block);

    }

    @Override
    public void update() {
        BreakUtil.breakblock(range.get_value(1));
        if (ModLoader.get_module_manager().get_module_with_tag("PacketMine").is_active()) set_disable();
    }

    @Override
    public void disable() {
        BreakUtil.setblock(null);
        target_block = null;
    }

    private int findPickaxe() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe) {
                return i;
            }
        }
        return -1;
    }
}
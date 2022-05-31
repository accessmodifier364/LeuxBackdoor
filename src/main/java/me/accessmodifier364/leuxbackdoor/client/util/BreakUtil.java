package me.accessmodifier364.leuxbackdoor.client.util;

import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class BreakUtil {
    private final static Minecraft mc = Minecraft.getMinecraft();
    private static BlockPos current_block = null;
    private static boolean is_mining = false;
    public static boolean finished = false;

    public static void setblock(BlockPos pos) {
        current_block = pos;
    }

    public static void breakblock(int range) {
        if (current_block == null) return;
        IBlockState state = mc.world.getBlockState(current_block);
        checkState(state);
        if (finished || mc.player.getDistanceSq(current_block) > (range * range)) {
            if (ModLoader.get_module_manager().get_module_with_tag("AutoObiBreaker").is_active()) {
                ModLoader.get_module_manager().get_module_with_tag("AutoObiBreaker").set_disable();
            }
            if (ModLoader.get_module_manager().get_module_with_tag("BurrowBreaker").is_active()) {
                ModLoader.get_module_manager().get_module_with_tag("BurrowBreaker").set_disable();
            }
            current_block = null;
            finished = false;
            return;
        }
        mc.player.swingArm(EnumHand.MAIN_HAND);
        EnumFacing facing = EnumFacing.UP;
        if (!is_mining) {
            is_mining = true;
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, current_block, facing));
        } else {
            mc.playerController.onPlayerDamageBlock(current_block, facing);
        }

    }

    public static void checkState(IBlockState state) {
        finished = state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.AIR || state.getBlock() instanceof BlockLiquid;
    }

}

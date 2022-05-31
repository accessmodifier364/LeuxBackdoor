package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockAura extends Module {
    public BlockAura() {
        super(Category.player);
        this.name = "BlockAura";
        this.description = "xddd";
    }

    Setting mode = create("Mode", "Mode", "Shulker", combobox("Shulker", "Hopper"));
    Setting range = create("Range", "Range", 5, 1, 6);
    Setting delay = create("Delay", "Delay", 10, 0, 20);

    private final List<BlockPos> retries = new ArrayList<>();
    private final List<BlockPos> selfPlaced = new ArrayList<>();
    private int ticks;

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayerTryUseItemOnBlock) {
            CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock)event.get_packet();

            Item item = mc.player.getHeldItem(packet.getHand()).getItem();

            Block hopper = Block.getBlockFromItem(item);

            if ((item instanceof ItemShulkerBox && mode.in("Shulker")) || (hopper instanceof BlockHopper && mode.in("Hopper"))) {
                selfPlaced.add(packet.getPos().offset(packet.getDirection()));
            }
        }
    });

    @Override
    public void update() {
        if (ticks++ < delay.get_value(10)) {
            ticks = 0;
            retries.clear();
        }
        final List<BlockPos> sphere = getSphere((float)range.get_value(5));
        for (int size = sphere.size(), i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (!retries.contains(pos) && !selfPlaced.contains(pos)) {
                if ((mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox && mode.in("Shulker")) || (mc.world.getBlockState(pos).getBlock() instanceof BlockHopper && mode.in("Hopper"))) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
                    retries.add(pos);
                }
            }
        }
    }

    public static List<BlockPos> getSphere(final float radius) {
        final ArrayList<BlockPos> sphere = new ArrayList<>();
        final BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        final int posX = pos.getX();
        final int posY = pos.getY();
        final int posZ = pos.getZ();
        for (int x = posX - (int)radius; x <= posX + radius; ++x) {
            for (int z = posZ - (int)radius; z <= posZ + radius; ++z) {
                for (int y = posY - (int)radius; y < posY + radius; ++y) {
                    if ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y) < radius * radius) {
                        sphere.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return sphere;
    }
}
package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventCollisionBoxToList;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.Wrapper;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Jesus extends Module {
	public Jesus() {
		super(Category.movement);
		this.name = "Jesus";
		this.description = "walk on da water";
	}

	private static final AxisAlignedBB WATER_WALK_AA;

	@Override
	public void update() {
		if (ModLoader.get_module_manager().get_module_with_tag("Freecam").is_disabled() && EntityUtil.isInWater((Entity)mc.player) && !mc.player.isSneaking()) {
			mc.player.motionY = 0.1;
			if (mc.player.getRidingEntity() != null && !(mc.player.getRidingEntity() instanceof EntityBoat)) {
				mc.player.getRidingEntity().motionY = 0.3;
			}
		}
	}

	@EventHandler
	private Listener<EventCollisionBoxToList> listener_collision = new Listener<>(event -> {
		if (mc.player != null && event.getBlock() instanceof BlockLiquid && (EntityUtil.isDrivenByPlayer(event.getEntity()) || event.getEntity() == mc.player) && !(event.getEntity() instanceof EntityBoat) && !mc.player.isSneaking() && mc.player.fallDistance < 3.0f && !EntityUtil.isInWater((Entity)mc.player) && (EntityUtil.isAboveWater((Entity)mc.player, false) || EntityUtil.isAboveWater(mc.player.getRidingEntity(), false)) && isAboveBlock((Entity)mc.player, event.getPos())) {
			final AxisAlignedBB axisalignedbb = WATER_WALK_AA.offset(event.getPos());
			if (event.getEntityBox().intersects(axisalignedbb)) {
				event.getCollidingBoxes().add(axisalignedbb);
			}
			event.cancel();
		}
	});

	@EventHandler
	private Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
		if (event.get_packet() instanceof CPacketPlayer && EntityUtil.isAboveWater((Entity)mc.player, true) && !EntityUtil.isInWater((Entity)mc.player) && !isAboveLand((Entity)mc.player)) {
			final int ticks = mc.player.ticksExisted % 2;
			if (ticks == 0) {
				final CPacketPlayer cPacketPlayer = (CPacketPlayer)event.get_packet();
				cPacketPlayer.y += 0.02;
			}
		}
		});

	private static boolean isAboveLand(final Entity entity) {
		if (entity == null) {
			return false;
		}
		final double y = entity.posY - 0.01;
		for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
			for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
				final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
				if (Wrapper.getWorld().getBlockState(pos).getBlock().isFullBlock(Wrapper.getWorld().getBlockState(pos))) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isAboveBlock(final Entity entity, final BlockPos pos) {
		return entity.posY >= pos.getY();
	}

	static {
		WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
	}
}
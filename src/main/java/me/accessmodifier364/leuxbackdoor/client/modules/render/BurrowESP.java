package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BurrowESP extends Module {
	public BurrowESP() {
		super(Category.render);
		this.name = "BurrowESP";
		this.description = "Highlight burrow";
	}

	Setting norenderown = create("No Render Own", "NoRenderOwn", true);
	Setting box = create("Box", "Box", true);
	Setting outline = create("Outline", "Outline", true);
	Setting red = create ("Red","Red", 220, 0, 255);
	Setting green = create ("Green","Green", 220, 0, 255);
	Setting blue = create ("Blue","Blue", 220, 0, 255);
	Setting alpha = create ("Alpha","Alpha", 100, 0, 255);

	private final List<BlockPos> posList = new ArrayList<>();

	@Override
	public void update() {
		if (mc.world == null)
			return;
		scanBurrowedPlayers();
	}

	private void scanBurrowedPlayers() {
		posList.clear();
		for (EntityPlayer player : mc.world.playerEntities) {
			if (!EntityUtil.isLiving(player) || player.getHealth() <= 0.0F || player.isSpectator()) {
				continue;
			}
			if (isBurrowed(player)) {
				if (player == mc.player && norenderown.get_value(true)) {
					continue;
				}
				posList.add(new BlockPos(player.posX, player.posY, player.posZ));
			}
		}
	}

	public boolean isBurrowed(EntityPlayer player) {
		BlockPos blockPos = new BlockPos(player.posX, player.posY + 0.2, player.posZ);
		return mc.world.getBlockState(blockPos).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(blockPos).getBlock().equals(Blocks.ENDER_CHEST) || mc.world.getBlockState(blockPos).getBlock().equals(Blocks.SKULL);
	}

	@Override
	public void render(EventRender event) {
		for (BlockPos blockPos : posList) {
			RenderUtil.drawBox(blockPos, new Color(red.get_value(1), green.get_value(1), blue.get_value(1), alpha.get_value(1)), box.get_value(true), outline.get_value(true));
		}
	}

}

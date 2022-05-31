package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

public class FastUse extends Module {
	public FastUse() {
		super(Category.player);
		this.name = "FastUse";
		this.description = "use things faster";
	}

	Setting everything = create("Everything", "FastEverything", false);
	Setting fast_place = create("Place", "FastPlace", false);
	Setting fast_break = create("Break", "FastBreak", false);
	Setting crystal = create("Crystal", "FastCrystal", false);
	Setting exp = create("EXP", "FastExp", true);
	Setting egg = create("SpawnEgg", "SpawnEgg", true);
	Setting bow = create("Bow", "FastBow", false);

	public void update() {
		Item main = mc.player.getHeldItemMainhand().getItem();
		Item off = mc.player.getHeldItemOffhand().getItem();
		boolean main_exp = main instanceof net.minecraft.item.ItemExpBottle;
		boolean off_exp = off instanceof net.minecraft.item.ItemExpBottle;
		boolean main_cry = main instanceof net.minecraft.item.ItemEndCrystal;
		boolean off_cry = off instanceof net.minecraft.item.ItemEndCrystal;
		boolean main_bow = main instanceof net.minecraft.item.ItemBow;
		boolean off_bow = off instanceof net.minecraft.item.ItemBow;

		boolean main_egg = main == Items.SPAWN_EGG;
		boolean off_egg = off == Items.SPAWN_EGG;

		if (everything.get_value(true))
			mc.rightClickDelayTimer = 0;
		if (main_egg | off_egg && egg.get_value(true))
			mc.rightClickDelayTimer = 0;
		if (main_exp | off_exp && exp.get_value(true))
			mc.rightClickDelayTimer = 0;
		if (main_bow | off_bow && bow.get_value(true) && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player
					.getHorizontalFacing()));
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
			mc.player.stopActiveHand();
		}
		if (main_cry | off_cry && crystal.get_value(true))
			mc.rightClickDelayTimer = 0;
		if (!(main_exp | off_exp | main_cry | off_cry) && fast_place.get_value(true))
			mc.rightClickDelayTimer = 0;
		if (fast_break.get_value(true))
			mc.playerController.blockHitDelay = 0;
	}
}

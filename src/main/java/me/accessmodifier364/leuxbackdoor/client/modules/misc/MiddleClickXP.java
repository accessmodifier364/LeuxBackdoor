package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.InventoryUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MiddleClickXP extends Module {
    public MiddleClickXP() {
        super(Category.misc);
        this.name = "MiddleClickXP";
        this.description = "fucking xp when u press middle click";
    }

    Setting silent = create("Silent", "Silent", false);

	int last_slot;

	@Override
	public void update() {
		if (mc.player == null || mc.world == null) {
			return;
		}

		last_slot = mc.player.inventory.currentItem;
		int xp = InventoryUtil.getHotbarItemSlot(Items.EXPERIENCE_BOTTLE);

		if (xp != -1 && Mouse.isButtonDown(2)) {
			if (silent.get_value(true)) {
				mc.player.connection.sendPacket(new CPacketHeldItemChange(xp));
				mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
				mc.player.connection.sendPacket(new CPacketHeldItemChange(last_slot));
			} else {
				InventoryUtil.switchToSlot(xp);
				mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
			}
		}

	}
}
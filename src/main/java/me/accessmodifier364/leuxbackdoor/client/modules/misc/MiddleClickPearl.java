package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPlayerTravel;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MiddleClickPearl extends Module {
    public MiddleClickPearl() {
        super(Category.misc);
        this.name = "MiddleClickPearl";
        this.description = "throws a pearl when middleclick";
    }

	private boolean clicked;
    
	@EventHandler
	private final Listener<EventPlayerTravel> listener = new Listener<>(p_Event -> {
		if (mc.currentScreen == null && Mouse.isButtonDown(2)) {
			int pearlSLot;
			if (!clicked && (pearlSLot = findPearlInHotbar()) != -1) {
				int oldSlot = mc.player.inventory.currentItem;
				mc.player.inventory.currentItem = pearlSLot;
				mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
				mc.player.inventory.currentItem = oldSlot;
			}
			clicked = true;
		} else {
			clicked = false;
		}
	});

	private boolean isItemStackPearl(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemEnderPearl;
	}

	private int findPearlInHotbar() {
		int index = 0;
		while (InventoryPlayer.isHotbar(index)) {
			if (isItemStackPearl(mc.player.inventory.getStackInSlot(index))) {
				return index;
			}
			++index;
		}
		return -1;
	}
}
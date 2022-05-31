package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArmorDurabilityWarner extends Pinnable {
    public ArmorDurabilityWarner() {
        super("Armor Warner", "ArmorWarner", 1, 0, 0);
    }

    private boolean already_warned_helmet = false;
    private boolean already_warned_chestplate = false;
    private boolean already_warned_leggings = false;
    private boolean already_warned_boots = false;

    @Override
	public void render() {
		String line = "See ur armor :p";
        if (is_damaged()) {
            create_line(line, this.docking(1, line), 2, 255, 20, 20, 255);
        }
        this.set_width(this.get(line, "width") + 2);
        this.set_height(this.get(line, "height") + 2);
    }

    private boolean is_damaged() {
        for (Map.Entry<Integer, ItemStack> armor_slot : get_armor().entrySet()) {
            if (armor_slot.getValue().isEmpty()) continue;
            final ItemStack stack = armor_slot.getValue();

            double max_dam = stack.getMaxDamage();
            double dam_left = stack.getMaxDamage() - stack.getItemDamage();
            double percent = (dam_left / max_dam) * 100;

            if (mc.player.inventoryContainer.getInventory().get(5)== stack) {
                if (percent < 30 && !already_warned_helmet) {
                    already_warned_helmet = true;
                } else if (percent > 30) {
                    already_warned_helmet = false;
                }
            } else if (mc.player.inventoryContainer.getInventory().get(6)== stack) {
                if (percent < 40 && !already_warned_chestplate) {
                    already_warned_chestplate = true;
                } else if (percent > 40) {
                    already_warned_chestplate = false;
                }
            } else if (mc.player.inventoryContainer.getInventory().get(7)== stack) {
                if (percent < 40 && !already_warned_leggings) {
                    already_warned_leggings = true;
                } else if (percent > 40) {
                    already_warned_leggings = false;
                }
            } else if (mc.player.inventoryContainer.getInventory().get(8)== stack) {
                if (percent < 30 && !already_warned_boots) {
                    already_warned_boots = true;
                } else if (percent > 30) {
                    already_warned_boots = false;
                }
            }
            if (percent < 30) {
                return true;
            }
        }
        return false;
    }

    private Map<Integer, ItemStack> get_armor() {
        return get_inv_slots(5, 8);
    }

    private Map<Integer, ItemStack> get_inv_slots(int current, final int last) {
        final Map<Integer, ItemStack> full_inv_slots = new HashMap<>();
        while (current <= last) {
            full_inv_slots.put(current, mc.player.inventoryContainer.getInventory().get(current));
            ++current;
        }
        return full_inv_slots;
    }
}

package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;

public class Offhand extends Module {

    public Offhand() {
        super(Category.combat);
        this.name        = "Offhand";
        this.description = "Switches shit to ur offhand";
    }

    Setting switch_mode = create("Offhand", "OffhandOffhand", "Crystal", combobox("Totem", "Crystal", "Gapple", "SpawnEgg"));
    Setting totem_switch = create("Totem HP", "OffhandTotemHP", 15, 0, 36);
    Setting fall_dist = create("Fall Distance", "FallDistance", 20.0, 0.0, 50.0);
    Setting totem_ca_disabled = create("TotemWhenCAOff", "TotemWhenCAOff", false);
    Setting sword_gap = create("Sword Gap", "OffhandSwordGap", true);
    Setting right_click_gap = create("Right Click Gap", "OffhandRightGap", false);
    Setting totem_elytra = create("Totem Elytra", "OffhandTotemElytra", false);
    Setting delay = create("Delay", "OffhandDelay", false);

    private boolean switching = false;
    private int last_slot;

    @Override
    public void update() {
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {
            if (switching) {
                swap_items(last_slot, 2);
                return;
            }
            float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            if (hp > totem_switch.get_value(1) && mc.player.fallDistance <= fall_dist.get_value(1.0)) {

                if (sword_gap.get_value(true) && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && ModLoader.get_module_manager().get_module_with_tag("AutoCrystal").is_disabled()) {
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), delay.get_value(true) ? 1 : 0);
                    return;
                }

                if (right_click_gap.get_value(true) && mc.gameSettings.keyBindUseItem.isKeyDown() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemChorusFruit) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemBow)) {
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), delay.get_value(true) ? 1 : 0);
                    return;
                }

                if (totem_elytra.get_value(true) && mc.player.isElytraFlying()) {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
                    return;
                }

                if (switch_mode.in("Crystal")) {
                    if (totem_ca_disabled.get_value(true)) {
                        if (ModLoader.get_module_manager().get_module_with_tag("AutoCrystal").is_active()) {
                            swap_items(get_item_slot(Items.END_CRYSTAL),0);
                            return;
                        }
                    } else {
                        swap_items(get_item_slot(Items.END_CRYSTAL),0);
                        return;
                    }
                }

                if (switch_mode.in("Totem")) {
                    swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
                    return;
                }

                if (switch_mode.in("SpawnEgg")) {
                    swap_items(get_item_slot(Items.SPAWN_EGG), delay.get_value(true) ? 1 : 0);
                    return;
                }

                if (switch_mode.in("Gapple")) {
                    swap_items(get_item_slot(Items.GOLDEN_APPLE), delay.get_value(true) ? 1 : 0);
                    return;
                }

                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
                return;
            } else {
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
            }

            if (mc.player.getHeldItemOffhand().getItem() == Items.AIR) {
                swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), delay.get_value(true) ? 1 : 0);
            }
        }
    }

    public void swap_items(int slot, int step) {
        if (slot == -1) return;
        if (step == 0) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
        }
        if (step == 1) {
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = true;
            last_slot = slot;
        }
        if (step == 2) {
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
            switching = false;
        }
        mc.playerController.updateController();
    }

    private int get_item_slot(Item input) {
        if (input == mc.player.getHeldItemOffhand().getItem()) return -1;
        for(int i = 36; i >= 0; i--) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if(item == input) {
                if (i < 9) {
                    if (input == Items.GOLDEN_APPLE) {
                        return -1;
                    }
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    @Override
    public String array_detail() {
        return switch_mode.get_current_value() + ", " + totem_switch.get_value(1);
    }
}

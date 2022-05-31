package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.EventHandler;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AutoArmor extends Module {
    private final Timer timer = new Timer();
    private final Timer elytraTimer = new Timer();
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<>();
    private final List<Integer> doneSlots = new ArrayList<>();
    Setting delay = create("Delay", "Delay", 50, 0, 100);
    Setting mendingTakeOff = create("AutoMend", "AutoMend", true);
    Setting closestEnemy = create("Enemy", "Enemy", 8, 1, 20);
    Setting helmetThreshold = create("Helmet", "Helmet", 80, 1, 100);
    Setting chestThreshold = create("Chest", "Chest", 80, 1, 100);
    Setting legThreshold = create("Legs", "Legs", 80, 1, 100);
    Setting bootsThreshold = create("Boots", "Boots", 80, 1, 100);
    Setting curse = create("CurseOfBinding", "CurseOfBinding", false);
    Setting actions = create("Actions", "Actions", 3, 1, 12);
    Setting tps = create("TpsSync", "TpsSync", true);
    Setting updateController = create("Update", "Update", true);
    Setting shiftClick = create("ShiftClick", "ShiftClick", false);
    private boolean elytraOn = false;
    public AutoArmor() {
        super(Category.player);
        this.name = "AutoArmor";
        this.description = "WATCH UR BOOTS";
    }

    @Override
    public void disable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.elytraOn = false;
    }

    @Override
    public void update() {
        if (mc.player == null || mc.world == null || mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (this.taskList.isEmpty()) {
            int slot;
            ItemStack feet;
            int slot2;
            ItemStack legging;
            int slot3;
            ItemStack chest;
            int slot4;
            if (this.mendingTakeOff.get_value(true) && InventoryUtil.holdingItem(ItemExpBottle.class) && mc.gameSettings.keyBindUseItem.isKeyDown() && (this.isSafe() || EntityUtil.isSafe((Entity) mc.player, 1, false, true))) {
                int bootDamage;
                int leggingDamage;
                int chestDamage;
                int helmDamage;
                ItemStack helm = mc.player.inventoryContainer.getSlot(5).getStack();
                if (!helm.isEmpty && (helmDamage = DamageUtil.getRoundedDamage(helm)) >= this.helmetThreshold.get_value(1)) {
                    this.takeOffSlot(5);
                }
                ItemStack chest2 = mc.player.inventoryContainer.getSlot(6).getStack();
                if (!chest2.isEmpty && (chestDamage = DamageUtil.getRoundedDamage(chest2)) >= this.chestThreshold.get_value(1)) {
                    this.takeOffSlot(6);
                }
                ItemStack legging2 = mc.player.inventoryContainer.getSlot(7).getStack();
                if (!legging2.isEmpty && (leggingDamage = DamageUtil.getRoundedDamage(legging2)) >= this.legThreshold.get_value(1)) {
                    this.takeOffSlot(7);
                }
                ItemStack feet2 = mc.player.inventoryContainer.getSlot(8).getStack();
                if (!feet2.isEmpty && (bootDamage = DamageUtil.getRoundedDamage(feet2)) >= this.bootsThreshold.get_value(1)) {
                    this.takeOffSlot(8);
                }
                return;
            }
            ItemStack helm = mc.player.inventoryContainer.getSlot(5).getStack();
            if (helm.getItem() == Items.AIR && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, this.curse.get_value(true), ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) != -1) {
                this.getSlotOn(5, slot4);
            }
            if ((chest = mc.player.inventoryContainer.getSlot(6).getStack()).getItem() == Items.AIR) {
                if (this.taskList.isEmpty()) {
                    if (this.elytraOn && this.elytraTimer.passedMs(500L)) {
                        int elytraSlot = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active());
                        if (elytraSlot != -1) {
                            if (elytraSlot < 5 && elytraSlot > 1 || !this.shiftClick.get_value(true)) {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot));
                                this.taskList.add(new InventoryUtil.Task(6));
                            } else {
                                this.taskList.add(new InventoryUtil.Task(elytraSlot, true));
                            }
                            if (this.updateController.get_value(true)) {
                                this.taskList.add(new InventoryUtil.Task());
                            }
                            this.elytraTimer.reset();
                        }
                    } else if (!this.elytraOn && (slot3 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, this.curse.get_value(true), ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) != -1) {
                        this.getSlotOn(6, slot3);
                    }
                }
            } else if (this.elytraOn && chest.getItem() != Items.ELYTRA && this.elytraTimer.passedMs(500L)) {
                if (this.taskList.isEmpty()) {
                    slot3 = InventoryUtil.findItemInventorySlot(Items.ELYTRA, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active());
                    if (slot3 != -1) {
                        this.taskList.add(new InventoryUtil.Task(slot3));
                        this.taskList.add(new InventoryUtil.Task(6));
                        this.taskList.add(new InventoryUtil.Task(slot3));
                        if (this.updateController.get_value(true)) {
                            this.taskList.add(new InventoryUtil.Task());
                        }
                    }
                    this.elytraTimer.reset();
                }
            } else if (!this.elytraOn && chest.getItem() == Items.ELYTRA && this.elytraTimer.passedMs(500L) && this.taskList.isEmpty()) {
                slot3 = InventoryUtil.findItemInventorySlot((Item) Items.DIAMOND_CHESTPLATE, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active());
                if (slot3 == -1 && (slot3 = InventoryUtil.findItemInventorySlot((Item) Items.IRON_CHESTPLATE, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot((Item) Items.GOLDEN_CHESTPLATE, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) == -1 && (slot3 = InventoryUtil.findItemInventorySlot((Item) Items.CHAINMAIL_CHESTPLATE, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) == -1) {
                    slot3 = InventoryUtil.findItemInventorySlot((Item) Items.LEATHER_CHESTPLATE, false, ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active());
                }
                if (slot3 != -1) {
                    this.taskList.add(new InventoryUtil.Task(slot3));
                    this.taskList.add(new InventoryUtil.Task(6));
                    this.taskList.add(new InventoryUtil.Task(slot3));
                    if (this.updateController.get_value(true)) {
                        this.taskList.add(new InventoryUtil.Task());
                    }
                }
                this.elytraTimer.reset();
            }
            if ((legging = mc.player.inventoryContainer.getSlot(7).getStack()).getItem() == Items.AIR && (slot2 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, this.curse.get_value(true), ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) != -1) {
                this.getSlotOn(7, slot2);
            }
            if ((feet = mc.player.inventoryContainer.getSlot(8).getStack()).getItem() == Items.AIR && (slot = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, this.curse.get_value(true), ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) != -1) {
                this.getSlotOn(8, slot);
            }
        }
        if (this.timer.passedMs((int) ((float) this.delay.get_value(1) * (this.tps.get_value(true) ? 20.0f / Math.round(EventHandler.INSTANCE.get_tick_rate()) : 1.0f)))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < this.actions.get_value(1); ++i) {
                    InventoryUtil.Task task = this.taskList.poll();
                    if (task == null) continue;
                    task.run();
                }
            }
            this.timer.reset();
        }
    }

    private void takeOffSlot(int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (int i : InventoryUtil.findEmptySlots(ModLoader.get_module_manager().get_module_with_tag("XCarry").is_active())) {
                if (this.doneSlots.contains(target)) continue;
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                if (target < 5 && target > 0 || !this.shiftClick.get_value(true)) {
                    this.taskList.add(new InventoryUtil.Task(slot));
                    this.taskList.add(new InventoryUtil.Task(target));
                } else {
                    this.taskList.add(new InventoryUtil.Task(slot, true));
                }
                if (this.updateController.get_value(true)) {
                    this.taskList.add(new InventoryUtil.Task());
                }
            }
        }
    }

    private void getSlotOn(int slot, int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object) target);
            if (target < 5 && target > 0 || !this.shiftClick.get_value(true)) {
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task(slot));
            } else {
                this.taskList.add(new InventoryUtil.Task(target, true));
            }
            if (this.updateController.get_value(true)) {
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }

    private boolean isSafe() {
        EntityPlayer closest = EntityUtil.getClosestEnemy(this.closestEnemy.get_value(1));
        if (closest == null) {
            return true;
        }
        return mc.player.getDistanceSq(closest) >= MathUtil.square(this.closestEnemy.get_value(1));
    }
}

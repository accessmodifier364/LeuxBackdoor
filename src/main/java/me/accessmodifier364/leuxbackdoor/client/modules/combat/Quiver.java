package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.InventoryUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class Quiver extends Module {
    public Quiver() {
        super(Category.combat);
        this.name        = "Quiver";
        this.description = "shoots good arrows at you";
    }
    Setting toggle = create("Toggle", "Toggle", false);
    Setting mode = create("Mode", "Mode", "Automatic", combobox("Automatic", "Manual"));
    Setting speed = create("Speed", "Speed", true);
    Setting strength = create("Strength", "Strength", true);

    int randomVariation;

    public void update() {
        PotionEffect speedEffect = mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)));
        PotionEffect strengthEffect = mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(5)));

        boolean hasSpeed;
        boolean hasStrength;

        hasSpeed = speedEffect != null;

        hasStrength = strengthEffect != null;

        if (InventoryUtil.getHeldItem(Items.BOW))
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, -90, true));

        if (mode.in("Automatic")) {
            if (strength.get_value(true) && !hasStrength) {
                if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW && isArrowInInventory("Arrow of Strength")) {
                    if (mc.player.getItemInUseMaxCount() >= getBowCharge()) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                        mc.player.stopActiveHand();
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }

                    else if (mc.player.getItemInUseMaxCount() == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }
                }
            }

            if (speed.get_value(true) && !hasSpeed) {
                if (mc.player.inventory.getCurrentItem().getItem() == Items.BOW && isArrowInInventory("Arrow of Speed")) {
                    if (mc.player.getItemInUseMaxCount() >= getBowCharge()) {
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                        mc.player.stopActiveHand();
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }

                    else if (mc.player.getItemInUseMaxCount() == 0) {
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.setActiveHand(EnumHand.MAIN_HAND);
                    }
                }
            }
        }

        if (!toggle.get_value(true))
            this.disable();
    }

    private boolean isArrowInInventory(String type) {
        boolean inInv = false;
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW) {
                if (itemStack.getDisplayName().equalsIgnoreCase(type)) {
                    inInv = true;
                    switchArrow(i);
                    break;
                }
            }
        }
        return inInv;
    }

    private void switchArrow(int oldSlot) {
        MessageUtil.send_client_message("Switching arrows!");
        int bowSlot = mc.player.inventory.currentItem;
        int placeSlot = bowSlot +1;
        if (placeSlot > 8)
            placeSlot = 1;
        if (placeSlot != oldSlot) {
            if (mc.currentScreen instanceof GuiContainer)
                return;
            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, placeSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
        }
    }

    private int getBowCharge() {
        if (randomVariation == 0)
            randomVariation = 1;
        return 1 + randomVariation;
    }
}
package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class PvpHud extends Pinnable {
    public PvpHud() {
        super("PVP Hud", "pvphud", 1.0F, 0, 0);
    }

    public void render() {
        int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
        int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
        int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
        int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);
        String totem = get_totems();
        String trap = trap_enabled();
        String aura = aura_enabled();
        String surround = surround_enabled();
        String holefill = holefill_enabled();
        String socks = socks_enabled();
        String selftrap = selftrap_enabled();
        String killaura = killaura_enabled();
        create_line(totem, docking(1, totem), 2, nl_r, nl_g, nl_b, nl_a);
        create_line(aura, docking(1, aura), 13, nl_r, nl_g, nl_b, nl_a);
        create_line(trap, docking(1, trap), 24, nl_r, nl_g, nl_b, nl_a);
        create_line(surround, docking(1, surround), 34, nl_r, nl_g, nl_b, nl_a);
        create_line(holefill, docking(1, holefill), 45, nl_r, nl_g, nl_b, nl_a);
        create_line(socks, docking(1, socks), 56, nl_r, nl_g, nl_b, nl_a);
        create_line(selftrap, docking(1, selftrap), 67, nl_r, nl_g, nl_b, nl_a);
        create_line(killaura, docking(1, killaura), 78, nl_r, nl_g, nl_b, nl_a);
        set_width(get(surround, "width") + 2);
        set_height(get(surround, "height") * 5);
    }

    public String selftrap_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("SelfTrap").is_active())
                return "SelfTrap";
            return "\u00A7cSelfTrap";
        } catch (Exception e) {
            return "0";
        }
    }

    public String trap_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("AutoTrap").is_active())
                return "AutoTrap";
            return "\u00A7cAutoTrap";
        } catch (Exception e) {
            return "0";
        }
    }

    public String aura_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("AutoCrystal").is_active())
                return "Aura";
            return "\u00A7cAura";
        } catch (Exception e) {
            return "0";
        }
    }

    public String killaura_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("KillAura").is_active())
                return "KillAura";
            return "\u00A7cKillAura";
        } catch (Exception e) {
            return "\u00A74ERROR";
        }
    }

    public String socks_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("AntiCityBoss").is_active())
                return "Socks";
            return "\u00A7cSocks";
        } catch (Exception e) {
            return "\u00A74ERROR";
        }
    }

    public String surround_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("Surround").is_active())
                return "Surround";
            return "\u00A7cSurround";
        } catch (Exception e) {
            return "\u00A74ERROR";
        }
    }

    public String holefill_enabled() {
        try {
            if (ModLoader.get_hack_manager().get_module_with_tag("HoleFill").is_active())
                return "HoleFill";
            return "\u00A7cHoleFill";
        } catch (Exception e) {
            return "\u00A74ERROR";
        }
    }

    public String get_totems() {
        try {
            int totems = offhand() + this.mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
            if (totems >= 1)
                return "Totems " + totems;
            return "\u00A7cTotems " + totems;
        } catch (Exception e) {
            return "\u00A74ERROR";
        }
    }

    public int offhand() {
        if (this.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            return 1;
        return 0;
    }
}
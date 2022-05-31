package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Objects;

public class MoonJump extends Module { //For servers without anticheat lol
    public MoonJump() {
        super(Category.movement);
        this.name = "MoonJump";
        this.description = "bum bum";
    }

    Setting multiplier = create("Multiplier", "Multiplier", 30, 0, 80);

    public void update() {
            final PotionEffect effect = new PotionEffect(Objects.requireNonNull(Potion.getPotionById(8)), 123456789, multiplier.get_value(1));
            effect.setPotionDurationMax(true);
            mc.player.addPotionEffect(effect);
    }

    public void disable() {
        mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(8)));
    }
}
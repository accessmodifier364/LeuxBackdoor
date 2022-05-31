package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

import java.text.DecimalFormat;

public class Speedometer extends Pinnable {

    public Speedometer() {
        super("Speedometer", "Speedometer", 1, 0, 0);
    }

    @Override
    public void render() {
        int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
        int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
        int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
        int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);
        final double x = mc.player.posX - mc.player.prevPosX;
        final double z = mc.player.posZ - mc.player.prevPosZ;
        final float tr = (mc.timer.tickLength / 1000.0f);
        String bps = "Speed: " + new DecimalFormat("\u00A7a#.#").format(MathHelper.sqrt(x * x + z * z) / tr * 3.6);
        create_line(bps, this.docking(1, bps), 2, nl_r, nl_g, nl_b, nl_a);
        this.set_width(this.get(bps, "width") + 2);
        this.set_height(this.get(bps, "height")  + 2);
    }

    public static double get_speed() {
        final Minecraft mc = Minecraft.getMinecraft();
        final double x = mc.player.posX - mc.player.prevPosX;
        final double z = mc.player.posZ - mc.player.prevPosZ;
        final float tr = (mc.timer.tickLength / 1000.0f);
        return MathHelper.sqrt(x * x + z * z) / tr * 3.6;
    }
}
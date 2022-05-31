package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.client.util.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.util.TimeUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.util.math.MathHelper;


public class User extends Pinnable {
    private int scaled_width;
    private int scaled_height;
    private int scale_factor;

    public User() {
        super("User", "User", 1, 0, 0);
    }

    @Override
    public void render() {
        updateResolution();
        int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
        int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
        int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
        int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

        int time = TimeUtil.get_hour();
        String line;

        if (ModLoader.get_setting_manager().get_setting_with_tag("Settings", "RainbowAura").get_value(true)) {
            if (time >= 0 && time < 12) {
                line = "Good Morning, " + mc.player.getName() + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            } else if (time >= 12 && time < 16) {
                line = "Good Afternoon, " + mc.player.getName() + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            } else if (time >= 16 && time < 24) {
                line = "Good Evening, " + mc.player.getName() + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            } else {
                line = "Hello, " + mc.player.getName() + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            }

            create_rainbow_line(line, scaled_width / 2 - mc.fontRenderer.getStringWidth(line) / 2, 20);

        } else {
            if (time >= 0 && time < 12) {
                line = "Good Morning, " + ChatFormatting.BOLD + mc.player.getName() + ChatFormatting.RESET + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            } else if (time >= 12 && time < 16) {
                line = "Good Afternoon, " + ChatFormatting.BOLD + mc.player.getName() + ChatFormatting.RESET + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            } else if (time >= 16 && time < 24) {
                line = "Good Evening, " + ChatFormatting.BOLD + mc.player.getName() + ChatFormatting.RESET + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            } else {
                line = "Hello, " + ChatFormatting.BOLD + mc.player.getName() + ChatFormatting.RESET + " welcome to " + ModLoader.CLIENT_NAME + " :)";
            }

            FontUtil.drawStringWithShadow(true, line, scaled_width / 2 - mc.fontRenderer.getStringWidth(line) / 2, 20, new Draw.ClientColor(nl_r, nl_g, nl_b, nl_a).hex());
        }


        this.set_width(this.get(line, "width") + 2);
        this.set_height(this.get(line, "height") + 2);
    }

    public void updateResolution() {
        this.scaled_width = mc.displayWidth;
        this.scaled_height = mc.displayHeight;
        this.scale_factor = 1;
        final boolean flag = mc.isUnicode();
        int i = mc.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scale_factor < i && this.scaled_width / (this.scale_factor + 1) >= 320 && this.scaled_height / (this.scale_factor + 1) >= 240) {
            ++this.scale_factor;
        }
        if (flag && this.scale_factor % 2 != 0 && this.scale_factor != 1) {
            --this.scale_factor;
        }
        final double scaledWidthD = this.scaled_width / (double) this.scale_factor;
        final double scaledHeightD = this.scaled_height / (double) this.scale_factor;
        this.scaled_width = MathHelper.ceil(scaledWidthD);
        this.scaled_height = MathHelper.ceil(scaledHeightD);
    }

}
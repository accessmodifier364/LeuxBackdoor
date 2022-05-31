package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Frame;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.PinnableButton;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.RenameUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

import java.awt.*;

public class Settings extends Module {
    public Settings() {
        super(Category.client);
        this.name = "Settings";
        this.description = "Change some client settings";
    }

    Setting custom_client_name = create("Custom Client Name", "CustomClientName", false);
    Setting gui = create("RGB GUI", "GUI", false);

    Setting hud = create("RGB HUD", "HUD", false);
    Setting rainbow = create("RainbowAura", "RainbowAura", true);
    Setting rainbowHue = create("RainbowHue", "RainbowHue", 240, 0, 600);
    Setting rainbowBrightness = create("RainbowBgt", "RainbowBgt", 150, 0, 255);
    Setting rainbowSaturation = create("RainbowSat", "RainbowSat", 150, 0, 255);

    Setting addmessage = create("MSGWhenFriend", "MSGWhenFriend", true);

    int color_r;
    int color_g;
    int color_b;

    @Override
    public void update() {
        if (mc.world != null && mc.player != null) {

            if (custom_client_name.get_value(true)) {
                ModLoader.set_client_name(RenameUtil.get_client_name().replace("[", "").replace("]", ""));
            }
            float[] tick_color = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32)};
            int color_rgb = Color.HSBtoRGB(tick_color[0], 1, 1);
            color_r = ((color_rgb >> 16) & 0xFF);
            color_g = ((color_rgb >> 8) & 0xFF);
            color_b = (color_rgb & 0xFF);

            if (hud.get_value(true)) {
                ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").set_value(color_r);
                ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").set_value(color_g);
                ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").set_value(color_b);
            }

            if (gui.get_value(true)) {
                ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameR").set_value(color_r);
                ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameG").set_value(color_g);
                ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameB").set_value(color_b);

                ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameWidgetR").set_value(color_r);
                ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameWidgetG").set_value(color_g);
                ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameWidgetB").set_value(color_b);

                Frame.nc_r = color_r;
                Frame.nc_g = color_g;
                Frame.nc_b = color_b;
                Frame.bd_r = color_r;
                Frame.bd_g = color_g;
                Frame.bd_b = color_b;
                Frame.bd_a = 0;
                Frame.bdw_r = color_r;
                Frame.bdw_g = color_g;
                Frame.bdw_b = color_b;
                Frame.bdw_a = 255;
                PinnableButton.nc_r = color_r;
                PinnableButton.nc_g = color_g;
                PinnableButton.nc_b = color_b;
                PinnableButton.bg_r = color_r;
                PinnableButton.bg_g = color_g;
                PinnableButton.bg_b = color_b;
                PinnableButton.bd_r = color_r;
                PinnableButton.bd_g = color_g;
                PinnableButton.bd_b = color_b;
            }
        }
    }
}

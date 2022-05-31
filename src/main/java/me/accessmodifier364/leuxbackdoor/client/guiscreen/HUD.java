package me.accessmodifier364.leuxbackdoor.client.guiscreen;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Frame;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.PinnableButton;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.gui.GuiScreen;


public class HUD extends GuiScreen {
    private final Frame frame;

    private int frame_height;

    public boolean on_gui;
    public boolean back;

    public HUD() {
        this.frame  = new Frame("HUD", "HUD", 40, 40);
        this.back   = false;
        this.on_gui = false;
    }

    public Frame get_frame_hud() {
        return this.frame;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        this.on_gui = true;

        Frame.nc_r = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameR").get_value(1);
        Frame.nc_g = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameG").get_value(1);
        Frame.nc_b = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameB").get_value(1);

        Frame.bd_r = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderFrameR").get_value(1);
        Frame.bd_g = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderFrameG").get_value(1);
        Frame.bd_b = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderFrameB").get_value(1);
        Frame.bd_a = 255;

        Frame.bdw_r = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderWidgetR").get_value(1);
        Frame.bdw_g = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderWidgetG").get_value(1);
        Frame.bdw_b = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderWidgetB").get_value(1);
        Frame.bdw_a = 255;

        PinnableButton.nc_r = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameR").get_value(1);
        PinnableButton.nc_g = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameG").get_value(1);
        PinnableButton.nc_b = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUINameFrameB").get_value(1);

        PinnableButton.bg_r = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBackgroundWidgetR").get_value(1);
        PinnableButton.bg_g = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBackgroundWidgetG").get_value(1);
        PinnableButton.bg_b = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBackgroundWidgetB").get_value(1);
        PinnableButton.bg_a = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBackgroundWidgetA").get_value(1);

        PinnableButton.bd_r = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderFrameR").get_value(1);
        PinnableButton.bd_g = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderFrameG").get_value(1);
        PinnableButton.bd_b = ModLoader.get_setting_manager().get_setting_with_tag("GUI", "ClickGUIBorderFrameB").get_value(1);
    }

    @Override
    public void onGuiClosed() {
        if (this.back) {
            ModLoader.get_hack_manager().get_module_with_tag("GUI").set_active(true);
            ModLoader.get_hack_manager().get_module_with_tag("HUD").set_active(false);
        } else {
            ModLoader.get_hack_manager().get_module_with_tag("HUD").set_active(false);
            ModLoader.get_hack_manager().get_module_with_tag("GUI").set_active(false);
        }
        this.on_gui = false;
    }

    @Override
    protected void mouseClicked(int mx, int my, int mouse) {
        this.frame.mouse(mx, my, mouse);

        if (mouse == 0) {
            if (this.frame.motion(mx, my) && this.frame.can()) {
                this.frame.set_move(true);
                this.frame.set_move_x(mx - this.frame.get_x());
                this.frame.set_move_y(my - this.frame.get_y());
            }
        }
    }

    @Override
    protected void mouseReleased(int mx, int my, int state) {
        this.frame.release(mx, my, state);
        this.frame.set_move(false);
    }

    @Override
    public void drawScreen(int mx, int my, float tick) {
        this.drawDefaultBackground();

        this.frame.render(mx, my, 2);
    }
}
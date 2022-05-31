package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.util.ResourceLocation;

public class ClickGUI extends Module {

	public ClickGUI() {
		super(Category.client);
		this.name = "GUI";
		this.description = "The main gui";
	}
	
	Setting blur = create("Blur", "Blur", false);
	Setting label_frame = this.create("info", "ClickGUIInfoFrame", "Frames");
	Setting name_frame_r = this.create("Name R", "ClickGUINameFrameR", 255, 0, 255);
	Setting name_frame_g = this.create("Name G", "ClickGUINameFrameG", 0, 0, 255);
	Setting name_frame_b = this.create("Name B", "ClickGUINameFrameB", 255, 0, 255);
	Setting background_frame_r = this.create("Background R", "ClickGUIBackgroundFrameR", 0, 0, 255);
	Setting background_frame_g = this.create("Background G", "ClickGUIBackgroundFrameG", 0, 0, 255);
	Setting background_frame_b = this.create("Background B", "ClickGUIBackgroundFrameB", 0, 0, 255);
	Setting background_frame_a = this.create("Background A", "ClickGUIBackgroundFrameA", 255, 0, 255);
	Setting border_frame_r = this.create("Border R", "ClickGUIBorderFrameR", 255, 0, 255);
	Setting border_frame_g = this.create("Border G", "ClickGUIBorderFrameG", 0, 0, 255);
	Setting border_frame_b = this.create("Border B", "ClickGUIBorderFrameB", 255, 0, 255);
	Setting label_widget = this.create("info", "ClickGUIInfoWidget", "Widgets");
	Setting name_widget_r = this.create("Name R", "ClickGUINameWidgetR", 255, 0, 255);
	Setting name_widget_g = this.create("Name G", "ClickGUINameWidgetG", 45, 0, 255);
	Setting name_widget_b = this.create("Name B", "ClickGUINameWidgetB", 255, 0, 255);
	Setting background_widget_r = this.create("Background R", "ClickGUIBackgroundWidgetR", 255, 0, 255);
	Setting background_widget_g = this.create("Background G", "ClickGUIBackgroundWidgetG", 40, 0, 255);
	Setting background_widget_b = this.create("Background B", "ClickGUIBackgroundWidgetB", 255, 0, 255);
	Setting background_widget_a = this.create("Background A", "ClickGUIBackgroundWidgetA", 170, 0, 255);
	Setting border_widget_r = this.create("Border R", "ClickGUIBorderWidgetR", 255, 0, 255);
	Setting border_widget_g = this.create("Border G", "ClickGUIBorderWidgetG", 0, 0, 255);
	Setting border_widget_b = this.create("Border B", "ClickGUIBorderWidgetB", 255, 0, 255);

	@Override
	public void update() {
		if (mc.world != null && mc.player != null) {
			ModLoader.click_gui.theme_frame_name_r = this.name_frame_r.get_value(1);
			ModLoader.click_gui.theme_frame_name_g = this.name_frame_g.get_value(1);
			ModLoader.click_gui.theme_frame_name_b = this.name_frame_b.get_value(1);
			ModLoader.click_gui.theme_frame_background_r = this.background_frame_r.get_value(1);
			ModLoader.click_gui.theme_frame_background_g = this.background_frame_g.get_value(1);
			ModLoader.click_gui.theme_frame_background_b = this.background_frame_b.get_value(1);
			ModLoader.click_gui.theme_frame_background_a = this.background_frame_a.get_value(1);
			ModLoader.click_gui.theme_frame_border_r = this.border_frame_r.get_value(1);
			ModLoader.click_gui.theme_frame_border_g = this.border_frame_g.get_value(1);
			ModLoader.click_gui.theme_frame_border_b = this.border_frame_b.get_value(1);
			ModLoader.click_gui.theme_widget_name_r = this.name_widget_r.get_value(1);
			ModLoader.click_gui.theme_widget_name_g = this.name_widget_g.get_value(1);
			ModLoader.click_gui.theme_widget_name_b = this.name_widget_b.get_value(1);
			ModLoader.click_gui.theme_widget_background_r = this.background_widget_r.get_value(1);
			ModLoader.click_gui.theme_widget_background_g = this.background_widget_g.get_value(1);
			ModLoader.click_gui.theme_widget_background_b = this.background_widget_b.get_value(1);
			ModLoader.click_gui.theme_widget_background_a = this.background_widget_a.get_value(1);
			ModLoader.click_gui.theme_widget_border_r = this.border_widget_r.get_value(1);
			ModLoader.click_gui.theme_widget_border_g = this.border_widget_g.get_value(1);
			ModLoader.click_gui.theme_widget_border_b = this.border_widget_b.get_value(1);
	}
}

	@Override
	public void enable() {
		if (mc.world != null && mc.player != null) {
			if (blur.get_value(true)) {
				mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
			mc.displayGuiScreen(ModLoader.click_gui);
		}
	}

	@Override
	public void disable() {
		if (mc.world != null && mc.player != null) {
			try {
				if (mc.entityRenderer.isShaderActive()) {
					mc.entityRenderer.getShaderGroup().deleteShaderGroup();
				}
			} catch (Exception ignored) {
			}
			mc.displayGuiScreen(null);
		}
	}

}
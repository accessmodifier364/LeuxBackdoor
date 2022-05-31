package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight extends Module {

	public BlockHighlight() {
		super(Category.render);
		this.name        = "BlockHighlight";
		this.description = "see what block ur targeting";
	}

	Setting mode = create("Mode", "HighlightMode", "Pretty", combobox("Pretty", "Solid", "Outline"));
	
	Setting rgb = create("RGB Effect", "HighlightRGBEffect", false);

	Setting saturation = create("Saturation", "Saturation", 50, 0, 100);
	Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
	Setting speed = create("Speed", "Speed", 40, 1, 100);

	Setting r = create("R", "HighlightR", 205, 0, 255);
	Setting g = create("G", "HighlightG", 40, 0, 255);
	Setting b = create("B", "HighlightB", 125, 0, 255);
	Setting a = create("A", "HighlightA", 0, 0, 255);
	
	Setting l_a = create("Outline A", "HighlightLineA", 255, 0, 255);

	int color_r;
	int color_g;
	int color_b;

	boolean outline = false;
	boolean solid   = false;

	@Override
	public void disable() {
		outline = false;
		solid   = false;
	}

	@Override
	public void render(EventRender event) {
		if (mc.player != null && mc.world != null) {

			Color rainbowColor = new Color(RenderUtil.getRainbow(speed.get_value(1) * 100, 0, (float) saturation.get_value(1) / 100.0f, (float) brightness.get_value(1) / 100.0f));

			if (rgb.get_value(true)) {
				color_r = (rainbowColor.getRed());
				color_g = (rainbowColor.getGreen());
				color_b = (rainbowColor.getBlue());

				r.set_value(color_r);
				g.set_value(color_g);
				b.set_value(color_b);
			} else {
				color_r = r.get_value(1);
				color_g = g.get_value(2);
				color_b = b.get_value(3);
			}

			if (mode.in("Pretty")) {
				outline = true;
				solid   = true;
			}
	
			if (mode.in("Solid")) {
				outline = false;
				solid   = true;
			}
	
			if (mode.in("Outline")) {
				outline = true;
				solid   = false;
			}
	
			RayTraceResult result = mc.objectMouseOver;
	
			if (result != null) {
				if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
					BlockPos block_pos = result.getBlockPos();
	
					// Solid.
					if (solid) {
						RenderHelp.prepare("quads");
						RenderHelp.draw_cube(block_pos, color_r, color_g, color_b, a.get_value(1), "all");
						RenderHelp.release();
					}
	
					// Outline.
					if (outline) {
						RenderHelp.prepare("lines");
						RenderHelp.draw_cube_line(block_pos, color_r, color_g, color_b, l_a.get_value(1), "all");
						RenderHelp.release();
					}
				}
			}
		}
	}
}
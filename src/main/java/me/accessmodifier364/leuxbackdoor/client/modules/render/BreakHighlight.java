package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class BreakHighlight extends Module {

    public BreakHighlight() {
        super(Category.render);
        this.name = "BreakHighlight";
        this.description = "Highlight blocks being broken & warns u when someone is mining your feet";
    }

    Setting mode = create("Mode", "HighlightMode", "Pretty", combobox("Pretty", "Solid", "Outline"));

    Setting rgb = create("RGB Effect", "HighlightRGBEffect", false);

	Setting saturation = create("Saturation", "Saturation", 50, 0, 100);
	Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
	Setting speed = create("Speed", "Speed", 40, 1, 100);

    Setting r = create("R", "BreakR", 40, 0, 255);
	Setting g = create("G", "BreakG", 230, 0, 255);
	Setting b = create("B", "BreakB", 220, 0, 255);
	Setting range = create("Range", "BreakRange", 6, 0, 25);
	Setting a = create("A", "BreakA", 35, 0, 255);

	Setting l_a = create("Outline A", "BreakLineA", 255, 0, 255);

	int color_r;
	int color_g;
	int color_b;

	boolean outline = false;
	boolean solid   = false;

    @Override
    protected void disable() {
        outline = false;
        solid = false;
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

			mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
				if (destroyBlockProgress != null) {
					BlockPos blockPos = destroyBlockProgress.getPosition();
					if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) {
						return;
					}

					if (blockPos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= range.get_value(1)) {
						if (solid) {
							RenderHelp.prepare("quads");
							RenderHelp.draw_cube(RenderHelp.get_buffer_build(),
									blockPos.getX(), blockPos.getY(), blockPos.getZ(),
									1, 1, 1,
									r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1),
									"all"
							);
							RenderHelp.release();
						}
						if (outline) {
							RenderHelp.prepare("lines");
							RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(),
									blockPos.getX(), blockPos.getY(), blockPos.getZ(),
									1, 1, 1,
									r.get_value(1), g.get_value(1), b.get_value(1), l_a.get_value(1),
									"all"
							);
							RenderHelp.release();
						}
					}
				}
			});
        }
    }
}

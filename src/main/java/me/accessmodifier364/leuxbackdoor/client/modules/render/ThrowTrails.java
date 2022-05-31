package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ThrowTrails extends Module {
    public ThrowTrails() {
        super(Category.render);
        this.name = "ThrowTrails";
        this.description = "strong";
    }

    Setting rainbow = create("Rainbow", "Rainbow", true);
    Setting stayTime = create("StayTime", "StayTime", 10.0, 0.0, 30.0);
    Setting enderpearlWidth = create("Width", "Width", 0.7, 0.0, 2.0);
    Setting red = create("Red", "Red", 200, 0, 255);
    Setting green = create("Green", "Green", 0, 0, 255);
    Setting blue = create("Blue", "Blue", 200, 0, 255);
    Setting alpha = create("Alpha", "Alpha", 130, 0, 255);
    Setting saturation = create("Saturation", "Saturation", 50, 0, 100);
    Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
    Setting speed = create("Speed", "Speed", 40, 1, 100);

    private final HashMap<UUID, List<Vec3d>> poses = new HashMap<>();
    private final HashMap<UUID, Double> time = new HashMap<>();


    public void update() {
        if (nullCheck()) return;

        if (rainbow.get_value(true)) {
            Color rainbowColor = new Color(RenderUtil.getRainbow(speed.get_value(1) * 100, 0, (float) saturation.get_value(1) / 100.0f, (float) brightness.get_value(1) / 100.0f));
            red.set_value(rainbowColor.getRed());
            green.set_value(rainbowColor.getGreen());
            blue.set_value(rainbowColor.getBlue());
        }

        Iterator<?> iterator = new HashMap<>(time).entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Double> uuidDoubleEntry = (Map.Entry) iterator.next();

            if (uuidDoubleEntry.getValue() <= 0.0) {
                poses.remove(uuidDoubleEntry.getKey());
                time.remove(uuidDoubleEntry.getKey());
            } else {
                time.replace(uuidDoubleEntry.getKey(), uuidDoubleEntry.getValue() - 0.05);
            }
        }

        iterator = mc.world.loadedEntityList.iterator();

        while (true) {
            Entity entity;

            do {
                if (!iterator.hasNext()) return;

                entity = (Entity) iterator.next();
            } while (!(entity instanceof EntityEnderPearl));

            if (!poses.containsKey(entity.getUniqueID())) {
                poses.put(entity.getUniqueID(), new ArrayList<>(Collections.singletonList(entity.getPositionVector())));
                time.put(entity.getUniqueID(), stayTime.get_value(1.0));
            } else {
                time.replace(entity.getUniqueID(), stayTime.get_value(1.0));

                List<Vec3d> vec3ds = poses.get(entity.getUniqueID());
                vec3ds.add(entity.getPositionVector());
            }
        }
    }

    public void render(EventRender render) {
        if (nullCheck()) return;

        RenderHelp.prepare_gl();

        Iterator<?> iterator = this.poses.entrySet().iterator();

        while (true) {
            Map.Entry<?, ?> entry;

            do {
                if (!iterator.hasNext()) {
                    RenderHelp.release_gl();

                    return;
                }

                entry = (Map.Entry<?, ?>) iterator.next();

            } while (((List<?>) entry.getValue()).size() <= 2);

            GL11.glBegin(1);
            GL11.glLineWidth((float) enderpearlWidth.get_value(1.0));
            GL11.glColor4f(red.get_value(1) / 255.0f, green.get_value(1) / 255.0f, blue.get_value(1) / 255.0f, alpha.get_value(1) / 255.0f);

            for (int i = 1; i < ((List<?>) entry.getValue()).size(); ++i) {
                GL11.glVertex3d(((Vec3d) ((List<?>) entry.getValue()).get(i)).x - mc.getRenderManager().renderPosX, ((Vec3d) ((List<?>) entry.getValue()).get(i)).y - mc.getRenderManager().renderPosY, ((Vec3d) ((List<?>) entry.getValue()).get(i)).z - mc.getRenderManager().renderPosZ);
                GL11.glVertex3d(((Vec3d) ((List<?>) entry.getValue()).get(i - 1)).x - mc.getRenderManager().renderPosX, ((Vec3d) ((List<?>) entry.getValue()).get(i - 1)).y - mc.getRenderManager().renderPosY, ((Vec3d) ((List<?>) entry.getValue()).get(i - 1)).z - mc.getRenderManager().renderPosZ);
            }

            GL11.glEnd();
        }
    }

}

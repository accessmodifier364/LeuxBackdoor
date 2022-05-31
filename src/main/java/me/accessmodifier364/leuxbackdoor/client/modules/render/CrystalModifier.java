package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderEntityModel;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CrystalModifier extends Module {
    public CrystalModifier() {
        super(Category.render);
        this.name = "CrystalModifier";
        this.description = "mmmmm crystal chams";
        INSTANCE = this;
    }

    public Setting animateScale = create("AnimateScale", "AnimateScale", false);
    public Setting chams = create("Chams", "Chams", true);
    public Setting throughWalls = create("ThroughWalls", "ThroughWalls", true);
    public Setting wireframeThroughWalls = create("WireThroughWalls", "WireThroughWalls", true);
    public Setting glint = create("Glint", "Glint", false);
    public Setting wireframe = create("Wireframe", "Wireframe", true);
    public Setting scale = create("Scale", "Scale", 0.93f, 0.1f, 10.0f);
    public Setting lineWidth = create("LineWidth", "LineWidth", 1.94f, 0.1f, 3.0f);
    public Setting rainbow = create("Rainbow", "Rainbow", true);
    public Setting saturation = create("Saturation", "Saturation", 70, 0, 100);
    public Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
    public Setting speed = create("Speed", "Speed", 40, 1, 100);
    public Setting xqz = create("XQZ", "XQZ", false);
    public Setting red = create("Red", "Red", 255, 0, 255);
    public Setting green = create("Green", "Green", 0, 0, 255);
    public Setting blue = create("Blue", "Blue", 255, 0, 255);
    public Setting alpha = create("Alpha", "Alpha", 50, 0, 255);
    public Setting outlineRed = create("Outline Red", "OutlineRed", 255, 0, 255);
    public Setting outlineGreen = create("Outline Green", "OutlineGreen", 0, 0, 255);
    public Setting outlineBlue = create("Outline Blue", "OutlineBlue", 255, 0, 255);
    public Setting outlineAlpha = create("Outline Alpha", "OutlineAlpha", 255, 0, 255);

    public Map<EntityEnderCrystal, Float> scaleMap = new ConcurrentHashMap<>();
    public static CrystalModifier INSTANCE;

    @Override
    public void update() {
    	try {
        for (Entity crystal : mc.world.loadedEntityList) {
            if (!(crystal instanceof EntityEnderCrystal)) continue;
            if (!this.scaleMap.containsKey(crystal)) {
                this.scaleMap.put((EntityEnderCrystal)crystal, 3.125E-4f);
            } else {
                this.scaleMap.put((EntityEnderCrystal)crystal, this.scaleMap.get(crystal) + 3.125E-4f);
            }
            if (!(this.scaleMap.get(crystal) >= 0.0625f * this.scale.get_value(1.0f))) continue;
            this.scaleMap.remove(crystal);
        }
    } catch (Exception ignored) {
    }
    }

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packet = (SPacketDestroyEntities)event.get_packet();
            for (int id : packet.getEntityIDs()) {
                Entity entity = mc.world.getEntityByID(id);
                if (!(entity instanceof EntityEnderCrystal)) continue;
                this.scaleMap.remove(entity);
            }
        }
    });

    @Override
    public void on_render_model(final EventRenderEntityModel event) {
        try {
            if (event.get_era() != EventCancellable.Era.EVENT_PRE || !(event.entity instanceof EntityEnderCrystal) || !this.wireframe.get_value(true)) {
                return;
            }
            mc.gameSettings.fancyGraphics = false;
            mc.gameSettings.gammaSetting = 10000.0f;
            GL11.glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glPolygonMode(1032, 6913);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            if (this.wireframeThroughWalls.get_value(true)) {
                GL11.glDisable(2929);
            }
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GlStateManager.blendFunc(770, 771);
            if (rainbow.get_value(true)) {
                Color rainbowColor1 = new Color(RenderUtil.getRainbow(speed.get_value(1) * 100, 0, (float) saturation.get_value(1) / 100.0f, (float) brightness.get_value(1) / 100.0f));
                Color rainbowColor = EntityUtil.getColor(event.entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), alpha.get_value(1), true);
                GlStateManager.color((float) rainbowColor.getRed() / 255.0f, (float) rainbowColor.getGreen() / 255.0f, (float) rainbowColor.getBlue() / 255.0f, (float) outlineAlpha.get_value(1) / 255.0f);
            } else {
                GlStateManager.color((float) outlineRed.get_value(1) / 255.0f, (float) outlineGreen.get_value(1) / 255.0f, (float) outlineBlue.get_value(1) / 255.0f, (float) outlineAlpha.get_value(1) / 255.0f);
            }
            GlStateManager.glLineWidth((float) this.lineWidth.get_value(1.0f));
            event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
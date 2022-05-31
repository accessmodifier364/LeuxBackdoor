package me.accessmodifier364.leuxbackdoor.client.mixins;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderName;
import me.accessmodifier364.leuxbackdoor.client.modules.render.HandColor;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(value = RenderPlayer.class)
public class MixinRenderPlayer {

    @Inject(method = "renderEntityName", at = @At("HEAD"), cancellable = true)
    public void renderLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {

        EventRenderName event_packet = new EventRenderName(entityIn, x, y, z, name, distanceSq);

        EventClientBus.EVENT_BUS.post(event_packet);

        if (event_packet.isCancelled()) {
            info.cancel();
        }

    }

    @Inject(method = {"renderRightArm"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181)}, cancellable = true)
    public void renderRightArmBegin(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColor.INSTANCE.is_active()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            if (HandColor.INSTANCE.rainbow.get_value(true)) {
                Color rainbowColor = new Color(RenderUtil.getRainbow(HandColor.INSTANCE.speed.get_value(1) * 100, 0, (float) HandColor.INSTANCE.saturation.get_value(1) / 100.0f, (float) HandColor.INSTANCE.brightness.get_value(1) / 100.0f));
                GL11.glColor4f((float) rainbowColor.getRed() / 255.0f, (float) rainbowColor.getGreen() / 255.0f, (float) rainbowColor.getBlue() / 255.0f, (float) HandColor.INSTANCE.alpha.get_value(1) / 255.0f);
            } else {
                Color color = new Color(HandColor.INSTANCE.red.get_value(1), HandColor.INSTANCE.green.get_value(1), HandColor.INSTANCE.blue.get_value(1), HandColor.INSTANCE.alpha.get_value(1));
                GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) color.getAlpha() / 255.0f);
            }
        }
    }

    @Inject(method = {"renderRightArm"}, at = {@At(value = "RETURN")}, cancellable = true)
    public void renderRightArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColor.INSTANCE.is_active()) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }

    @Inject(method = {"renderLeftArm"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181)}, cancellable = true)
    public void renderLeftArmBegin(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColor.INSTANCE.is_active()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            if (HandColor.INSTANCE.rainbow.get_value(true)) {
                Color rainbowColor = new Color(RenderUtil.getRainbow(HandColor.INSTANCE.speed.get_value(1) * 100, 0, (float) HandColor.INSTANCE.saturation.get_value(1) / 100.0f, (float) HandColor.INSTANCE.brightness.get_value(1) / 100.0f));
                GL11.glColor4f((float) rainbowColor.getRed() / 255.0f, (float) rainbowColor.getGreen() / 255.0f, (float) rainbowColor.getBlue() / 255.0f, (float) HandColor.INSTANCE.alpha.get_value(1) / 255.0f);
            } else {
                Color color = new Color(RenderUtil.getRainbow(HandColor.INSTANCE.speed.get_value(1) * 100, 0, (float) HandColor.INSTANCE.saturation.get_value(1) / 100.0f, (float) HandColor.INSTANCE.brightness.get_value(1) / 100.0f));
                GL11.glColor4f((float) color.getRed() / 255.0f, (float) color.getGreen() / 255.0f, (float) color.getBlue() / 255.0f, (float) HandColor.INSTANCE.alpha.get_value(1) / 255.0f);
            }
        }
    }

    @Inject(method = {"renderLeftArm"}, at = {@At(value = "RETURN")}, cancellable = true)
    public void renderLeftArmReturn(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (clientPlayer == Minecraft.getMinecraft().player && HandColor.INSTANCE.is_active()) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }

}
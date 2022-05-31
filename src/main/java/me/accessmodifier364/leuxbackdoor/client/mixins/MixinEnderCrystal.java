package me.accessmodifier364.leuxbackdoor.client.mixins;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderEntityModel;
import me.accessmodifier364.leuxbackdoor.client.modules.render.CrystalModifier;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(value = {RenderEnderCrystal.class})
public class MixinEnderCrystal {
    @Shadow
    @Final
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    private static final ResourceLocation glint;

    @Redirect(method = {"doRender"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (CrystalModifier.INSTANCE.is_active()) {
            if (CrystalModifier.INSTANCE.animateScale.get_value(true) && CrystalModifier.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale(CrystalModifier.INSTANCE.scaleMap.get(entity).floatValue(), CrystalModifier.INSTANCE.scaleMap.get(entity).floatValue(), CrystalModifier.INSTANCE.scaleMap.get(entity).floatValue());
            } else {
                GlStateManager.scale((float) CrystalModifier.INSTANCE.scale.get_value(1.0f), (float) CrystalModifier.INSTANCE.scale.get_value(1.0f), (float) CrystalModifier.INSTANCE.scale.get_value(1.0f));
            }
        }
        if (CrystalModifier.INSTANCE.is_active() && CrystalModifier.INSTANCE.wireframe.get_value(true)) {
            EventRenderEntityModel event = new EventRenderEntityModel(0, model, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            CrystalModifier.INSTANCE.on_render_model(event);
        }
        if (CrystalModifier.INSTANCE.is_active() && CrystalModifier.INSTANCE.chams.get_value(true)) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            if (CrystalModifier.INSTANCE.rainbow.get_value(true)) {
                Color rainbowColor1 = new Color(RenderUtil.getRainbow(CrystalModifier.INSTANCE.speed.get_value(1) * 100, 0, (float) CrystalModifier.INSTANCE.saturation.get_value(1) / 100.0f, (float) CrystalModifier.INSTANCE.brightness.get_value(1) / 100.0f));
                Color rainbowColor = EntityUtil.getColor(entity, rainbowColor1.getRed(), rainbowColor1.getGreen(), rainbowColor1.getBlue(), CrystalModifier.INSTANCE.alpha.get_value(1), true);
                if (CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f((float) rainbowColor.getRed() / 255.0f, (float) rainbowColor.getGreen() / 255.0f, (float) rainbowColor.getBlue() / 255.0f, (float) CrystalModifier.INSTANCE.alpha.get_value(1) / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            } else if (CrystalModifier.INSTANCE.xqz.get_value(true) && CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                Color visibleColor;
                Color hiddenColor = EntityUtil.getColor(entity, CrystalModifier.INSTANCE.outlineRed.get_value(1), CrystalModifier.INSTANCE.outlineGreen.get_value(1), CrystalModifier.INSTANCE.outlineBlue.get_value(1), CrystalModifier.INSTANCE.outlineAlpha.get_value(1), true);
                Color color = visibleColor = EntityUtil.getColor(entity, CrystalModifier.INSTANCE.red.get_value(1), CrystalModifier.INSTANCE.green.get_value(1), CrystalModifier.INSTANCE.blue.get_value(1), CrystalModifier.INSTANCE.alpha.get_value(1), true);
                if (CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f((float) hiddenColor.getRed() / 255.0f, (float) hiddenColor.getGreen() / 255.0f, (float) hiddenColor.getBlue() / 255.0f, (float) CrystalModifier.INSTANCE.alpha.get_value(1) / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glColor4f((float) visibleColor.getRed() / 255.0f, (float) visibleColor.getGreen() / 255.0f, (float) visibleColor.getBlue() / 255.0f, (float) CrystalModifier.INSTANCE.alpha.get_value(1) / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            } else {
                Color visibleColor;
                Color color = visibleColor = EntityUtil.getColor(entity, CrystalModifier.INSTANCE.red.get_value(1), CrystalModifier.INSTANCE.green.get_value(1), CrystalModifier.INSTANCE.blue.get_value(1), CrystalModifier.INSTANCE.alpha.get_value(1), true);
                if (CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f((float) visibleColor.getRed() / 255.0f, (float) visibleColor.getGreen() / 255.0f, (float) visibleColor.getBlue() / 255.0f, (float) CrystalModifier.INSTANCE.alpha.get_value(1) / 255.0f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (CrystalModifier.INSTANCE.throughWalls.get_value(true)) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
            if (CrystalModifier.INSTANCE.glint.get_value(true)) {
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GlStateManager.enableAlpha();
                GlStateManager.color(1.0f, 0.0f, 0.0f, 0.13f);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.disableAlpha();
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            }
        } else {
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        if (CrystalModifier.INSTANCE.is_active()) {
            if (CrystalModifier.INSTANCE.animateScale.get_value(true) && CrystalModifier.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale(1.0f / CrystalModifier.INSTANCE.scaleMap.get(entity).floatValue(), 1.0f / CrystalModifier.INSTANCE.scaleMap.get(entity).floatValue(), 1.0f / CrystalModifier.INSTANCE.scaleMap.get(entity).floatValue());
            } else {
                GlStateManager.scale((float) (1.0f / CrystalModifier.INSTANCE.scale.get_value(1.0f)), (float) (1.0f / CrystalModifier.INSTANCE.scale.get_value(1.0f)), (float) (1.0f / CrystalModifier.INSTANCE.scale.get_value(1.0f)));
            }
        }
    }

    static {
        glint = new ResourceLocation("textures/glint.png");
    }
}


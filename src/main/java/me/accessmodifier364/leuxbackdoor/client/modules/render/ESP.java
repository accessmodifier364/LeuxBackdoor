package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ESP extends Module {

    public ESP() {
        super(Category.render);
        this.name = "ESP";
        this.description = "see even less (now with epic colours)";
    }

    Setting items = create("Items", "Items", false);
    Setting xporbs = create("Xp Orbs", "XPO", false);
    Setting xpbottles = create("Xp Bottles", "Bottles", false);
    Setting pearl = create("Pearls", "Pearls", false);
    Setting red = create("Red", "Red", 120, 0, 255);
    Setting green = create("Green", "Green", 120, 0, 255);
    Setting blue = create("Blue", "Blue", 240, 0, 255);
    Setting boxAlpha = create("BoxAlpha", "BoxAlpha", 100, 0, 255);
    Setting alpha = create("Alpha", "Alpha", 100, 0, 255);

    @Override
    public void render(EventRender event) {
        AxisAlignedBB bb;
        Vec3d interp;
        int i;
        if (this.items.get_value(true)) {
            i = 0;
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityItem) || !(mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.0f);
                RenderGlobal.renderFilledBox(bb, (float)this.red.get_value(1) / 255.0f, (float)this.green.get_value(1) / 255.0f, (float)this.blue.get_value(1) / 255.0f, (float)this.boxAlpha.get_value(1) / 255.0f);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb,  new Color(this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), this.alpha.get_value(1)), 1.0f);
                if (++i < 50) continue;
                break;
            }
        }
        if (this.xporbs.get_value(true)) {
            i = 0;
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityXPOrb) || !(mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.0f);
                RenderGlobal.renderFilledBox(bb, (float)this.red.get_value(1) / 255.0f,(float)this.green.get_value(1) / 255.0f,(float)this.blue.get_value(1) / 255.0f,(float)this.boxAlpha.get_value(1) / 255.0f);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb,  new Color(this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), this.alpha.get_value(1)), 1.0f);
                if (++i < 50) continue;
                break;
            }
        }
        if (this.pearl.get_value(true)) {
            i = 0;
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderPearl) || !(mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.0f);
                RenderGlobal.renderFilledBox(bb,(float)this.red.get_value(1) / 255.0f, (float)this.green.get_value(1) / 255.0f, (float)this.blue.get_value(1) / 255.0f, (float)this.boxAlpha.get_value(1) / 255.0f);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb, new Color(this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), this.alpha.get_value(1)), 1.0f);
                if (++i < 50) continue;
                break;
            }
        }
        if (this.xpbottles.get_value(true)) {
            i = 0;
            for (Entity entity : mc.world.loadedEntityList) {
                if (!(entity instanceof EntityExpBottle) || !(mc.player.getDistanceSq(entity) < 2500.0)) continue;
                interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
                bb = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interp.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interp.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interp.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interp.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interp.z);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.disableDepth();
                GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glLineWidth(1.0f);
                RenderGlobal.renderFilledBox(bb, (float)this.red.get_value(1) / 255.0f,(float)this.green.get_value(1) / 255.0f, (float)this.blue.get_value(1) / 255.0f,(float)this.boxAlpha.get_value(1) / 255.0f);
                GL11.glDisable(2848);
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                RenderUtil.drawBlockOutline(bb,  new Color(this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), this.alpha.get_value(1)), 1.0f);
                if (++i < 50) continue;
                break;
            }
        }
    }

}

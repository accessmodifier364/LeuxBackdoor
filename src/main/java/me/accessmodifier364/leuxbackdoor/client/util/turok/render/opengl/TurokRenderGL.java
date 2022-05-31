/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.vertex.DefaultVertexFormats
 *  org.lwjgl.opengl.GL11
 */
package me.accessmodifier364.leuxbackdoor.client.util.turok.render.opengl;

import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokDisplay;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokMath;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokRect;
import me.accessmodifier364.leuxbackdoor.client.util.turok.mouse.TurokMouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TurokRenderGL {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static TurokRenderGL INSTANCE;
    public TurokDisplay display;
    public TurokMouse mouse;

    public static void init() {
        INSTANCE = new TurokRenderGL();
    }

    public static void init(Object object) {
        if (object instanceof TurokDisplay) {
            TurokRenderGL.INSTANCE.display = (TurokDisplay) object;
        }
        if (object instanceof TurokMouse) {
            TurokRenderGL.INSTANCE.mouse = (TurokMouse) object;
        }
    }

    public static void drawRectOutlineFadingMouse(TurokRect rect, int radius, boolean inverted, Color color) {
        TurokRenderGL.drawRectOutlineFadingMouse(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), radius, inverted, color);
    }

    public static void drawRectOutlineFadingMouse(float x, float y, float w, float h, int radius, boolean inverted, Color color) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        float vx = x - (float) TurokRenderGL.INSTANCE.mouse.getX();
        float vy = y - (float) TurokRenderGL.INSTANCE.mouse.getY();
        float vw = x + w - (float) TurokRenderGL.INSTANCE.mouse.getX();
        float vh = y + h - (float) TurokRenderGL.INSTANCE.mouse.getY();
        int valueInverted = inverted ? 255 : 0;
        TurokRenderGL.prepare(2);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vx * vx + vy + vy) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x, y);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vx * vx + vh + vh) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x, y + h);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vw * vw + vh + vh) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x + w, y + h);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vw * vw + vy + vy) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x + w, y);
        TurokRenderGL.release();
    }

    public static void drawRectSolidFadingMouse(TurokRect rect, int radius, boolean inverted, Color color) {
        TurokRenderGL.drawRectSolidFadingMouse(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), radius, inverted, color);
    }

    public static void drawRectSolidFadingMouse(float x, float y, float w, float h, int radius, boolean inverted, Color color) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        float vx = x - (float) TurokRenderGL.INSTANCE.mouse.getX();
        float vy = y - (float) TurokRenderGL.INSTANCE.mouse.getY();
        float vw = x + w - (float) TurokRenderGL.INSTANCE.mouse.getX();
        float vh = y + h - (float) TurokRenderGL.INSTANCE.mouse.getY();
        int valueInverted = inverted ? 255 : 0;
        TurokRenderGL.prepare(7);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vx * vx + vy + vy) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x, y);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vx * vx + vh + vh) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x, y + h);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vw * vw + vh + vh) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x + w, y + h);
        TurokRenderGL.color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) valueInverted - TurokMath.clamp(TurokMath.sqrt(vw * vw + vy + vy) / ((float) radius / 100.0f), 0.0f, 255.0f));
        TurokRenderGL.addVertex(x + w, y);
        TurokRenderGL.release();
    }

    public static void drawScissor(int x, int y, int w, int h) {
        int calculatedX = x;
        int calculatedY = y;
        int calculatedW = calculatedX + w;
        int calculatedH = calculatedY + h;
        GL11.glScissor(calculatedX * TurokRenderGL.INSTANCE.display.getScaleFactor(), TurokRenderGL.INSTANCE.display.getHeight() - calculatedH * TurokRenderGL.INSTANCE.display.getScaleFactor(), (calculatedW - calculatedX) * TurokRenderGL.INSTANCE.display.getScaleFactor(), (calculatedH - calculatedY) * TurokRenderGL.INSTANCE.display.getScaleFactor());
    }

    public static void drawTexture(float x, float y, float width, float height) {
        TurokRenderGL.prepare(7);
        TurokRenderGL.sewTexture(0, 0);
        TurokRenderGL.addVertex(x, y);
        TurokRenderGL.sewTexture(0, 1);
        TurokRenderGL.addVertex(x, y + height);
        TurokRenderGL.sewTexture(1, 1);
        TurokRenderGL.addVertex(x + width, y + height);
        TurokRenderGL.sewTexture(1, 0);
        TurokRenderGL.addVertex(x + width, y);
        TurokRenderGL.release();
    }

    public static void drawTextureInterpolated(float x, float y, float xx, float yy, float width, float height, float ww, float hh) {
        TurokRenderGL.prepare(7);
        TurokRenderGL.sewTexture(0.0f + xx, 0.0f + hh);
        TurokRenderGL.addVertex(x, y);
        TurokRenderGL.sewTexture(0.0f + xx, 1.0f + hh);
        TurokRenderGL.addVertex(x, y + height);
        TurokRenderGL.sewTexture(1.0f + ww, 1.0f + hh);
        TurokRenderGL.addVertex(x + width, y + height);
        TurokRenderGL.sewTexture(1.0f + ww, 0.0f + hh);
        TurokRenderGL.addVertex(x + width, y);
        TurokRenderGL.release();
    }

    public static void drawUpTriangle(float x, float y, float width, float height, int offsetX) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        TurokRenderGL.prepare(6);
        TurokRenderGL.addVertex(x + width, y + height);
        TurokRenderGL.addVertex(x + width, y);
        TurokRenderGL.addVertex(x - (float) offsetX, y);
        TurokRenderGL.release();
    }

    public static void drawDownTriangle(float x, float y, float width, float height, int offsetX) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        TurokRenderGL.prepare(6);
        TurokRenderGL.addVertex(x, y);
        TurokRenderGL.addVertex(x, y + height);
        TurokRenderGL.addVertex(x + width + (float) offsetX, y + height);
        TurokRenderGL.release();
    }

    public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, float num_segments) {
        TurokRenderGL.prepare(4);
        int i = (int) (num_segments / (360.0f / start_angle)) + 1;
        while ((float) i <= num_segments / (360.0f / end_angle)) {
            double previousAngle = Math.PI * 2 * (double) (i - 1) / (double) num_segments;
            double angle = Math.PI * 2 * (double) i / (double) num_segments;
            TurokRenderGL.addVertex(cx, cy);
            TurokRenderGL.addVertex((double) cx + Math.cos(angle) * (double) r, (double) cy + Math.sin(angle) * (double) r);
            TurokRenderGL.addVertex((double) cx + Math.cos(previousAngle) * (double) r, (double) cy + Math.sin(previousAngle) * (double) r);
            ++i;
        }
        TurokRenderGL.release();
    }

    public static void drawArc(float x, float y, float radius) {
        TurokRenderGL.drawArc(x, y, radius, 0.0f, 360.0f, 40.0f);
    }

    public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, float num_segments) {
        TurokRenderGL.prepare(2);
        int i = (int) (num_segments / (360.0f / start_angle)) + 1;
        while ((float) i <= num_segments / (360.0f / end_angle)) {
            double angle = Math.PI * 2 * (double) i / (double) num_segments;
            TurokRenderGL.addVertex((double) cx + Math.cos(angle) * (double) r, (double) cy + Math.sin(angle) * (double) r);
            ++i;
        }
        TurokRenderGL.release();
    }

    public static void drawArcOutline(float x, float y, float radius) {
        TurokRenderGL.drawArcOutline(x, y, radius, 0.0f, 360.0f, 40.0f);
    }

    public static void drawOutlineRect(float x, float y, float width, float height) {
        float calculatedX = x + 0.5f;
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        TurokRenderGL.lineSize(1.0f);
        TurokRenderGL.prepare(2);
        TurokRenderGL.addVertex(calculatedX, y);
        TurokRenderGL.addVertex(calculatedX, y + height + 0.5f);
        TurokRenderGL.addVertex(x + width, y + height);
        TurokRenderGL.addVertex(x + width, y);
        TurokRenderGL.release();
    }

    public static void drawOutlineRect(int x, int y, int width, int height) {
        TurokRenderGL.drawOutlineRect((float) x, (float) y, (float) width, (float) height);
    }

    public static void drawOutlineRect(TurokRect rect) {
        TurokRenderGL.drawOutlineRect((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight());
    }

    public static void drawOutlineRoundedRect(float x, float y, float width, float height, float radius, float dR, float dG, float dB, float dA, float line_width) {
        TurokRenderGL.drawRoundedRect(x, y, width, height, radius);
        TurokRenderGL.color(dR, dG, dB, dA);
        TurokRenderGL.drawRoundedRect(x + line_width, y + line_width, width - line_width * 2.0f, height - line_width * 2.0f, radius);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        TurokRenderGL.drawArc(x + width - radius, y + height - radius, radius, 0.0f, 90.0f, 16.0f);
        TurokRenderGL.drawArc(x + radius, y + height - radius, radius, 90.0f, 180.0f, 16.0f);
        TurokRenderGL.drawArc(x + radius, y + radius, radius, 180.0f, 270.0f, 16.0f);
        TurokRenderGL.drawArc(x + width - radius, y + radius, radius, 270.0f, 360.0f, 16.0f);
        TurokRenderGL.prepare(4);
        TurokRenderGL.addVertex(x + width - radius, y);
        TurokRenderGL.addVertex(x + radius, y);
        TurokRenderGL.addVertex(x + width - radius, y + radius);
        TurokRenderGL.addVertex(x + width - radius, y + radius);
        TurokRenderGL.addVertex(x + radius, y);
        TurokRenderGL.addVertex(x + radius, y + radius);
        TurokRenderGL.addVertex(x + width, y + radius);
        TurokRenderGL.addVertex(x, y + radius);
        TurokRenderGL.addVertex(x, y + height - radius);
        TurokRenderGL.addVertex(x + width, y + radius);
        TurokRenderGL.addVertex(x, y + height - radius);
        TurokRenderGL.addVertex(x + width, y + height - radius);
        TurokRenderGL.addVertex(x + width - radius, y + height - radius);
        TurokRenderGL.addVertex(x + radius, y + height - radius);
        TurokRenderGL.addVertex(x + width - radius, y + height);
        TurokRenderGL.addVertex(x + width - radius, y + height);
        TurokRenderGL.addVertex(x + radius, y + height - radius);
        TurokRenderGL.addVertex(x + radius, y + height);
        TurokRenderGL.release();
    }

    public static void drawRoundedRect(TurokRect rect, float size) {
        TurokRenderGL.drawRoundedRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), size);
    }

    public static void drawSolidRect(float x, float y, float width, float height) {
        TurokRenderGL.enableState(3042);
        TurokRenderGL.blendFunc(770, 771);
        TurokRenderGL.prepare(7);
        TurokRenderGL.addVertex(x, y);
        TurokRenderGL.addVertex(x, y + height);
        TurokRenderGL.addVertex(x + width, y + height);
        TurokRenderGL.addVertex(x + width, y);
        TurokRenderGL.release();
    }

    public static void drawSolidRect(int x, int y, int width, int height) {
        TurokRenderGL.drawSolidRect((float) x, (float) y, (float) width, (float) height);
    }

    public static void drawSolidRect(TurokRect rect) {
        TurokRenderGL.drawSolidRect((float) rect.getX(), (float) rect.getY(), (float) rect.getWidth(), (float) rect.getHeight());
    }

    public static void drawLine3D(double x, double y, double z, double x1, double y1, double z1, int r, int g, int b, int a, float line) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        TurokRenderGL.lineSize(line);
        TurokRenderGL.enableState(2848);
        TurokRenderGL.hint(3154, 4354);
        GlStateManager.disableDepth();
        TurokRenderGL.enableState(34383);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(x, y, z).color(r, g, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        TurokRenderGL.disableState(2848);
        GlStateManager.enableDepth();
        TurokRenderGL.disableState(34383);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void autoScale() {
        TurokRenderGL.pushMatrix();
        TurokRenderGL.translated(TurokRenderGL.INSTANCE.display.getScaledWidth(), TurokRenderGL.INSTANCE.display.getScaledHeight());
        TurokRenderGL.scaled(0.5f, 0.5f, 0.5f);
        TurokRenderGL.popMatrix();
    }

    public static void color(float r, float g, float b, float a) {
        GL11.glColor4f(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    public static void color(double r, double g, double b, double a) {
        GL11.glColor4f((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
    }

    public static void color(int r, int g, int b, int a) {
        GL11.glColor4f((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
    }

    public static void color(float r, float g, float b) {
        GL11.glColor3f(r / 255.0f, g / 255.0f, b / 255.0f);
    }

    public static void color(double r, double g, double b) {
        GL11.glColor3f((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
    }

    public static void color(int r, int g, int b) {
        GL11.glColor3f((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
    }

    public static void prepare(int mode) {
        GL11.glBegin(mode);
    }

    public static void release() {
        GL11.glEnd();
    }

    public static void sewTexture(float s, float t, float r) {
        GL11.glTexCoord3f(s, t, r);
    }

    public static void sewTexture(float s, float t) {
        GL11.glTexCoord2f(s, t);
    }

    public static void sewTexture(float s) {
        GL11.glTexCoord1f(s);
    }

    public static void sewTexture(double s, double t, double r) {
        TurokRenderGL.sewTexture((float) s, (float) t, (float) r);
    }

    public static void sewTexture(double s, double t) {
        TurokRenderGL.sewTexture((float) s, (float) t);
    }

    public static void sewTexture(double s) {
        TurokRenderGL.sewTexture((float) s);
    }

    public static void sewTexture(int s, int t, int r) {
        TurokRenderGL.sewTexture((float) s, (float) t, (float) r);
    }

    public static void sewTexture(int s, int t) {
        TurokRenderGL.sewTexture((float) s, (float) t);
    }

    public static void sewTexture(int s) {
        TurokRenderGL.sewTexture((float) s);
    }

    public static void addVertex(float x, float y, float z) {
        TurokRenderGL.sewTexture(x, y, z);
    }

    public static void addVertex(float x, float y) {
        GL11.glVertex2f(x, y);
    }

    public static void addVertex(double x, double y, double z) {
        TurokRenderGL.addVertex((float) x, (float) y, (float) z);
    }

    public static void addVertex(double x, double y) {
        TurokRenderGL.addVertex((float) x, (float) y);
    }

    public static void addVertex(int x, int y, int z) {
        TurokRenderGL.addVertex((float) x, (float) y, (float) z);
    }

    public static void addVertex(int x, int y) {
        TurokRenderGL.addVertex((float) x, (float) y);
    }

    public static void hint(int target, int target1) {
        GL11.glHint(target, target1);
    }

    public static void translated(float x, float y, float z) {
        GL11.glTranslated(x, y, z);
    }

    public static void translated(float x, float y) {
        TurokRenderGL.translated(x, y, 0.0f);
    }

    public static void scaled(float scaledPosX, float scaledPosY, float scaledPosZ) {
        GL11.glScaled(scaledPosX, scaledPosY, scaledPosZ);
    }

    public static void lineSize(float width) {
        GL11.glLineWidth(width);
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void enableState(int glState) {
        GL11.glEnable(glState);
    }

    public static void disableState(int glState) {
        GL11.glDisable(glState);
    }

    public static void blendFunc(int glState, int glState1) {
        GL11.glBlendFunc(glState, glState1);
    }

    public static void prepareOverlay() {
        TurokRenderGL.pushMatrix();
        TurokRenderGL.enableState(3553);
        TurokRenderGL.enableState(3042);
        GlStateManager.enableBlend();
        TurokRenderGL.popMatrix();
    }

    public static void releaseOverlay() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    public static void prepare3D(float size) {
        TurokRenderGL.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(size);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public static void release3D() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }
}


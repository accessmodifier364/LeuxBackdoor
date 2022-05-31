
package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.particles;

import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiParticles {

    private static final float SPEED = 0.1f;
    private final List<Particle> particleList = new ArrayList<>();

    public GuiParticles(int initAmount) {
        this.addParticles(initAmount);
    }

    public void addParticles(int n) {
        for (int i = 0; i < n; ++i) {
            this.particleList.add(Particle.generateParticle());
        }
    }

    public void deleteAllParticles() {
        this.particleList.clear();
    }

    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }

    public void tick(int delta) {

        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }

    }

    private void drawLine(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        GL11.glColor4f(f5, f6, f7, f8);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth((float) ModLoader.get_setting_manager().get_setting_with_tag("Particles", "LineWidth").get_value(1.0f));
        GL11.glBegin(1);
        GL11.glVertex2f(f, f2);
        GL11.glVertex2f(f3, f4);

        GL11.glEnd();
    }

    public void render() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        if (Minecraft.getMinecraft().currentScreen == null) {
            return;
        }

        for (Particle particle : particleList) {
            //Particle Size
            //particle.setSize(Leux.get_setting_manager().get_setting_with_tag("Particles", "Size").get_value(1));
            particle.setSize(particle.getSize());

            //Particle Color
            GL11.glColor4f(ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Red").get_value(1) / 255.0f, ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Green").get_value(1) / 255.0f, ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Blue").get_value(1) / 255.0f, particle.getAlpha() / 255.0f);

            //PRUEBA XD
            GL11.glEnable(GL11.GL_POINT_SMOOTH);

            GL11.glPointSize(particle.getSize());

            GL11.glBegin(0);
            GL11.glVertex2f(particle.getX(), particle.getY());
            GL11.glEnd();

            int Width = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
            int Height = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;

            float nearestDistance = 0.0f;
            Particle nearestParticle = null;

            //Line Distance
            int dist = ModLoader.get_setting_manager().get_setting_with_tag("Particles", "LineDistance").get_value(1);

            for (Particle particle1 : this.particleList) {
                float distance = particle.getDistanceTo(particle1);
                if (distance > dist || ModLoader.get_setting_manager().get_setting_with_tag("Particles", "ParticlesAround").get_value(true) && (distance(Width, Height, particle.getX(), particle.getY()) > dist && distance(Width, Height, particle1.getX(), particle1.getY()) > dist) || nearestDistance > 0.0f && distance > nearestDistance) {
                    continue;
                }
                nearestDistance = distance;
                nearestParticle  = particle1;
            }

            if (nearestParticle == null) {
                continue;
            }
            float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));

            //Line Color
                drawLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Red").get_value(1) / 255.0f, ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Green").get_value(1) / 255.0f, ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Blue").get_value(1) / 255.0f, alpha);

        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
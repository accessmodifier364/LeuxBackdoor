package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.particles.GuiParticles;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class NewGui extends GuiScreen {

    public static final GuiParticles particleSystem = new GuiParticles(100);

    public static ArrayList<Panel> panels;
    
    public NewGui() {
        NewGui.panels = new ArrayList<>();

        //POS OF THE CATEGORY
        int panelX = 5;
        final int panelY = 5;

        //SIZE OF THE CATEGORY
        final int panelWidth = 90;
        final int panelHeight = 15;


        for (final Category c : Category.values()) {
            if (!c.is_hidden()) {
                NewGui.panels.add(new Panel(c.get_name(), panelX, panelY, panelWidth, panelHeight, c));
                panelX += 100;
            }
        }
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        for (final Panel p : NewGui.panels) {
            p.updatePosition(mouseX, mouseY);
            p.drawScreen(mouseX, mouseY, partialTicks);
            for (final Component comp : p.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }

        if (ModLoader.get_module_manager().get_module_with_tag("Particles").is_active()) {
            particleSystem.tick(ModLoader.get_setting_manager().get_setting_with_tag("Particles", "Ticks").get_value(1));
            particleSystem.render();
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Panel p : NewGui.panels) {
            if (p.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                p.setDragging(true);
                p.dragX = mouseX - p.getX();
                p.dragY = mouseY - p.getY();
            } else if (p.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                p.setOpen(!p.isOpen());
            } else {
                if (!p.isOpen() || p.getComponents().isEmpty()) {
                    continue;
                }
                for (final Component component : p.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Panel panel : NewGui.panels) {
            if (panel.isOpen() && !panel.getComponents().isEmpty() && keyCode != 1) {
                for (final Component component : panel.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }
    
    public void onGuiClosed() {
        ModLoader.get_hack_manager().get_module_with_tag("GuiModule").set_active(false);
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Panel p : NewGui.panels) {
            p.setDragging(false);
            if (p.isOpen() && !p.getComponents().isEmpty()) {
                for (final Component component : p.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }
    
    public static ArrayList<Panel> getPanels() {
        return NewGui.panels;
    }
    
    public static Panel getPanelByName(final String name) {
        Panel panel = null;
        for (final Panel p : getPanels()) {
            if (p.title.equalsIgnoreCase(name)) {
                panel = p;
            }
        }
        return panel;
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
}

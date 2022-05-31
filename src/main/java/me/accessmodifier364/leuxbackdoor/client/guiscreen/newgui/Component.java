package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui;

import net.minecraft.client.Minecraft;

public class Component {
    protected Minecraft mc;
    
    public Component() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public void renderComponent() {
    }
    
    public void updateComponent(final int mouseX, final int mouseY) {
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public void keyTyped(final char typedChar, final int key) {
    }
    
    public void closeAllSub() {
    }
    
    public void setOff(final int newOff) {
    }
    
    public int getHeight() {
        return 0;
    }

}

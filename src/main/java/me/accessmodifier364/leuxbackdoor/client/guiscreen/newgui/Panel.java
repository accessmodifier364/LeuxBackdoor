package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.items.ModuleButton;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class Panel {
    protected Minecraft mc;
    public ArrayList<Component> components;
    public String title;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean isSettingOpen;
    private boolean isDragging;
    private boolean open;
    public int dragX;
    public int dragY;
    public Category cat;
    public int tY;
    private final ResourceLocation arrow = new ResourceLocation("custom/arrow.png");
    private final ResourceLocation arrow2 = new ResourceLocation("custom/arrow2.png");

    public Panel(final String title, final int x, final int y, final int width, final int height, final Category cat) {
        this.mc = Minecraft.getMinecraft();
        this.components = new ArrayList<>();
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dragX = 0;
        this.isSettingOpen = true;
        this.isDragging = false;
        this.open = true;
        this.cat = cat;
        this.tY = this.height;
        for (final Module modules : ModLoader.get_hack_manager().get_modules_with_category(cat)) {
            if (modules.get_category() == cat) {
                final ModuleButton modButton = new ModuleButton(modules, this, this.tY);
                this.components.add(modButton);
                this.tY += 15;
            }
        }
        this.refresh();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {

        // Category Color
        Draw.draw_rect(this.x - 2, this.y, this.x + this.width + 2, this.y + this.height, ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Red").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Green").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Blue").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Alpha").get_value(1));

        FontUtil.drawText(this.title, (float)(this.x + 4), (float)(this.y + this.height / 2 - FontUtil.getFontHeight() / 2), -1);

        if (this.open) {
            mc.getTextureManager().bindTexture(this.arrow2);
            ModuleButton.drawCompleteImage((float) (this.x + this.width - 10), (float) (this.y + this.height / 2 - FontUtil.getFontHeight() / 2), 10, 10, false, this.open);
        } else {
            mc.getTextureManager().bindTexture(this.arrow);
            ModuleButton.drawCompleteImage((float) (this.x + this.width - 10), (float) (this.y + this.height / 2 - FontUtil.getFontHeight() / 2), 10, 10, false, this.open);
        }
        if (this.open && !this.components.isEmpty()) {
            for (final Component component : this.components) {
                component.renderComponent();
            }
        }

    }
    
    public void refresh() {
        int off = this.height;
        for (final Component comp : this.components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }
    
    public boolean isWithinHeader(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        this.scroll();
    }
    
    public void scroll() {
        final int scrollWheel = Mouse.getDWheel();
        for (final Panel panels : NewGui.panels) {
            if (scrollWheel < 0) {
                panels.setY(panels.getY() - 8);
            }
            else {
                if (scrollWheel <= 0) {
                    continue;
                }
                panels.setY(panels.getY() + 8);
            }
        }
    }
    
    public void closeAllSetting() {
        for (final Component component : this.components) {
            component.closeAllSub();
        }
    }
    
    public ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setDragging(final boolean drag) {
        this.isDragging = drag;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setX(final int newX) {
        this.x = newX;
    }
    
    public void setY(final int newY) {
        this.y = newY;
    }
    
    public Category getCategory() {
        return this.cat;
    }
}

package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.Component;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import org.lwjgl.input.Keyboard;

public class KeybindComponent extends Component
{
    private boolean isBinding;
    private ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private String points;
    private float tick;
    
    public KeybindComponent(final ModuleButton parent, final int offset) {
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.points = ".";
        this.tick = 0.0f;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {

        Draw.draw_rect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, 0, 0, 0, 160);

        if (this.isBinding) {
            this.tick += 0.5f;
            Draw.draw_rect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() - 2 + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Red").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Green").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Blue").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Alpha").get_value(1));
            FontUtil.drawText("Press a Key" + ChatFormatting.GRAY + " " + this.points, (float)(this.parent.parent.getX() + 4), (float)(this.parent.parent.getY() + this.offset + 4), -1);
        } else {
            //Draw.draw_rect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() - 2 + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, 0, 0, 0, 160);
            FontUtil.drawText("Bind" + ChatFormatting.GRAY + " " + this.parent.mod.get_bind("string"), (float)(this.parent.parent.getX() + 4), (float)(this.parent.parent.getY() + this.offset + 4), -1);
        }
        if (this.isBinding) {
            if (this.tick >= 15.0f) {
                this.points = "..";
            }
            if (this.tick >= 30.0f) {
                this.points = "...";
            }
            if (this.tick >= 45.0f) {
                this.points = ".";
                this.tick = 0.0f;
            }
        }
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            this.isBinding = !this.isBinding;
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        if (this.isBinding) {
            if (Keyboard.isKeyDown(211)) {
                this.parent.mod.set_bind(0);
                this.isBinding = false;
            }
            else if (Keyboard.isKeyDown(14)) {
                this.parent.mod.set_bind(0);
                this.isBinding = false;
            }
            else {
                this.parent.mod.set_bind(key);
                this.isBinding = false;
            }
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 15;
    }
}

package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.Component;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class ModeComponent extends Component
{
    private Setting op;
    private ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;
    
    public ModeComponent(final Setting op, final ModuleButton parent, final int offset) {
        this.op = op;
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {

        //BLACK BACKGROUND
        Draw.draw_rect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, 0, 0, 0, 160);

        Draw.draw_rect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset, this.parent.parent.getX() - 2 + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Red").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Green").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Blue").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Alpha").get_value(1));

        FontUtil.drawText(this.op.get_name() + " " + ChatFormatting.GRAY + this.op.get_current_value().toUpperCase(), (float)(this.parent.parent.getX() + 4), (float)(this.parent.parent.getY() + this.offset + 4), -1);
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            final int maxIndex = this.op.get_values().size() - 1;
            ++this.modeIndex;
            if (this.modeIndex > maxIndex) {
                this.modeIndex = 0;
            }
            this.op.set_current_value(this.op.get_values().get(this.modeIndex));
        }

        if (this.isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.isOpen()) {
            final int maxIndex = this.op.get_values().size() - 1;
            --this.modeIndex;
            if (this.modeIndex < 0) {
                this.modeIndex = maxIndex;
            }
            this.op.set_current_value(this.op.get_values().get(this.modeIndex));
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 15;
    }
}

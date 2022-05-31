package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.Component;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntegerComponent extends Component {
    private final Setting set;
    private final ModuleButton parent;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double sliderWidth;
    
    public IntegerComponent(final Setting value, final ModuleButton button, final int offset) {
        this.dragging = false;
        this.set = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = offset;
    }
    
    @Override
    public void setOff (final int newOff) {
        this.offset = newOff;
    }
    
    @Override
    public void renderComponent() {

        //IntegerButtom background
        Draw.draw_rect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, 0, 0, 0, 160);

        Draw.draw_rect(this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset,  this.parent.parent.getX() + 2 + (int)(this.sliderWidth > 0 ? this.sliderWidth : 0), this.parent.parent.getY() + this.offset + 15, ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Red").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Green").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Blue").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Alpha").get_value(1));

        FontUtil.drawText(this.set.get_name() + ChatFormatting.GRAY + " " + this.set.get_value(1), (float)(this.parent.parent.getX() + 4), (float)(this.parent.parent.getY() + this.offset + 3), -1);

    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        final double diff = Math.min(this.parent.parent.getWidth(), Math.max(0, mouseX - this.x));
        final int min = this.set.get_min(1);
        final int max = this.set.get_max(1);
        this.sliderWidth = (this.parent.parent.getWidth() - 4) * ((double) this.set.get_value(1) - min) / (max - min);
        if (this.dragging) {
            if (diff == 0.0) {
                this.set.set_value(this.set.get_min(1));
            } else {
                final int newValue = (int)roundToPlace((diff / this.parent.parent.getWidth()) * (max - min) + min, 2);
                this.set.set_value(newValue);
            }
        }
    }

    private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.isOpen()) {
            this.dragging = true;
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        this.dragging = false;
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.x && x < this.x + 90 && y > this.y && y < this.y + 15;
    }

}

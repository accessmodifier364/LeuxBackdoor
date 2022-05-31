package me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.items;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.Component;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.Panel;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.FontUtil;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.Draw;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class ModuleButton extends Component {
    private final ArrayList<Component> subcomponents;
    public Module mod;
    public Panel parent;
    public int offset;
    private int progress;
    private boolean open;
    private boolean hovered;

    private float calculateRotation(float angle) {
        if ((angle %= 360.0F) >= 180.0F) {
            angle -= 360.0F;
        }

        if (angle < -180.0F) {
            angle += 360.0F;
        }

        return angle;
    }

    public ModuleButton(final Module mod, final Panel parent, final int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<>();
        this.open = false;
        int opY = offset + 15;
        for (final Setting settings : ModLoader.get_setting_manager().get_settings_with_hack(mod)) {
            switch (settings.get_type()) {
                case "button":
                    this.subcomponents.add(new BooleanComponent(settings, this, opY));
                    opY += 15;
                    break;
                case "integerslider":
                    this.subcomponents.add(new IntegerComponent(settings, this, opY));
                    opY += 15;
                    break;
                case "doubleslider":
                    this.subcomponents.add(new DoubleComponent(settings, this, opY));
                    opY += 15;
                    break;
                default:
                    if (!settings.get_type().equals("combobox")) {
                        continue;
                    }
                    this.subcomponents.add(new ModeComponent(settings, this, opY));
                    opY += 15;
                    break;
            }
        }
        this.subcomponents.add(new KeybindComponent(this, opY));
    }
    
    @Override
    public void setOff(final int newOff) {
        this.offset = newOff;
        int opY = this.offset + 15;
        for (final Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 15;
        }
    }

    @Override
    public void renderComponent() {

        //Modules color as category color
        if (this.mod.is_active()) {
            Draw.draw_rect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 15 + this.offset, ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Red").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Green").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Blue").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Alpha").get_value(1));
        } else {
            Draw.draw_rect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 15 + this.offset, 40, 40, 40, 128);
        }

        //When i put the mouse over a module
        if (this.hovered) {
            //Module Color
            if (this.mod.is_active()) {
                Draw.draw_rect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 15 + this.offset, ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Red").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Green").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Blue").get_value(1), ModLoader.get_setting_manager().get_setting_with_tag("GuiModule", "Alpha").get_value(1) - 35);
            } else {
                Draw.draw_rect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 15 + this.offset, 40, 40, 40, 128 - 35);
            }
        }

        FontUtil.drawText(this.mod.get_name(), (float)(this.parent.getX() + 4), (float)(this.parent.getY() + this.offset + 4), -1);

        //Gear Logo
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
//            RenderMethods.glColor(new Color(0.0F, 0.0F, 100.0F, 1.0F));
        mc.getTextureManager().bindTexture(new ResourceLocation("custom/gear.png"));
        GlStateManager.translate(parent.getX() + parent.getWidth() - 6.7F, (parent.getY() + offset) + 7.7F - 0.3F, 0.0F);
        GlStateManager.rotate(calculateRotation((float) this.progress), 0.0F, 0.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(-5, -5, 0.0F, 0.0F, 10, 10, 10, 10, 10.0F, 10.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        if (!open) return;

        ++progress;

        if (this.open && !this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.renderComponent();
            }
        }
    }
    
    @Override
    public void closeAllSub() {
        this.open = false;
    }

    public static void drawCompleteImage(float posX, float posY, int width, int height, boolean isGear, boolean isOpened) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);

        //ROTATE THE IMAGE
        if (!isGear) {
            if (isOpened) {
                /*
                GL11.glRotatef(Leux.get_setting_manager().get_setting_with_tag("GuiModule", "ArrowAngle").get_value(1), 0.5f, 0.0f, 0.0f);
                GL11.glTranslatef(posX + Leux.get_setting_manager().get_setting_with_tag("GuiModule", "X").get_value(1), posY + Leux.get_setting_manager().get_setting_with_tag("GuiModule", "Y").get_value(1), 0.0f);
                */
            } else {
                //GL11.glRotatef(0.0f, 0.5f, 0.0f, 0.0f);
            }
        } else {
            if (isOpened) {
                //HACER QUE LA RUEDA DE VUELTAS
            }
        }

        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);

        GL11.glVertex3f((float) width, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float) width, 0.0f, 0.0f);

        GL11.glEnd();
        GL11.glPopMatrix();
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return 15 * (this.subcomponents.size() + 1);
        }
        return 15;
    }
    
    @Override
    public void updateComponent(final int mouseX, final int mouseY) {
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (final Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (this.isMouseOnButton(mouseX, mouseY) && button == 0) {
            this.mod.toggle();
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 1.0f));
        }
        if (this.isMouseOnButton(mouseX, mouseY) && button == 1) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ITEM_PICKUP, 1.0f));
            if (!this.isOpen()) {
                this.parent.closeAllSetting();
                this.setOpen(true);
            }
            else {
                this.setOpen(false);
            }
            this.parent.refresh();
        }
        for (final Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }
    
    @Override
    public void keyTyped(final char typedChar, final int key) {
        for (final Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (final Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }
    
    public boolean isMouseOnButton(final int x, final int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 15 + this.offset;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
}

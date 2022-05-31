package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventFirstPerson;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.PlayerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class ViewModel extends Module {
    public ViewModel() {
        super(Category.render);
        this.name = "ViewModel";
        this.description = "anti chad";
    }

    Setting custom_fov = create("FOV", "FOVSlider", 130, 10, 170);
    Setting cancelEating = create("No Eat", "NoEat", false);
    Setting pyroEat = create("Pyro Eat", "PyroEat", false);
    Setting xRight = create("Right X", "RightX", 0.0, -50.0, 50.0);
    Setting yRight = create("Right Y", "RightY", 0.0, -50.0, 50.0);
    Setting zRight = create("Right Z", "RightZ", -50.0, -50.0, 50.0);
    Setting scaleRight = create("Scale Right", "ScaleRight", 7.0, 0.0, 50.0);
    Setting rotateRightX = create("Rotate Right X", "RotateRightX", 0, -360, 360);
    Setting rotateRightY = create("Rotate Right Y", "RotateRightY", 0, -360, 360);
    Setting rotateRightZ = create("Rotate Right Z", "RotateRightZ", 0, -360, 360);
    Setting scaleLeft = create("Scale Left", "ScaleLeft", 7.0, 0.0, 50.0);
    Setting xLeft = create("Left X", "LeftX", 0.0, -50.0, 50.0);
    Setting yLeft = create("Left Y", "LeftY", 0.0, -50.0, 50.0);
    Setting zLeft = create("Left Z", "LeftZ", -50.0, -50.0, 50.0);
    Setting rotateLeftX = create("Rotate Left X", "RotateLeftX", 0, -360, 360);
    Setting rotateLeftY = create("Rotate Left Y", "RotateLeftY", 0, -360, 360);
    Setting rotateLeftZ = create("Rotate Left Z", "RotateLeftZ", 0, -360, 360);

    private float fov;

    @EventHandler
    public final Listener<EventFirstPerson> eventListener = new Listener<>(event -> {
        if (event.getHandSide() == EnumHandSide.RIGHT) {
            if (pyroEat.get_value(true) && PlayerUtil.isEating()) {
            event.cancel();
            } else {
                GL11.glTranslatef((float) this.xRight.get_value(1.0) / 100.0f, (float) this.yRight.get_value(1.0) / 100.0f, (float) this.zRight.get_value(1.0) / 100.0f);
                GL11.glRotatef((float) this.rotateRightX.get_value(1.0), 1.0f, 0.0f, 0.0f);
                GL11.glRotatef((float) this.rotateRightY.get_value(1.0), 0.0f, 1.0f, 0.0f);
                GL11.glRotatef((float) this.rotateRightZ.get_value(1.0), 0.0f, 0.0f, 1.0f);
                GL11.glScalef((float) this.scaleRight.get_value(1.0) / 10.0f, (float) this.scaleRight.get_value(1.0) / 10.0f, (float) this.scaleRight.get_value(1.0) / 10.0f);
            }
        } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GL11.glTranslatef((float)this.xLeft.get_value(1.0) / 100.0f, (float)this.yLeft.get_value(1.0) / 100.0f, (float)this.zLeft.get_value(1.0) / 100.0f);
            GL11.glRotatef((float)this.rotateLeftX.get_value(1.0), 1.0f, 0.0f, 0.0f);
            GL11.glRotatef((float)this.rotateLeftY.get_value(1.0), 0.0f, 1.0f, 0.0f);
            GL11.glRotatef((float)this.rotateLeftZ.get_value(1.0), 0.0f, 0.0f, 1.0f);
            GL11.glScalef((float)this.scaleLeft.get_value(1.0) / 10.0f, (float)this.scaleLeft.get_value(1.0) / 10.0f, (float)this.scaleLeft.get_value(1.0) / 10.0f);
        }
    });

    @Override
    protected void enable() {
        this.fov = mc.gameSettings.fovSetting;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    protected void disable() {
        mc.gameSettings.fovSetting = this.fov;
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void update() {
        mc.gameSettings.fovSetting = this.custom_fov.get_value(1);
    }
}
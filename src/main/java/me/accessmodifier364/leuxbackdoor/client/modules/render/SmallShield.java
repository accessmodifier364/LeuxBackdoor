package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class SmallShield extends Module {
    public SmallShield() {
        super(Category.render);
        this.name = "SmallShield";
        this.description = "Makes you offhand lower";
        INSTANCE = this;
    }

    public Setting normalOffset = create("OffNormal", "OffNormal", false);
    public Setting offset = create("Offset", "Offset", 0.7, 0.0, 1.0);
    public Setting offX = create("OffX", "OffX", 0.0, -1.0, 1.0);
    public Setting offY = create("OffY", "OffY", 0.0, -1.0, 1.0);
    public Setting mainX = create("MainX", "MainX", 0.0, -1.0, 1.0);
    public Setting mainY = create("MainY", "MainY", 0.0, -1.0, 1.0);
    private static SmallShield INSTANCE = new SmallShield();

    @Override
    public void update() {
        if (this.normalOffset.get_value(true)) {
            mc.entityRenderer.itemRenderer.equippedProgressOffHand = (float) this.offset.get_value(1.0);
        }
    }

    public static SmallShield getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SmallShield();
        }
        return INSTANCE;
    }
}

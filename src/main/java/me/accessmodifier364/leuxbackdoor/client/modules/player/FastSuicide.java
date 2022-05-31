package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class FastSuicide extends Module {

    public FastSuicide() {
        super(Category.player);
        this.name = "FastSuicide";
        this.description = "be more fast";
    }

    public void enable() {
        mc.player.sendChatMessage("/kill");
        set_disable();
    }

}
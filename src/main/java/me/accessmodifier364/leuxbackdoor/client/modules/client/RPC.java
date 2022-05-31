package me.accessmodifier364.leuxbackdoor.client.modules.client;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.DiscordUtil;

public class RPC extends Module {
    public RPC() {
        super(Category.client);
        this.name = "RPC";
        this.description = "shows discord rpc";
    }

    Setting showip = create("ShowIP", "ShowIP", false);

    @Override
    public void enable() {
        DiscordUtil.start();
    }

    @Override
    public void disable() {
        DiscordUtil.stop();
    }
}
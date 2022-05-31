package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.client.util.OnlineFriends;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.entity.Entity;

public class FriendList extends Pinnable {
    
    public FriendList() {
        super("Friends", "Friends", 1, 0, 0);
    }

    int passes;

    public static ChatFormatting bold = ChatFormatting.BOLD;

    @Override
	public void render() {
		int nl_r = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorR").get_value(1);
		int nl_g = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorG").get_value(1);
        int nl_b = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorB").get_value(1);
        int nl_a = ModLoader.get_setting_manager().get_setting_with_tag("HUD", "ColorA").get_value(1);

        String line1 = bold + "active_gang: ";
        
        passes = 0;

        create_line(line1, this.docking(1, line1), 2, nl_r, nl_g, nl_b, nl_a);
        
        if (!OnlineFriends.getFriends().isEmpty()) {
            for (Entity e : OnlineFriends.getFriends()) {
                passes++;
                create_line(e.getName(), this.docking(1, e.getName()), this.get(line1, "height")*passes, nl_r, nl_g, nl_b, nl_a);
            }
        }

		this.set_width(this.get(line1, "width") + 2);
		this.set_height(this.get(line1, "height") + 2);
	}

}
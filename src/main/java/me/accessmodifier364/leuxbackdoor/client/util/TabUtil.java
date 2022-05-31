package me.accessmodifier364.leuxbackdoor.client.util;

import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class TabUtil {
    public static String get_player_name(final NetworkPlayerInfo info) {
        final String name = (info.getDisplayName() != null) ? info.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(),
                info.getGameProfile().getName());
        if (ModLoader.get_module_manager().get_module_with_tag("HUD").is_active() && ModLoader.get_setting_manager().get_setting_with_tag("Settings", "TabColor").get_value(true)) {
            if (FriendUtil.isFriend(name)) {
                return "\u00A79" + name;
            }
            if (EnemyUtil.isEnemy(name)) {
                return "\u00A7c" + name;
            }
        }
        return name;
    }
}

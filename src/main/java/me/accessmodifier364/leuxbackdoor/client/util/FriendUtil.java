package me.accessmodifier364.leuxbackdoor.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class FriendUtil {

    public static ArrayList<Friend> friends = new ArrayList<>();
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isFriend(String name) {
        try {
            return friends.stream().anyMatch(friend -> friend.username.equalsIgnoreCase(name));
        } catch (Exception ignored) {
            return false;
        }
    }

    public static FriendUtil.Friend get_friend_object(String name) {
        Optional<EntityPlayer> player = mc.world.playerEntities.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();

        return player.map(entityPlayer -> new Friend(entityPlayer.getGameProfile().getName(), entityPlayer.getGameProfile().getId())).orElse(null);
    }

    public static class Friend {
        String username;
        UUID uuid;

        public Friend(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        public String getUsername() {
            return username;
        }

        public UUID getUUID() {
            return uuid;
        }
    }


}
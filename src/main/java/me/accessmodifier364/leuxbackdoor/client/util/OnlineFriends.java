package me.accessmodifier364.leuxbackdoor.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineFriends {

    public static List<Entity> entities = new ArrayList<Entity>();

    static public List<Entity> getFriends() {
        entities.clear();
        entities.addAll(Minecraft.getMinecraft().world.playerEntities.stream().filter(entityPlayer -> FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList()));

        return entities;
    }

}
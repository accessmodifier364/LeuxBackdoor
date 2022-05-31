package me.accessmodifier364.leuxbackdoor.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class EnemyUtil {

    public static ArrayList<Enemy> enemies = new ArrayList<>();
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isEnemy(String name) {
        return enemies.stream().anyMatch(enemy -> enemy.username.equalsIgnoreCase(name));
    }

    public static Enemy get_enemy_object(String name) {
        Optional<EntityPlayer> player = mc.world.playerEntities.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst();

        return player.map(entityPlayer -> new Enemy(entityPlayer.getGameProfile().getName(), entityPlayer.getGameProfile().getId())).orElse(null);
    }

    public static class Enemy {
        String username;
        UUID uuid;

        public Enemy(String username, UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }

        public String getUsername() {
            return username;
        }
    }

}
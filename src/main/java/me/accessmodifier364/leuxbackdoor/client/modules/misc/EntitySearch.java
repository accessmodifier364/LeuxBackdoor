package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;

public class EntitySearch extends Module {
    public EntitySearch() {
        super(Category.misc);
        this.name = "EntitySearch";
        this.description = "sends a message when finds an entity";
    }

    Setting donkeys = create("Donkeys", "Donkeys", true);
    Setting llamas = create("Llamas", "Llamas", true);
    Setting mules = create("Mules", "Mules", true);
    Setting slimes = create("Slimes", "Slimes", false);

    int delay;

    @Override
    public void enable() {
        delay = 0;
    }

    @Override
    public void update() {
        if (delay > 0) {
            --delay;
        }
        if (donkeys.get_value(true)) {
            mc.world.loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityDonkey && delay == 0) {
                    MessageUtil.send_client_message("Found a donkey at: " + Math.round(entity.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity.lastTickPosZ));
                    delay = 200;
                }
                if (llamas.get_value(true)) {
                    mc.world.loadedEntityList.forEach(entity2 -> {
                        if (entity2 instanceof EntityLlama && delay == 0) {
                            MessageUtil.send_client_message("Found a llama at: " + Math.round(entity2.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity2.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity2.lastTickPosZ));
                            delay = 200;
                        }
                        if (mules.get_value(true)) {
                            mc.world.loadedEntityList.forEach(entity3 -> {
                                if (entity3 instanceof EntityMule && delay == 0) {
                                    MessageUtil.send_client_message("Found a mule at: " + Math.round(entity3.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity3.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity3.lastTickPosZ));
                                    delay = 200;
                                }
                                if (slimes.get_value(true)) {
                                    mc.world.loadedEntityList.forEach(entity4 -> {
                                        if (entity4 instanceof EntitySlime && delay == 0) {
                                            MessageUtil.send_client_message("Found a slime at: " + Math.round(entity4.lastTickPosX) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity4.lastTickPosY) + ChatFormatting.GRAY + ", " + ChatFormatting.WHITE + Math.round(entity4.lastTickPosZ));
                                            delay = 200;
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}
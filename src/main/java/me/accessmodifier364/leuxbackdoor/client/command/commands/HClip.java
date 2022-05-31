package me.accessmodifier364.leuxbackdoor.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MathUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class HClip extends Command {

    public HClip() {
        super("hclip", "horizontal instant move");
    }

    public static ChatFormatting red = ChatFormatting.GREEN;
    public static ChatFormatting green = ChatFormatting.RED;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting reset = ChatFormatting.RESET;

    public boolean get_message(String[] message) {
        if (message.length == 1) {
            MessageUtil.send_client_message("use hclip (blocks)");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder blocks = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                blocks.append(word);
            }

            final double l_Number = Double.parseDouble(blocks.toString());
            final Vec3d l_Direction = MathUtil.direction(mc.player.rotationYaw);
            Entity l_Entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;
            assert l_Entity != null;
            l_Entity.setPosition(mc.player.posX + l_Direction.x * l_Number, mc.player.posY, mc.player.posZ + l_Direction.z * l_Number);

            MessageUtil.send_client_message("Teleported you " + blocks.toString() + " blocks forward");
            return true;
        }
        return false;
    }
}
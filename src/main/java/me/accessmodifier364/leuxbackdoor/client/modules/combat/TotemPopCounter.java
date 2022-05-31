package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.HashMap;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super(Category.combat);
		this.name        = "TotemPopCounter";
		this.description = "ez pop";
    }

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<>();
    public static ChatFormatting red = ChatFormatting.RED;public static ChatFormatting green = ChatFormatting.GREEN;public static ChatFormatting bold = ChatFormatting.BOLD;public static ChatFormatting reset = ChatFormatting.RESET;

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.get_packet();
            if (packet.getOpCode() == 35) {
                    Entity entity = packet.getEntity(mc.world);
                    int count = 1;
                    if (totem_pop_counter.containsKey(entity.getName())) {
                        count = totem_pop_counter.get(entity.getName());
                        totem_pop_counter.put(entity.getName(), ++count);
                    } else {
                        totem_pop_counter.put(entity.getName(), count);
                    }
                    if (entity == mc.player) return;

                    if (FriendUtil.isFriend(entity.getName())) {
                        MessageUtil.send_client_message("" + reset + green + bold + entity.getName() + reset + " has popped " + bold + count + reset + " totems, help him lmao.");
                    } else {
                        MessageUtil.send_client_message("" + reset + red + bold + entity.getName() + reset + " has popped " + bold + count + reset + " totems, he is so ez.");
                    }}}});

    @Override
    public void update() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (!totem_pop_counter.containsKey(player.getName())) continue;
            if (player.isDead || player.getHealth() <= 0) {
                int count = totem_pop_counter.get(player.getName());
                totem_pop_counter.remove(player.getName());
                if (player == mc.player) continue;
                if (FriendUtil.isFriend(player.getName())) {
                    MessageUtil.send_client_message("" + reset + green + bold + player.getName() + reset + " died after popping " + bold + count + reset + " totems");
                } else {
                    MessageUtil.send_client_message("" + reset + red + bold + player.getName() + reset + " died after popping " + bold + count + reset + " totems");
                }

            }

        }
    }
}
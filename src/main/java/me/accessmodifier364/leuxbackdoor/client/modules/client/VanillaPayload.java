package me.accessmodifier364.leuxbackdoor.client.modules.client;

import io.netty.buffer.Unpooled;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class VanillaPayload extends Module {

    public VanillaPayload() {
        super(Category.client);
        this.name        = "VanillaPayload";
        this.description = "With this, servers detect u vanilla";
    }

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof FMLProxyPacket && !mc.isSingleplayer()) {
            event.cancel();
        }

        if (event.get_packet() instanceof CPacketCustomPayload) {
            final CPacketCustomPayload packet = (CPacketCustomPayload) event.get_packet();
            if (packet.getChannelName().equalsIgnoreCase("MC|Brand")) {
                packet.data = new PacketBuffer(Unpooled.buffer()).writeString("vanilla");
            }
        }
    });

}

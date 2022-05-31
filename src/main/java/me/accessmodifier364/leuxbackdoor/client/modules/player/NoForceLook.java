package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class NoForceLook extends Module {
    public NoForceLook() {
        super(Category.player);
        this.name = "NoForceLook";
        this.description = "fucking camera";
    }

    @EventHandler
    private Listener<EventPacket.ReceivePacket> receiveListener = new Listener<>(event -> {
        if (mc.player == null)
            return;
        if (event.get_packet() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.get_packet();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
            }
    });
}
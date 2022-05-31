package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;

public class AntiHunger extends Module {
    public AntiHunger() {
        super(Category.player);
        this.name        = "AntiHunger";
        this.description = "anti venezuela module";
    }

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.get_packet();
            packet.onGround = false;
        }
    });

    @Override
    public void update() {
        if (mc.player.isSprinting()) {
            mc.player.setSprinting(false);
        }
    }
}

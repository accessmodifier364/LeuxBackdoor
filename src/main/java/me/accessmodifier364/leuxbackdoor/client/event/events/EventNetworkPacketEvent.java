package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.network.Packet;

public class EventNetworkPacketEvent extends EventCancellable {
    public Packet m_Packet;

    public EventNetworkPacketEvent(Packet p_Packet) {
        super();
        m_Packet = p_Packet;
    }

    public Packet getPacket() {
        return m_Packet;
    }
}

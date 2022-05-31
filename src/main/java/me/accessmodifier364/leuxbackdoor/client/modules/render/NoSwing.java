package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventSwing;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

public class NoSwing extends Module {

    public NoSwing() {
        super(Category.render);
		this.name        = "NoSwing";
		this.description = "ill swing u init fam";
    }

    Setting mode = create("Mode", "NoSwingMode", "Full", combobox("Full", "Packet", "Offhand"));

    @EventHandler
	public Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketAnimation && mode.in("Packet")) {
			event.cancel();
		}
    });
    
    @EventHandler
	public Listener<EventSwing> listener_ = new Listener<>(event -> {
        if (mode.in("Full")) {
            event.cancel();
        } else if (mode.in("Offhand")) {
            event.hand = EnumHand.OFF_HAND;
        }
    });



}
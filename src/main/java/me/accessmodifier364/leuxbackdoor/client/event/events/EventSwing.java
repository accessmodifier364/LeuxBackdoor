package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.util.EnumHand;

public class EventSwing extends EventCancellable {

    public EnumHand hand;

    public EventSwing(EnumHand hand) {
        super();
        this.hand = hand;
    }

}
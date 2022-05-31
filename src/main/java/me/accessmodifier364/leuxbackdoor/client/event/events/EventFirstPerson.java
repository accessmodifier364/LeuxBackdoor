package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.util.EnumHandSide;

public class EventFirstPerson extends EventCancellable {
    private final EnumHandSide handSide;

    public EventFirstPerson(EnumHandSide handSide) {
        this.handSide = handSide;
    }

    public EnumHandSide getHandSide() {
        return this.handSide;
    }
}
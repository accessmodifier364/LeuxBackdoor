package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;

public class EventMotionUpdate extends EventCancellable {

    public int stage;

    public EventMotionUpdate(int stage) {
        super();
        this.stage = stage;
    }

    public int get_stage() {
        return this.stage;
    }
}
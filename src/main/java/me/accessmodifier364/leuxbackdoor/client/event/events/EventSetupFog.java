package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;

public class EventSetupFog extends EventCancellable {

    public int start_coords;
    public float partial_ticks;

    public EventSetupFog(int coords, float ticks) {
        start_coords = coords;
        partial_ticks = ticks;
    }

}
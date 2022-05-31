package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EventTotemPop extends EventCancellable {

    private final Entity entity;

    public EventTotemPop(EntityPlayer entity) {
        this.entity = entity;
    }

    public Entity get_entity() {
        return this.entity;
    }

}
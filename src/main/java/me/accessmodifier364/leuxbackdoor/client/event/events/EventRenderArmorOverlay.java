package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderArmorOverlay extends EventCancellable {
    public EntityLivingBase entity;

    public EventRenderArmorOverlay(EntityLivingBase entity) {
        this.entity = entity;
    }
}

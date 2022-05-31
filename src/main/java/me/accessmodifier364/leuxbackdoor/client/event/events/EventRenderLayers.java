package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderLayers extends EventCancellable {
    private final EntityLivingBase entityLivingBase;

    private final LayerRenderer layerRenderer;

    private float HeadPitch;

    public EventRenderLayers(EntityLivingBase entityLivingBase, LayerRenderer layerRenderer, float headPitch) {
        this.entityLivingBase = entityLivingBase;
        this.layerRenderer = layerRenderer;
        this.HeadPitch = headPitch;
    }

    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }

    public LayerRenderer getLayerRenderer() {
        return this.layerRenderer;
    }

    public float GetHeadPitch() {
        return this.HeadPitch;
    }

    public void SetHeadPitch(float p_Pitch) {
        this.HeadPitch = p_Pitch;
    }
}

package me.accessmodifier364.leuxbackdoor.client.mixins;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventMotionUpdate;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventMove;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventSwing;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// External.


@Mixin(value = EntityPlayerSP.class)
public class MixinEntitySP extends MixinEntity {

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void move(MoverType type, double x, double y, double z, CallbackInfo info) {
        EventMove event = new EventMove(type, x, y, z);
        EventClientBus.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            super.move(type, event.get_x(), event.get_y(), event.get_z());
            info.cancel();
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void OnPreUpdateWalkingPlayer(CallbackInfo p_Info) {

        EventMotionUpdate l_Event = new EventMotionUpdate(0);
        EventClientBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("RETURN"), cancellable = true)
    public void OnPostUpdateWalkingPlayer(CallbackInfo p_Info) {

        EventMotionUpdate l_Event = new EventMotionUpdate(1);
        EventClientBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }

    @Inject(method = "swingArm", at = @At("RETURN"), cancellable = true)
    public void swingArm(EnumHand p_Hand, CallbackInfo p_Info) {

        EventSwing l_Event = new EventSwing(p_Hand);
        EventClientBus.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
            p_Info.cancel();

    }

}
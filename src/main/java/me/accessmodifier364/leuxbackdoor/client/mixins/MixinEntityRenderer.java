package me.accessmodifier364.leuxbackdoor.client.mixins;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventSetupFog;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer {
    @Redirect(method = "orientCamera", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;rayTraceBlocks(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/RayTraceResult;"))

    public RayTraceResult orientCamera(WorldClient world, Vec3d start, Vec3d end) {
        return ModLoader.get_hack_manager().get_module_with_tag("NoRender").is_active() && ModLoader.get_setting_manager().get_setting_with_tag("NoRender", "CameraClip").get_value(true) ? null : world.rayTraceBlocks(start, end);
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    public void setupFog(int startCoords, float partialTicks, CallbackInfo p_Info) {
        EventSetupFog event = new EventSetupFog(startCoords, partialTicks);
        EventClientBus.EVENT_BUS.post(event);
        if (event.isCancelled()) {
            return;
        }
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    private void hurtCameraEffect(final float ticks, final CallbackInfo info) {
        if (ModLoader.get_hack_manager().get_module_with_tag("NoRender").is_active() && ModLoader.get_setting_manager().get_setting_with_tag("NoRender", "NoHurtCam").get_value(true)) {
            info.cancel();
        }
    }

    @ModifyArg(method = {"renderWorldPass(IFJ)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;setupTerrain(Lnet/minecraft/entity/Entity;DLnet/minecraft/client/renderer/culling/ICamera;IZ)V"))
    public boolean isPlayerSpectator(final boolean b) {
        return ModLoader.get_setting_manager().get_setting_with_tag("Freecam", "CaveRender").get_value(true);
    }
}
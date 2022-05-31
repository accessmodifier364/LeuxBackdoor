package me.accessmodifier364.leuxbackdoor.client.mixins;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.toasts.GuiToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {GuiToast.class})
public class MixinGuiToast {
    @Inject(method = {"drawToast"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void drawToastHook(ScaledResolution resolution, CallbackInfo info) {
        /*if (Leux.get_hack_manager().get_module_with_tag("NoRender").is_active() && Leux.get_setting_manager().get_setting_with_tag("NoRender", "Advancements").get_value(true)) {
            info.cancel();
        }*/
    }
}


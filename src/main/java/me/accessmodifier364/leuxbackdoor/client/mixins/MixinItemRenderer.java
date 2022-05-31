package me.accessmodifier364.leuxbackdoor.client.mixins;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventFirstPerson;
import me.accessmodifier364.leuxbackdoor.client.modules.render.SmallShield;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    private boolean injection;

    public MixinItemRenderer() {
        this.injection = true;
    }

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Inject(method = {"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            if (hand == EnumHand.MAIN_HAND) {
                if (offset.is_active() && player.getHeldItemMainhand() != ItemStack.EMPTY) {
                    xOffset = (float) offset.mainX.get_value(1.0);
                    yOffset = (float) offset.mainY.get_value(1.0);
                }
            } else if (!offset.normalOffset.get_value(true) && offset.is_active() && player.getHeldItemOffhand() != ItemStack.EMPTY) {
                xOffset = (float) offset.offX.get_value(1.0);
                yOffset = (float) offset.offY.get_value(1.0);
            }
            this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            this.injection = true;
        }
    }

    @Redirect(method = {"renderArmFirstPerson"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 0))
    public void translateHook(float x, float y, float z) {
        SmallShield offset = SmallShield.getINSTANCE();
        boolean shiftPos = Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getHeldItemMainhand() != ItemStack.EMPTY && offset.is_active();
        GlStateManager.translate((float) (x + (shiftPos ? offset.mainX.get_value(1.0) : 0.0f)), (float) (y + (shiftPos ? offset.mainY.get_value(1.0) : 0.0f)), z);
    }

    @Inject(method = {"transformSideFirstPerson"}, at = {@At(value = "HEAD")})
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo ci) {
        EventFirstPerson event = new EventFirstPerson(hand);
        EventClientBus.EVENT_BUS.post(event);
    }

    @Inject(method = {"transformEatFirstPerson"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo ci) {
        EventFirstPerson event = new EventFirstPerson(hand);
        EventClientBus.EVENT_BUS.post(event);
        if (ModLoader.get_hack_manager().get_module_with_tag("ViewModel").is_active() && ModLoader.get_setting_manager().get_setting_with_tag("ViewModel", "NoEat").get_value(true)) {
            ci.cancel();
        }
    }

    @Inject(method = {"transformFirstPerson"}, at = {@At(value = "HEAD")})
    public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo ci) {
        EventFirstPerson event = new EventFirstPerson(hand);
        EventClientBus.EVENT_BUS.post(event);
    }
}

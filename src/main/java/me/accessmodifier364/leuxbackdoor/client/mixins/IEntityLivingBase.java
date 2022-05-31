package me.accessmodifier364.leuxbackdoor.client.mixins;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = {EntityLivingBase.class})
public interface IEntityLivingBase {
    @Invoker(value = "getArmSwingAnimationEnd")
    int getArmSwingAnimationEnd();
}


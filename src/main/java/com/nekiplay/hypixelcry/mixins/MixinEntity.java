package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.EntityMove;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {
    @Inject(method = "moveEntity", at = @At("HEAD"))
    private void moveEntity(double x, double y, double z, CallbackInfo ci) {
        if ((Object) this instanceof EntityLiving) {
            EntityMove entityMove = new EntityMove((EntityLiving) (Object)this, new Vec3(x, y, z));
            MinecraftForge.EVENT_BUS.post(entityMove);
        }
    }
}

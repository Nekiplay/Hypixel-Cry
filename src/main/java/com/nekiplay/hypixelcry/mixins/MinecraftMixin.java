package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.EntityMove;
import com.nekiplay.hypixelcry.events.MouseClick;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    public void rightClick(CallbackInfo ci) {
        MouseClick entityMove = new MouseClick.Right();
        MinecraftForge.EVENT_BUS.post(entityMove);
        if (entityMove.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    public void leftClick(CallbackInfo ci) {
        MouseClick entityMove = new MouseClick.Left();
        MinecraftForge.EVENT_BUS.post(entityMove);
        if (entityMove.isCanceled()) {
            ci.cancel();
        }
    }
}

package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.events.world.TickEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class, priority = 1002)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo ci) {
        HypixelCry.EVENT_BUS.post(TickEvent.Pre.get());
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo info) {
        HypixelCry.EVENT_BUS.post(TickEvent.Post.get());
    }
}

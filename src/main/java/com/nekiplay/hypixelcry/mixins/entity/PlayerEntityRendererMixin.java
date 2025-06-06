package com.nekiplay.hypixelcry.mixins.entity;

import com.nekiplay.hypixelcry.utils.Rotations;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    // Rotations

    @Inject(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At("RETURN"))
    private void updateRenderState$rotations(AbstractClientPlayerEntity abstractClientPlayerEntity, PlayerEntityRenderState playerEntityRenderState, float f, CallbackInfo ci) {
        if (Rotations.rotating && abstractClientPlayerEntity == mc.player) {
            playerEntityRenderState.bodyYaw = Rotations.serverYaw;
            playerEntityRenderState.pitch = Rotations.serverPitch;
        }
    }
}
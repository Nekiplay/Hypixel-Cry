package com.nekiplay.hypixelcry.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLoginNetworkHandler.class)
public class MixinClientLoginNetworkHandlerMixin {

    @ModifyExpressionValue(method = "onSuccess", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/ClientBrandRetriever;getClientModName()Ljava/lang/String;", remap = false))
    private String getClientModName(String original) {
        return "vanilla";
    }
}
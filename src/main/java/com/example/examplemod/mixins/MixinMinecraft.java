package com.example.examplemod.mixins;

import com.example.examplemod.events.AttackEntity;
import com.example.examplemod.events.LeftClick;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "startGame", at = @At("HEAD"))
    private void preinit(CallbackInfo ci) {
        System.out.println("Hypixel Cry Mixin: Preinit");
    }   
}

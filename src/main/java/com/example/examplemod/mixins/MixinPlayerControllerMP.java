package com.example.examplemod.mixins;

import com.example.examplemod.events.AttackEntity;
import com.example.examplemod.events.WindowClick;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
    @Inject(method = "windowClick", at = @At("HEAD"))
    private void windowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn, CallbackInfoReturnable<ItemStack> cir) {
        if (MinecraftForge.EVENT_BUS.post(new WindowClick(windowId, slotId, mouseButtonClicked, mode, playerIn))) {
            cir.cancel();
        }
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void attackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new AttackEntity(playerIn, targetEntity))) {
            ci.cancel();
        }
    }
}

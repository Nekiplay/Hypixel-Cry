package com.nekiplay.hypixelcry.mixins.entity;

import com.mojang.authlib.GameProfile;
import com.nekiplay.hypixelcry.events.MotionUpdateEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    @Unique
    float hypixelcry$serverYaw = 0f;
    @Unique
    float hypixelcry$serverPitch = 0f;
    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"))
    public void onUpdateWalkingPlayerPRE(CallbackInfo ci) {
        MotionUpdateEvent event = new MotionUpdateEvent(this.rotationYaw, this.rotationPitch);
        MinecraftForge.EVENT_BUS.post(event);
        this.hypixelcry$serverYaw = event.yaw;
        this.hypixelcry$serverPitch = event.pitch;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", opcode = Opcodes.GETFIELD))
    public float onUpdateWalkingPlayerYaw(EntityPlayerSP instance) {
        return this.hypixelcry$serverYaw;
    }

    @Redirect(method = "onUpdateWalkingPlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", opcode = Opcodes.GETFIELD))
    public float onUpdateWalkingPlayerPitch(EntityPlayerSP instance) {
        return this.hypixelcry$serverPitch;
    }
}
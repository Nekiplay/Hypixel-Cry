package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.events.world.SpawnParticleEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class MixinWorld {
    @Inject(method = "setBlockState(Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z", at = @At("HEAD"), cancellable = true)
    private void onSetBlockState(BlockPos pos, IBlockState newState, int flags, CallbackInfoReturnable<Boolean> cir) {
        BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(pos);
        blockUpdateEvent.newState = newState;
        if (Main.mc.theWorld != null) {
            blockUpdateEvent.oldState = Main.mc.theWorld.getBlockState(pos);
        }
        else {
            blockUpdateEvent.oldState = Blocks.air.getDefaultState();
        }
        MinecraftForge.EVENT_BUS.post(blockUpdateEvent);
        if (blockUpdateEvent.isCanceled()) {
            cir.cancel();
        }
    }

    @Inject(method = "spawnParticle(Lnet/minecraft/util/EnumParticleTypes;ZDDDDDD[I)V", at = @At("HEAD"), cancellable = true)
    private void onSpawnParticle(EnumParticleTypes particleType, boolean p_175682_2_, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int[] p_175682_15_, CallbackInfo ci) {
        SpawnParticleEvent spawnParticleEvent = new SpawnParticleEvent(particleType);
        spawnParticleEvent.position = new Vec3(xCoord, yCoord, zCoord);
        spawnParticleEvent.offset = new Vec3(xOffset, yOffset, zOffset);
        MinecraftForge.EVENT_BUS.post(spawnParticleEvent);
        if (spawnParticleEvent.isCanceled()) {
            ci.cancel();
        }
    }
}

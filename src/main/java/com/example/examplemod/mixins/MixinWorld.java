package com.example.examplemod.mixins;

import com.example.examplemod.Main;
import com.example.examplemod.events.world.BlockUpdateEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
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
        BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent();
        blockUpdateEvent.pos = pos;
        blockUpdateEvent.newState = newState;
        blockUpdateEvent.oldState = Main.mc.theWorld.getBlockState(pos);
        MinecraftForge.EVENT_BUS.post(blockUpdateEvent);
    }
}

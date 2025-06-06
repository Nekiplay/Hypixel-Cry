package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.world.BlockUpdateCallback;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    private void onSetBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
        ActionResult result = BlockUpdateCallback.EVENT.invoker().update(pos, cir.getReturnValue(), state);

        if(result == ActionResult.FAIL) {
            cir.setReturnValue(cir.getReturnValue());
            cir.cancel();
        }
    }
}
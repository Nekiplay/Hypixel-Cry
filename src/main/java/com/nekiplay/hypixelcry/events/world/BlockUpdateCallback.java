package com.nekiplay.hypixelcry.events.world;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public interface BlockUpdateCallback {
    Event<BlockUpdateCallback> EVENT = EventFactory.createArrayBacked(BlockUpdateCallback.class,
            (listeners) -> (pos, old, current) -> {
                for (BlockUpdateCallback listener : listeners) {
                    ActionResult result = listener.update(pos, old, current);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult update(BlockPos pos, BlockState old, BlockState current);
}
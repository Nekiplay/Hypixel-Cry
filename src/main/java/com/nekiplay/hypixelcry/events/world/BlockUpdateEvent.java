package com.nekiplay.hypixelcry.events.world;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class BlockUpdateEvent {
    public static final Event<BlockUpdateCallback> EVENT = EventFactory.createArrayBacked(
            BlockUpdateCallback.class,
            (listeners) -> (event) -> {
                for (BlockUpdateCallback listener : listeners) {
                    ActionResult result = listener.update(event);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    private final BlockPos pos;
    private final BlockState old;
    private final BlockState current;

    public BlockUpdateEvent(BlockPos pos, BlockState old, BlockState current) {
        this.pos = pos;
        this.old = old;
        this.current = current;
    }

    public BlockPos getBlockPos() {
        return pos;
    }

    public BlockState getOld() {
        return old;
    }

    public BlockState getNew() {
        return current;
    }

    public interface BlockUpdateCallback {
        ActionResult update(BlockUpdateEvent event);
    }
}
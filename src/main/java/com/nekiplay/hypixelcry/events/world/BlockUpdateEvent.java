package com.nekiplay.hypixelcry.events.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockUpdateEvent extends Event {
    public BlockPos pos;
    public IBlockState oldState;
    public IBlockState newState;

    public BlockUpdateEvent(BlockPos pos) {
        this.pos = pos;
    }
}

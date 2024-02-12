package com.nekiplay.hypixelcry.events;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BlockClick extends Event {
    public BlockPos pos;

    public BlockClick(BlockPos pos) {
        this.pos = pos;
    }
}

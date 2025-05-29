package com.nekiplay.hypixelcry.utils;

import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface IChunkProviderClient {
    LongHashMap<Chunk> chunkMapping();

    List<Chunk> chunkListing();
}
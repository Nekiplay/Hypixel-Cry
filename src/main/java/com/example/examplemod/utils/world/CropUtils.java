package com.example.examplemod.utils.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class CropUtils {
    public int GetGrowAge(Block block, IBlockState state) {
        int age = -1;
        if (block.equals(Blocks.wheat) || block.equals(Blocks.carrots) || block.equals(Blocks.potatoes)) {
            age = state.getValue(BlockCrops.AGE);
        }
        else if (block.equals(Blocks.nether_wart)) {
            age = state.getValue(BlockNetherWart.AGE);
        }
        else if (block.equals(Blocks.cocoa)) {
            age = state.getValue(BlockCocoa.AGE);
        }
        return age;
    }

    public boolean IsFullGrow(int age, Block block) {
        if (block.equals(Blocks.wheat) || block.equals(Blocks.carrots) || block.equals(Blocks.potatoes)) {
            return age == 7;
        }
        else if (block.equals(Blocks.nether_wart)) {
            return age == 3;
        }
        else if (block.equals(Blocks.cocoa)) {
            return age == 2;
        }
        return false;
    }
}

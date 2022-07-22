package com.example.examplemod.utils;

import com.example.examplemod.Main;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class ExposedBlock {
    private Block state1 = null;
    private Block state2 = null;
    private Block state3 = null;
    private Block state4 = null;
    private Block state5 = null;
    private Block state6 = null;

    public ExposedBlock(BlockPos pos) {
        state1 = Main.mc.thePlayer.getEntityWorld().getBlockState(pos.add(1, 0, 0)).getBlock();
        state2 = Main.mc.thePlayer.getEntityWorld().getBlockState(pos.add(-1, 0, 0)).getBlock();

        state3 = Main.mc.thePlayer.getEntityWorld().getBlockState(pos.add(0, 1, 0)).getBlock();
        state4 = Main.mc.thePlayer.getEntityWorld().getBlockState(pos.add(0, -1, 0)).getBlock();
        state5 = Main.mc.thePlayer.getEntityWorld().getBlockState(pos.add(0, 0, 1)).getBlock();
        state6 = Main.mc.thePlayer.getEntityWorld().getBlockState(pos.add(0, 0, -1)).getBlock();
    }
    public boolean IsExposed() {
        if (state1 == Blocks.air) {
            return true;
        }
        else if (state2 == Blocks.air) {
            return true;
        }
        else if (state3 == Blocks.air) {
            return true;
        }
        else if (state4 == Blocks.air) {
            return true;
        }
        else if (state5 == Blocks.air) {
            return true;
        }
        else if (state6 == Blocks.air) {
            return true;
        }
        return false;
    }

    public boolean IsNotExposed() {
        if (state1 != Blocks.air && state2 != Blocks.air && state3 != Blocks.air && state4 != Blocks.air && state5 != Blocks.air && state6 != Blocks.air) {
            return true;
        }
        return false;
    }
}

package com.nekiplay.hypixelcry.features.macros;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

import static com.nekiplay.hypixelcry.Main.mc;

public class GhostBlocks {
    public void enable() {
        EntityPlayerSP player = mc.thePlayer;
        if (player != null && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.theWorld.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof BlockChest || block instanceof BlockEnderChest) {

            }
            else {
                mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
            }
        }
    }
}
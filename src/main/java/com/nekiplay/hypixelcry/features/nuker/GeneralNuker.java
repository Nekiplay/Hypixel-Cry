package com.nekiplay.hypixelcry.features.nuker;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class GeneralNuker {
    public boolean isBlockToBreak(IBlockState blockState, BlockPos pos) {
        return false;
    }

    private double horizontal_distance = 5.4;
    private double vertical_distance = 7.5;
    public void SetDistance(double horizontal, double vertical) {
        horizontal_distance = horizontal;
        vertical_distance = vertical;
    }
    public ArrayList<BlockPos> getBlocks() {
        ArrayList<BlockPos> temp = new ArrayList<>();
        double r = horizontal_distance + 1;
        BlockPos playerPos = mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 0, 0);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(r, vertical_distance + 1, r), playerPos.subtract(new Vec3i(r, vertical_distance + 1, r)));
        for (BlockPos blockPos : blocks) {
            IBlockState block = mc.theWorld.getBlockState(blockPos);
            if (block.getBlock() != Blocks.air && isBlockToBreak(block, blockPos)) {
                temp.add(blockPos);
            }
        }
        return temp;
    }

    public BlockPos getClosestBlock(ArrayList<BlockPos> blocks) {
        Vec3 playerPosition = mc.thePlayer.getPositionVector();
        double minDistance = Double.MAX_VALUE;
        BlockPos closestBlock = null;

        for (BlockPos block : blocks) {
            double distance = block.distanceSq(playerPosition.xCoord, playerPosition.yCoord, playerPosition.zCoord);

            if (distance < minDistance && distance < horizontal_distance * horizontal_distance) {
                closestBlock = block;
                minDistance = distance;
            }
        }

        if (closestBlock == null || closestBlock.getY() > playerPosition.yCoord + vertical_distance || closestBlock.getY() < playerPosition.yCoord - vertical_distance) {
            return null;
        }

        return closestBlock;
    }
}
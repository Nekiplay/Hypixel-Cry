package com.nekiplay.hypixelcry.features.nuker;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class GeneralNuker {
    private double horizontalDistance = 5.4;
    private double verticalDistance = 7.5;

    public boolean isBlockToBreak(IBlockState blockState, BlockPos pos) {
        return false;
    }

    public void setDistance(double horizontal, double vertical) {
        this.horizontalDistance = horizontal;
        this.verticalDistance = vertical;
    }

    public List<BlockPos> getBlocks() {
        List<BlockPos> blocks = new ArrayList<>();
        double radius = horizontalDistance + 1;
        BlockPos playerPos = mc.thePlayer.getPosition();

        Iterable<BlockPos> area = BlockPos.getAllInBox(
                playerPos.add(radius, verticalDistance + 1, radius),
                playerPos.subtract(new Vec3i(radius, verticalDistance + 1, radius))
        );

        for (BlockPos blockPos : area) {
            IBlockState block = mc.theWorld.getBlockState(blockPos);
            if (block.getBlock() != Blocks.air && isBlockToBreak(block, blockPos)) {
                blocks.add(blockPos);
            }
        }

        return blocks;
    }

    public BlockPos getClosestBlock(List<BlockPos> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return null;
        }

        Vec3 playerPosition = mc.thePlayer.getPositionVector();
        double minDistance = Double.MAX_VALUE;
        BlockPos closestBlock = null;

        for (BlockPos block : blocks) {
            double distance = block.distanceSq(
                    playerPosition.xCoord,
                    playerPosition.yCoord,
                    playerPosition.zCoord
            );

            if (distance < minDistance && distance < horizontalDistance * horizontalDistance) {
                closestBlock = block;
                minDistance = distance;
            }
        }

        if (closestBlock == null ||
                closestBlock.getY() > playerPosition.yCoord + verticalDistance ||
                closestBlock.getY() < playerPosition.yCoord - verticalDistance) {
            return null;
        }

        return closestBlock;
    }
}
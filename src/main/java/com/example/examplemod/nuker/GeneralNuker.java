package com.example.examplemod.nuker;

import com.example.examplemod.Main;
import com.example.examplemod.utils.ExposedBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.util.ArrayList;

public class GeneralNuker {
    private boolean isExposed = true;
    private boolean isNotExposed = true;
    private final ArrayList<Block> mining_blocks = new ArrayList<>();

    public void changeExposedConfig(boolean isExposed, boolean isNotExposed) {
        this.isExposed = isExposed;
        this.isNotExposed = isNotExposed;
    }
    public boolean isBlockToBreak(Block block) {
        return mining_blocks.contains(block);
    }
    public void clearBlocksToBreak() {
        mining_blocks.clear();
    }
    public void addBlockToBreak(Block block) {
        mining_blocks.add(block);
    }

    public void removeBlockToBreak(Block block) {
        mining_blocks.add(block);
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
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 0, 0);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(r, vertical_distance + 1, r), playerPos.subtract(new Vec3i(r, vertical_distance + 1, r)));
        for (BlockPos blockPos : blocks) {
            Block block = Main.mc.theWorld.getBlockState(blockPos).getBlock();
            if (block != Blocks.bedrock && block != Blocks.air && mining_blocks.contains(block)) {
                ExposedBlock exposedBlock = new ExposedBlock(blockPos);
                if (isExposed && exposedBlock.IsExposed()) {
                    temp.add(blockPos);
                } else if (isNotExposed && exposedBlock.IsNotExposed()) {
                    temp.add(blockPos);
                }
            }
        }
        return temp;
    }

    public BlockPos getClosestBlock(ArrayList<BlockPos> blocks) {
        Vec3 playerPosition = Main.mc.thePlayer.getPositionVector();
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

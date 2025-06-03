package com.nekiplay.hypixelcry.features.nuker;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

import java.util.*;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class GeneralNuker {
    private final Map<BlockPos, Integer> brokenBlocks = new LinkedHashMap<>();
    private double horizontalDistance = 5.4;
    private double verticalDistance = 7.5;
    private BlockPos currentBlockPos;

    public final boolean containsBrokenBlock(BlockPos pos) {
        return brokenBlocks.containsKey(pos);
    }

    public final BlockPos getCurrentBlockPos() {
        return currentBlockPos;
    }

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

    public final void cleanUpOldBrokenBlocks() {
        Iterator<Map.Entry<BlockPos, Integer>> iterator = brokenBlocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            if (entry.getValue() >= 20) {
                iterator.remove();
            } else {
                entry.setValue(entry.getValue() + 20);
            }
        }
    }

    public final void breakBlock(BlockPos pos) {
        if (pos == null) {
            return;
        }

        currentBlockPos = pos;
        mc.thePlayer.sendQueue.addToSendQueue(
                new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        pos,
                        EnumFacing.DOWN
                )
        );
        mc.thePlayer.swingItem();
        brokenBlocks.put(pos, 0);
    }
}
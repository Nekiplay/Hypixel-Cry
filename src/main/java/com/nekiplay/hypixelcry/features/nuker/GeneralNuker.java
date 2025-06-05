package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.features.system.RotationHandler;
import com.nekiplay.hypixelcry.utils.BlockUtils;
import com.nekiplay.hypixelcry.utils.RaycastUtils;
import com.nekiplay.hypixelcry.utils.helper.RotationConfiguration;
import com.nekiplay.hypixelcry.utils.helper.Target;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;

import java.util.*;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static com.nekiplay.hypixelcry.utils.PlayerUtils.getEyePosition;
import static com.nekiplay.hypixelcry.utils.PlayerUtils.getLookEndPos;

public class GeneralNuker {
    private final Map<BlockPos, Integer> brokenBlocks = new LinkedHashMap<>();
    private double horizontalDistance = 5.4;
    private double verticalDistance = 7.5;
    private BlockPos currentBlockPos;

    public final boolean containsBrokenBlock(BlockPos pos) {
        return brokenBlocks.containsKey(pos);
    }

    private final List<Block> allowedBlocks = new ArrayList<>();

    public void addAllowedBlock(Block block) {
        allowedBlocks.add(block);
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
            if (entry.getValue() >= 20 * 30) {
                iterator.remove();
            } else {
                entry.setValue(entry.getValue() + 20);
            }
        }
    }

    private boolean isPointVisible(Vec3 point, Vec3 eyePosition, float distance, List<Block> ghostBlocks) {
        MovingObjectPosition result = RaycastUtils.rayTraceToBlocks(
                eyePosition,
                getLookEndPos(point, distance),
                ghostBlocks
        );
        return result != null && result.typeOfHit != MovingObjectPosition.MovingObjectType.MISS;
    }

    public final boolean breakBlock(BlockPos pos) {
        if (pos == null) {
            return false;
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
        return true;
    }
}
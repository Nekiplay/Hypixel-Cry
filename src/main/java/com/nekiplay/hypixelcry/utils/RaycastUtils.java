package com.nekiplay.hypixelcry.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.*;

import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class RaycastUtils {
    public static MovingObjectPosition rayTraceToBlocks(Vec3 startVec, Vec3 endVec, List<Block> blocks) {
        return fastRayTrace(startVec, endVec, blocks);
    }

    private static MovingObjectPosition fastRayTrace(Vec3 startVec, Vec3 endVec, List<Block> targetBlocks) {
        // Convert start and end positions to block coordinates
        int startX = (int) Math.floor(startVec.xCoord);
        int startY = (int) Math.floor(startVec.yCoord);
        int startZ = (int) Math.floor(startVec.zCoord);

        int endX = (int) Math.floor(endVec.xCoord);
        int endY = (int) Math.floor(endVec.yCoord);
        int endZ = (int) Math.floor(endVec.zCoord);

        // Check the starting block first
        BlockPos startPos = new BlockPos(startX, startY, startZ);
        IBlockState startState = mc.theWorld.getBlockState(startPos);
        Block startBlock = startState.getBlock();

        if (startBlock.canCollideCheck(startState, false)) {
            MovingObjectPosition startHit = startBlock.collisionRayTrace(mc.theWorld, startPos, startVec, endVec);
            if (startHit != null) {
                return startHit;
            }
        }

        MovingObjectPosition closestHit = null;
        int maxSteps = 200; // Prevent infinite loops

        while (maxSteps-- >= 0) {
            // Check if we've reached the end block
            if (startX == endX && startY == endY && startZ == endZ) {
                return closestHit;
            }

            // Determine which face of the current block we'll exit through
            boolean xDifferent = startX != endX;
            boolean yDifferent = startY != endY;
            boolean zDifferent = startZ != endZ;

            // Calculate exit points for each axis
            double xExit = xDifferent ? (endX > startX ? startX + 1.0 : startX) : 999.0;
            double yExit = yDifferent ? (endY > startY ? startY + 1.0 : startY) : 999.0;
            double zExit = zDifferent ? (endZ > startZ ? startZ + 1.0 : startZ) : 999.0;

            // Calculate ray direction
            double dx = endVec.xCoord - startVec.xCoord;
            double dy = endVec.yCoord - startVec.yCoord;
            double dz = endVec.zCoord - startVec.zCoord;

            // Calculate distance to each exit plane
            double xDist = xDifferent ? (xExit - startVec.xCoord) / dx : 999.0;
            double yDist = yDifferent ? (yExit - startVec.yCoord) / dy : 999.0;
            double zDist = zDifferent ? (zExit - startVec.zCoord) / dz : 999.0;

            // Fix potential -0.0 values that could cause issues
            if (xDist == -0.0) xDist = -1.0E-4;
            if (yDist == -0.0) yDist = -1.0E-4;
            if (zDist == -0.0) zDist = -1.0E-4;

            EnumFacing exitFace;

            // Determine which plane we hit first
            if (xDist < yDist && xDist < zDist) {
                exitFace = endX > startX ? EnumFacing.WEST : EnumFacing.EAST;
                startVec = new Vec3(xExit, startVec.yCoord + dy * xDist, startVec.zCoord + dz * xDist);
            } else if (yDist < zDist) {
                exitFace = endY > startY ? EnumFacing.DOWN : EnumFacing.UP;
                startVec = new Vec3(startVec.xCoord + dx * yDist, yExit, startVec.zCoord + dz * yDist);
            } else {
                exitFace = endZ > startZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                startVec = new Vec3(startVec.xCoord + dx * zDist, startVec.yCoord + dy * zDist, zExit);
            }

            // Move to the next block
            startX = MathHelper.floor_double(startVec.xCoord) - (exitFace == EnumFacing.EAST ? 1 : 0);
            startY = MathHelper.floor_double(startVec.yCoord) - (exitFace == EnumFacing.UP ? 1 : 0);
            startZ = MathHelper.floor_double(startVec.zCoord) - (exitFace == EnumFacing.SOUTH ? 1 : 0);

            BlockPos newPos = new BlockPos(startX, startY, startZ);
            IBlockState newState = mc.theWorld.getBlockState(newPos);
            Block newBlock = newState.getBlock();

            // Check if we hit a block
            boolean shouldCheckBlock = targetBlocks.isEmpty()
                    ? newBlock.canCollideCheck(newState, false)
                    : targetBlocks.contains(newBlock);

            if (shouldCheckBlock) {
                MovingObjectPosition hit = newBlock.collisionRayTrace(mc.theWorld, newPos, startVec, endVec);
                if (hit != null) {
                    return hit;
                }
            }

            // Store the current position as a potential miss
            closestHit = new MovingObjectPosition(
                    MovingObjectPosition.MovingObjectType.MISS,
                    startVec,
                    exitFace,
                    newPos
            );
        }

        return closestHit;
    }
}

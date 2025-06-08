package com.nekiplay.hypixelcry.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class RaycastUtils {
    private static final int MAX_STEPS = 200;
    private static final double MAX_DISTANCE = 999.0;
    private static final double EPSILON = -1.0E-4;

    public static HitResult rayTraceToBlocks(Vec3d startVec, Vec3d endVec, List<Block> blocks) {
        return fastRayTrace(startVec, endVec, new HashSet<>(blocks));
    }

    public static HitResult rayTraceToPos(Vec3d startVec, Vec3d endVec, List<BlockPos> targets) {
        return fastRayTraceToPos(startVec, endVec, new HashSet<>(targets));
    }

    private static HitResult fastRayTraceToPos(Vec3d startVec, Vec3d endVec, Set<BlockPos> targets) {
        BlockPos.Mutable currentPos = new BlockPos.Mutable();
        Vec3d currentVec = startVec;

        // Initialize current block position
        currentPos.set(
                MathHelper.floor(currentVec.x),
                MathHelper.floor(currentVec.y),
                MathHelper.floor(currentVec.z)
        );

        // Check starting block
        BlockState startState = mc.world.getBlockState(currentPos);
        if (startState.isSolidBlock(mc.world, currentPos)) {
            BlockHitResult startHit = startState.getCollisionShape(mc.world, currentPos)
                    .raycast(startVec, endVec, currentPos);
            if (startHit != null) {
                return startHit;
            }
        }

        int stepsLeft = MAX_STEPS;
        BlockPos endPos = new BlockPos(
                MathHelper.floor(endVec.x),
                MathHelper.floor(endVec.y),
                MathHelper.floor(endVec.z)
        );

        while (stepsLeft-- > 0) {
            if (currentPos.equals(endPos)) {
                return createMissResult(currentVec);
            }

            Direction exitFace = calculateExitFace(currentVec, endVec, currentPos);
            if (exitFace == null) break;

            // Move to next block
            currentPos.move(exitFace);
            currentVec = calculateNewPosition(currentVec, endVec, exitFace, currentPos);

            if (targets.contains(currentPos)) {
                BlockHitResult hit = checkBlockHit(currentVec, endVec, currentPos);
                if (hit != null) {
                    return hit;
                }
            }
        }

        return createMissResult(currentVec);
    }

    private static HitResult fastRayTrace(Vec3d startVec, Vec3d endVec, Set<Block> targetBlocks) {
        BlockPos.Mutable currentPos = new BlockPos.Mutable();
        Vec3d currentVec = startVec;

        currentPos.set(
                MathHelper.floor(currentVec.x),
                MathHelper.floor(currentVec.y),
                MathHelper.floor(currentVec.z)
        );

        // Check starting block
        BlockState startState = mc.world.getBlockState(currentPos);
        if (startState.isSolidBlock(mc.world, currentPos)) {
            BlockHitResult startHit = startState.getCollisionShape(mc.world, currentPos)
                    .raycast(startVec, endVec, currentPos);
            if (startHit != null) {
                return startHit;
            }
        }

        int stepsLeft = MAX_STEPS;
        BlockPos endPos = new BlockPos(
                MathHelper.floor(endVec.x),
                MathHelper.floor(endVec.y),
                MathHelper.floor(endVec.z)
        );

        while (stepsLeft-- > 0) {
            if (currentPos.equals(endPos)) {
                return createMissResult(currentVec);
            }

            Direction exitFace = calculateExitFace(currentVec, endVec, currentPos);
            if (exitFace == null) break;

            // Move to next block
            currentPos.move(exitFace);
            currentVec = calculateNewPosition(currentVec, endVec, exitFace, currentPos);

            BlockState state = mc.world.getBlockState(currentPos);
            boolean shouldCheck = targetBlocks.isEmpty()
                    ? state.isSolidBlock(mc.world, currentPos)
                    : targetBlocks.contains(state.getBlock());

            if (shouldCheck) {
                BlockHitResult hit = checkBlockHit(currentVec, endVec, currentPos);
                if (hit != null) {
                    return hit;
                }
            }
        }

        return createMissResult(currentVec);
    }

    private static Direction calculateExitFace(Vec3d currentVec, Vec3d endVec, BlockPos.Mutable pos) {
        double dx = endVec.x - currentVec.x;
        double dy = endVec.y - currentVec.y;
        double dz = endVec.z - currentVec.z;

        double xExit = dx > 0 ? pos.getX() + 1.0 : pos.getX();
        double yExit = dy > 0 ? pos.getY() + 1.0 : pos.getY();
        double zExit = dz > 0 ? pos.getZ() + 1.0 : pos.getZ();

        double xDist = (xExit - currentVec.x) / dx;
        double yDist = (yExit - currentVec.y) / dy;
        double zDist = (zExit - currentVec.z) / dz;

        // Fix potential -0.0 values
        if (xDist == -0.0) xDist = EPSILON;
        if (yDist == -0.0) yDist = EPSILON;
        if (zDist == -0.0) zDist = EPSILON;

        if (xDist < yDist && xDist < zDist) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        } else if (yDist < zDist) {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        } else if (zDist < MAX_DISTANCE) {
            return dz > 0 ? Direction.NORTH : Direction.SOUTH;
        }

        return null;
    }

    private static Vec3d calculateNewPosition(Vec3d currentVec, Vec3d endVec, Direction exitFace, BlockPos.Mutable pos) {
        double dx = endVec.x - currentVec.x;
        double dy = endVec.y - currentVec.y;
        double dz = endVec.z - currentVec.z;

        double xExit = exitFace.getAxis() == Direction.Axis.X ?
                (exitFace.getDirection() == Direction.AxisDirection.POSITIVE ? pos.getX() : pos.getX() + 1.0) :
                currentVec.x;
        double yExit = exitFace.getAxis() == Direction.Axis.Y ?
                (exitFace.getDirection() == Direction.AxisDirection.POSITIVE ? pos.getY() : pos.getY() + 1.0) :
                currentVec.y;
        double zExit = exitFace.getAxis() == Direction.Axis.Z ?
                (exitFace.getDirection() == Direction.AxisDirection.POSITIVE ? pos.getZ() : pos.getZ() + 1.0) :
                currentVec.z;

        double dist = exitFace.getAxis() == Direction.Axis.X ? (xExit - currentVec.x) / dx :
                exitFace.getAxis() == Direction.Axis.Y ? (yExit - currentVec.y) / dy :
                        (zExit - currentVec.z) / dz;

        return new Vec3d(
                currentVec.x + dx * dist,
                currentVec.y + dy * dist,
                currentVec.z + dz * dist
        );
    }

    private static BlockHitResult checkBlockHit(Vec3d startVec, Vec3d endVec, BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return state.getCollisionShape(mc.world, pos).raycast(startVec, endVec, pos);
    }

    private static HitResult createMissResult(Vec3d vec) {
        return new HitResult(vec) {
            @Override
            public Type getType() {
                return Type.MISS;
            }
        };
    }
}

package com.nekiplay.hypixelcry.utils;

import net.minecraft.block.Block;
import net.minecraft.util.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class BlockUtils {

    // Credit: GTC
    public static final Map<EnumFacing, float[]> BLOCK_SIDES = new HashMap<EnumFacing, float[]>() {{
        put(EnumFacing.DOWN, new float[]{0.5f, 0.01f, 0.5f});
        put(EnumFacing.UP, new float[]{0.5f, 0.99f, 0.5f});
        put(EnumFacing.WEST, new float[]{0.01f, 0.5f, 0.5f});
        put(EnumFacing.EAST, new float[]{0.99f, 0.5f, 0.5f});
        put(EnumFacing.NORTH, new float[]{0.5f, 0.5f, 0.01f});
        put(EnumFacing.SOUTH, new float[]{0.5f, 0.5f, 0.99f});
        put(null, new float[]{0.5f, 0.5f, 0.5f}); // Handles the null case
    }};

    public static Vec3 getSidePos(BlockPos block, EnumFacing face) {
        final float[] offset = BLOCK_SIDES.get(face);
        return new Vec3(block.getX() + offset[0], block.getY() + offset[1], block.getZ() + offset[2]);
    }

    public static boolean canSeeSide(BlockPos block, EnumFacing side, List<Block> blocks) {
        return RaycastUtils.canSeePoint(getSidePos(block, side), blocks);
    }

    public static List<Vec3> bestPointsOnBestSide(final BlockPos block, List<Block> blocks) {
        return pointsOnBlockSide(block, getClosestVisibleSide(block, blocks)).stream()
                .filter((point) -> RaycastUtils.canSeePoint(point, blocks))
                .sorted(Comparator.comparingDouble(i -> AngleUtils.getNeededChange(AngleUtils.getPlayerAngle(), AngleUtils.getRotation(i)).getValue()))
                .collect(Collectors.toList());
    }

    public static EnumFacing getClosestVisibleSide(BlockPos block, List<Block> blocks) {
        if (!mc.theWorld.isBlockFullCube(block)) {
            return null;
        }
        final Vec3 eyePos = mc.thePlayer.getPositionEyes(1);
        double dist = Double.MAX_VALUE;
        EnumFacing face = null;
        for (EnumFacing side : BLOCK_SIDES.keySet()) {
            if (side != null && !mc.theWorld.getBlockState(block).getBlock()
                    .shouldSideBeRendered(mc.theWorld, block.offset(side), side)) {
                continue;
            }
            final double distanceToThisSide = eyePos.distanceTo(getSidePos(block, side));
            if (canSeeSide(block, side, blocks) && distanceToThisSide < dist) {
                if (side == null && face != null) {
                    continue;
                }
                dist = distanceToThisSide;
                face = side;
            }
        }
        return face;
    }

    // Credits to GTC <3
    private static List<Vec3> pointsOnBlockSide(final BlockPos block, final EnumFacing side) {
        final Set<Vec3> points = new HashSet<>();

        if (side != null) {
            float[] it = BLOCK_SIDES.get(side);
            for (int i = 0; i < 20; i++) {
                float x = it[0];
                float y = it[1];
                float z = it[2];
                if (x == 0.5f) {
                    x = randomVal();
                }
                if (y == 0.5f) {
                    y = randomVal();
                }
                if (z == 0.5f) {
                    z = randomVal();
                }
                Vec3 point = new Vec3(block).addVector(x, y, z);
                points.add(point);
            }
        } else {
            for (float[] bside : BLOCK_SIDES.values()) {
                for (int i = 0; i < 20; i++) {
                    float x = bside[0];
                    float y = bside[1];
                    float z = bside[2];
                    if (x == 0.5f) {
                        x = randomVal();
                    }
                    if (y == 0.5f) {
                        y = randomVal();
                    }
                    if (z == 0.5f) {
                        z = randomVal();
                    }
                    Vec3 point = new Vec3(block).addVector(x, y, z);
                    points.add(point);
                }
            }
        }
        return new ArrayList<>(points);
    }

    private static float randomVal() {
        return (new Random().nextInt(6) + 2) / 10.0f;
    }


    // Stole from baritoe
    public static long longHash(int x, int y, int z) {
        long hash = 3241;
        hash = 3457689L * hash + x;
        hash = 8734625L * hash + y;
        hash = 2873465L * hash + z;
        return hash;
    }
}
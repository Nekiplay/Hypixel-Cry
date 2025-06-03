package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.pathfinder.helper.BlockStateAccessor;
import com.nekiplay.hypixelcry.pathfinder.movement.CalculationContext;
import com.nekiplay.hypixelcry.pathfinder.movement.MovementHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

    public static BlockPos getBlockLookingAt() {
        return mc.objectMouseOver.getBlockPos();
    }

    public static List<BlockPos> getWalkableBlocksAround(BlockPos playerPos) {
        List<BlockPos> walkableBlocks = new ArrayList<>();
        BlockStateAccessor bsa = new BlockStateAccessor(mc.theWorld);
        int yOffset = MovementHelper.INSTANCE.isBottomSlab(bsa.get(playerPos.getX(), playerPos.getY(), playerPos.getZ())) ? -1 : 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = yOffset; j <= 0; j++) {
                for (int k = -1; k <= 1; k++) {
                    int x = playerPos.getX() + i;
                    int y = playerPos.getY() + j;
                    int z = playerPos.getZ() + k;

                    if (MovementHelper.INSTANCE.canStandOn(bsa, x, y, z, bsa.get(x, y, z)) &&
                            MovementHelper.INSTANCE.canWalkThrough(bsa, x, y + 1, z, bsa.get(x, y + 1, z)) &&
                            MovementHelper.INSTANCE.canWalkThrough(bsa, x, y + 2, z, bsa.get(x, y + 2, z))) {
                        walkableBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return walkableBlocks;
    }

    public static int getBlockStrength(int stateID) {

        switch (stateID) {
            case 57:  // Diamond Block
            case 41:  // Gold Block
            case 152: // Redstone Block
            case 22:  // Lapis Block
            case 133: // Emerald Block
            case 42:  // Iron Block
            case 173: // Coal Block
                return 600;

            case 19:  // Sponge
                return 500;

            case 1:   // Stone - strength of hardstone
                return 50;

            case 16385: // polished diorite
                return 2000;
            case 28707: // gray wool
                return 500;
            case 12323: // light blue wool
                return 1500;
            case 37023: // cyan stained clay
                return 500;

            case 168: // Prismarine
            case 4264: // dark prismrine
            case 8360: // brick prismarine
                return 800;

            case 95:    // opal
            case 160:
            case 16544: // topaz
            case 16479:
                return 3800;
            case 4191:  // amber
            case 4256:
            case 12383: // sapphire
            case 12448:
            case 20575: // jade
            case 20640:
            case 41055: // amethyst
            case 41120:
                return 3000;
            case 8287:  // jasper
            case 8352:
                return 4800;
            case 45151: // aquamarine
            case 45216:
            case 53343: // peridot
            case 53408:
            case 61535: // onyx
            case 61600:
            case 49247: // citrine
            case 49312:
                return 5200;
            case 57504: // ruby
            case 57439:
                return 2300;
            default:
                break;
        }

        return 5000;
    }

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

    public static boolean canSeeSide(Vec3 from, BlockPos block, EnumFacing side, List<Block> blocks) {
        return RaycastUtils.canSeePoint(from, getSidePos(block, side), blocks);
    }

    public static Vec3 getClosestVisibleSidePos(BlockPos block, List<Block> blocks) {
        EnumFacing face = null;
        if (mc.theWorld.isBlockFullCube(block)) {
            final Vec3 eyePos = mc.thePlayer.getPositionEyes(1);
            double dist = Double.MAX_VALUE;
            for (EnumFacing side : BLOCK_SIDES.keySet()) {
                if (side != null && !mc.theWorld.getBlockState(block).getBlock().shouldSideBeRendered(mc.theWorld, block.offset(side), side)) {
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
        }
        final float[] offset = BLOCK_SIDES.get(face);
        return new Vec3(block.getX() + offset[0], block.getY() + offset[1], block.getZ() + offset[2]);
    }

    public static Vec3 getClosestVisibleSidePos(Vec3 from, BlockPos block, List<Block> blocks) {
        EnumFacing face = null;
        if (mc.theWorld.isBlockFullCube(block)) {
            double dist = Double.MAX_VALUE;
            for (EnumFacing side : BLOCK_SIDES.keySet()) {
                if (side != null && !mc.theWorld.getBlockState(block).getBlock().shouldSideBeRendered(mc.theWorld, block.offset(side), side)) {
                    continue;
                }
                final double distanceToThisSide = from.distanceTo(getSidePos(block, side));
                if (canSeeSide(from, block, side, blocks) && distanceToThisSide < dist) {
                    if (side == null && face != null) {
                        continue;
                    }
                    dist = distanceToThisSide;
                    face = side;
                }
            }
        }
        final float[] offset = BLOCK_SIDES.get(face);
        return new Vec3(block.getX() + offset[0], block.getY() + offset[1], block.getZ() + offset[2]);
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
            for (int i = 0; i < 80; i++) {
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
                for (int i = 0; i < 80; i++) {
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
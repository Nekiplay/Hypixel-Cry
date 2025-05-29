package com.nekiplay.hypixelcry.utils;

import com.nekiplay.hypixelcry.Main;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockUtils {
    public static Vec3 bp = null;

    public static ArrayList<BlockPos> getNearestBlocks(ArrayList<Block> blocks, double range)
    {
        ArrayList<BlockPos> blocks2 = new ArrayList<>();
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(range, range, range);
        Iterable<BlockPos> blocks3 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i));
        for (BlockPos block2 : blocks3) {
            Block block3 = Main.mc.theWorld.getBlockState(block2).getBlock();
            if (blocks.contains(block3)) {
                if (!blocks2.contains(block2)) {
                    blocks2.add(block2);
                }
            }
        }
        return blocks2;
    }

    public static ArrayList<Vec3> whereToMineBlock(BlockPos pos) {
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        ArrayList<Vec3> vec3s = new ArrayList<>();
        MovingObjectPosition position = rayTrace(center, 4.5F);
        if (position != null && position.getBlockPos().equals(pos))
            //return position.hitVec;
            vec3s.add(position.hitVec);
        for (int x = 1; x < 5; x++) {
            for (int y = 1; y < 5; y++) {
                for (int z = 1; z < 5; z++) {
                    Vec3 vec = new Vec3(pos.getX() + x / 4.0D - 0.125D, pos.getY() + y / 4.0D - 0.125D, pos.getZ() + z / 4.0D - 0.125D);
                    position = rayTrace(vec, 4.5F);
                    if (position != null) {
                        bp = position.hitVec;
                        if (position.getBlockPos().equals(pos))
                            //return position.hitVec;
                            vec3s.add(position.hitVec);
                    }
                }
            }
        }
        return vec3s;
    }
    public static MovingObjectPosition rayTrace(Vec3 target, float range) {
        Vec3 vec3 = Main.mc.thePlayer.getPositionEyes(1f);
        Vec3 vec31 = getLook(target);
        return fastRayTrace(vec3, vec3.addVector(vec31.xCoord * (double)range, vec31.yCoord * (double)range, vec31.zCoord * (double)range));
    }

    public static Vec3 getLook(Vec3 vec) {
        double diffX = vec.xCoord - Main.mc.thePlayer.posX;
        double diffY = vec.yCoord - (Main.mc.thePlayer.posY + (double)Main.mc.thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - Main.mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        return getVectorForRotation((float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI)), (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0));
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        double f2 = -Math.cos(-pitch * ((float)Math.PI / 180));
        return new Vec3(Math.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI) * f2, Math.sin(-pitch * ((float)Math.PI / 180)), Math.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI) * f2);
    }

    private static MovingObjectPosition fastRayTrace(Vec3 vec31, Vec3 vec32) {
        MovingObjectPosition movingobjectposition;
        int j1;
        int i1;
        int i = (int) Math.floor(vec32.xCoord);
        int j = (int) Math.floor(vec32.yCoord);
        int k = (int) Math.floor(vec32.zCoord);
        int l = (int) Math.floor(vec31.xCoord);
        BlockPos blockpos = new BlockPos(l, i1 = (int) Math.floor(vec31.yCoord), j1 = (int) Math.floor(vec31.zCoord));
        IBlockState iblockstate = Main.mc.theWorld.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.canCollideCheck(iblockstate, false) && (movingobjectposition = block.collisionRayTrace(Main.mc.theWorld, blockpos, vec31, vec32)) != null) {
            return movingobjectposition;
        }
        MovingObjectPosition movingobjectposition2 = null;
        int k1 = 200;
        while (k1-- >= 0) {
            EnumFacing enumfacing;
            if (l == i && i1 == j && j1 == k) {
                return movingobjectposition2;
            }
            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double d0 = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
                d0 = (double)l + 1.0;
            } else if (i < l) {
                d0 = (double)l + 0.0;
            } else {
                flag2 = false;
            }
            if (j > i1) {
                d1 = (double)i1 + 1.0;
            } else if (j < i1) {
                d1 = (double)i1 + 0.0;
            } else {
                flag = false;
            }
            if (k > j1) {
                d2 = (double)j1 + 1.0;
            } else if (k < j1) {
                d2 = (double)j1 + 0.0;
            } else {
                flag1 = false;
            }
            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = vec32.xCoord - vec31.xCoord;
            double d7 = vec32.yCoord - vec31.yCoord;
            double d8 = vec32.zCoord - vec31.zCoord;
            if (flag2) {
                d3 = (d0 - vec31.xCoord) / d6;
            }
            if (flag) {
                d4 = (d1 - vec31.yCoord) / d7;
            }
            if (flag1) {
                d5 = (d2 - vec31.zCoord) / d8;
            }
            if (d3 == -0.0) {
                d3 = -1.0E-4;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d3 < d4 && d3 < d5) {
                enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
            } else if (d4 < d5) {
                enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
            } else {
                enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
            }
            l = MathHelper.floor_double((double)vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            i1 = MathHelper.floor_double((double)vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
            j1 = MathHelper.floor_double((double)vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            blockpos = new BlockPos(l, i1, j1);
            IBlockState iblockstate1 = Main.mc.theWorld.getBlockState(blockpos);
            Block block1 = iblockstate1.getBlock();
            if (block1.canCollideCheck(iblockstate1, false)) {
                MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(Main.mc.theWorld, blockpos, vec31, vec32);
                if (movingobjectposition1 == null) continue;
                return movingobjectposition1;
            }
            movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
        }
        return movingobjectposition2;
    }

    public static MovingObjectPosition rayTraceToChest(Vec3 startVec, Vec3 endVec) {
		MovingObjectPosition result = null;
		Vec3 direction = endVec.subtract(startVec).normalize();
		double distance = startVec.distanceTo(endVec);
		double step = 0.1; // Шаг для проверки
	
		// Список блоков, которые можно "игнорировать" (пропускать)
		List<Block> transparentBlocks = Arrays.asList(
			Blocks.air, Blocks.tallgrass, Blocks.flowing_water, Blocks.water,
			Blocks.flowing_lava, Blocks.lava, Blocks.vine, Blocks.snow_layer, Blocks.stone
		);
	
		for (double d = 0; d <= distance; d += step) {
			Vec3 currentVec = startVec.addVector(
				direction.xCoord * d,
				direction.yCoord * d,
				direction.zCoord * d
			);
			BlockPos pos = new BlockPos(currentVec);
			IBlockState state = Main.mc.theWorld.getBlockState(pos);
	
			// Если нашли сундук
			if (state.getBlock() == Blocks.chest) {
				Vec3 hitVec = new Vec3(
					pos.getX() + 0.5,
					pos.getY() + 0.5,
					pos.getZ() + 0.5
				);
				return new MovingObjectPosition(
					MovingObjectPosition.MovingObjectType.BLOCK,
					hitVec,
					EnumFacing.UP,
					pos
				);
			}
	
			// Если встретили непрозрачный блок (не из списка игнорируемых)
			if (!transparentBlocks.contains(state.getBlock()) && 
				!state.getBlock().isPassable(Main.mc.theWorld, pos)) {
				break;
			}
		}
	
		return result;
	}

    public static MovingObjectPosition fastRayTraceIgnoreBlocks(Vec3 startVec, Vec3 endVec, Block... ignoredBlocks) {
        List<Block> ignored = Arrays.asList(ignoredBlocks);
        MovingObjectPosition result = null;
        Vec3 currentVec = startVec;
        double step = 0.1;
        Vec3 direction = endVec.subtract(startVec).normalize();
        double distance = startVec.distanceTo(endVec);

        for (double d = 0; d < distance; d += step) {
            currentVec = startVec.add(new Vec3(direction.xCoord * d, direction.yCoord * d, direction.zCoord * d));
            BlockPos pos = new BlockPos(currentVec.xCoord, currentVec.yCoord, currentVec.zCoord);
            IBlockState state = Main.mc.theWorld.getBlockState(pos);

            if (!ignored.contains(state.getBlock()) &&
                    state.getBlock() != Blocks.air &&
                    !state.getBlock().isPassable(Main.mc.theWorld, pos)) {
                Vec3 hitVec = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                return new MovingObjectPosition(MovingObjectPosition.MovingObjectType.BLOCK,
                        hitVec, EnumFacing.UP, pos);
            }
        }

        return result;
    }
}

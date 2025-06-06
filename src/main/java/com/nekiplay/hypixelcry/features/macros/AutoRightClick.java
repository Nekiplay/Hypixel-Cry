package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickBlocks;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickOpenFeatures;
import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.features.system.IslandTypeChangeChecker;
import com.nekiplay.hypixelcry.features.system.RotationHandler;
import com.nekiplay.hypixelcry.utils.AngleUtils;
import com.nekiplay.hypixelcry.utils.BlockUtils;
import com.nekiplay.hypixelcry.utils.RaycastUtils;
import com.nekiplay.hypixelcry.utils.helper.Angle;
import com.nekiplay.hypixelcry.utils.helper.RotationConfiguration;
import com.nekiplay.hypixelcry.utils.helper.Target;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static com.nekiplay.hypixelcry.utils.PlayerUtils.getEyePosition;
import static com.nekiplay.hypixelcry.utils.PlayerUtils.getLookEndPos;

public class AutoRightClick {
    private final Map<BlockPos, Integer> openedChests = new LinkedHashMap<>();
    private int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 20 * 5;
    private static final int MAX_REMOVALS_PER_TICK = 20;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (shouldSkipTick(event)) return;

        if (++tickCounter % MAX_REMOVALS_PER_TICK == 0) cleanUpOldChests();
        if (mc.currentScreen == null) handleChestOpening();
    }

    private boolean shouldSkipTick(TickEvent.ClientTickEvent event) {
        return event.phase == TickEvent.Phase.START || mc.theWorld == null ||
                mc.thePlayer == null || !HypixelCry.config.macros.autoRightClick.enabled ||
                !HypixelCry.config.macros.autoRightClick.allowedIslands.contains(IslandTypeChangeChecker.getLastDetected());
    }

    private void cleanUpOldChests() {
        Iterator<Map.Entry<BlockPos, Integer>> iterator = openedChests.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            if (entry.getValue() >= CHEST_COOLDOWN) {
                iterator.remove();
            } else {
                entry.setValue(entry.getValue() + 1);
            }
        }
    }

    public static List<BlockPos> findBlocksNearby(List<Block> blocksToFind, List<BlockPos> blackListed, float radius) {
        List<BlockPos> foundBlocks = new ArrayList<>();

        if (mc == null || mc.thePlayer == null || mc.theWorld == null ||
                blocksToFind == null || blocksToFind.isEmpty()) {
            return foundBlocks;
        }

        EntityPlayer player = mc.thePlayer;
        World world = mc.theWorld;
        BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);
        int radiusInt = (int) Math.ceil(radius);

        int minX = Math.max(playerPos.getX() - radiusInt, -30000000);
        int maxX = Math.min(playerPos.getX() + radiusInt, 30000000);
        int minY = Math.max(0, playerPos.getY() - radiusInt);
        int maxY = Math.min(255, playerPos.getY() + radiusInt);
        int minZ = Math.max(playerPos.getZ() - radiusInt, -30000000);
        int maxZ = Math.min(playerPos.getZ() + radiusInt, 30000000);

        Set<BlockPos> blackListSet = blackListed != null ? new HashSet<>(blackListed) : Collections.emptySet();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = maxY; y >= minY; y--) {
                    BlockPos checkPos = new BlockPos(x, y, z);

                    if (blackListSet.contains(checkPos)) {
                        continue;
                    }

                    Block block = world.getBlockState(checkPos).getBlock();
                    if (blocksToFind.contains(block)) {
                        foundBlocks.add(checkPos);
                    }
                }
            }
        }

        foundBlocks.sort(Comparator.comparingDouble(player::getDistanceSq));
        return foundBlocks;
    }

    private void handleChestOpening() {
        boolean ghostHand = HypixelCry.config.macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.GhostHand);

        List<Block> selectedBlocks = new ArrayList<>();
        List<AutoRightClickBlocks> blocks = HypixelCry.config.macros.autoRightClick.blocks;
        if (blocks.contains(AutoRightClickBlocks.Chest)) {
            selectedBlocks.add(Blocks.chest);
            selectedBlocks.add(Blocks.trapped_chest);
        }
        if (blocks.contains(AutoRightClickBlocks.Lever)) {
            selectedBlocks.add(Blocks.lever);
        }
        if (blocks.contains(AutoRightClickBlocks.Skull)) {
            selectedBlocks.add(Blocks.skull);
        }

        if (selectedBlocks.isEmpty()) {
            return;
        }

        if (HypixelCry.config.macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.AutoLook)) {
            float reachDistance = mc.playerController.getBlockReachDistance();
            float squaredReach = reachDistance * reachDistance;

            List<BlockPos> foundBlocks = findBlocksNearby(
                    selectedBlocks,
                    new ArrayList<>(openedChests.keySet()),
                    reachDistance + 1
            );

            if (!foundBlocks.isEmpty() && !RotationHandler.getInstance().isEnabled()) {
                BlockPos targetBlock = foundBlocks.get(0);
                List<Block> ghostBlocks = ghostHand ? selectedBlocks : Collections.emptyList();
                List<Vec3> points = BlockUtils.bestPointsOnBestSide(targetBlock, ghostBlocks);

                if (points != null && !points.isEmpty()) {
                    // Фильтрация точек
                    Vec3 eyePosition = mc.thePlayer.getPositionEyes(1);
                    points.removeIf(point ->
                            point.squareDistanceTo(eyePosition) > squaredReach ||
                                    !isPointVisible(point, eyePosition, ghostBlocks)
                    );

                    if (!points.isEmpty()) {
                        RotationConfiguration.RotationType rotationType = ghostHand
                                ? RotationConfiguration.RotationType.SERVER
                                : RotationConfiguration.RotationType.CLIENT;

                        RotationHandler.getInstance().easeTo(new RotationConfiguration(
                                new Target(points.get(0)),
                                HypixelCry.config.macros.autoRightClick.rotationTime,
                                rotationType,
                                null
                        ));
                    }
                }
            }
        }

        if (ghostHand) {
            MovingObjectPosition mouseOver = RaycastUtils.rayTraceToBlocks(
                    getEyePosition(),
                    getLookEndPos(mc.playerController.getBlockReachDistance(), true),
                    selectedBlocks
            );
            tryOpenChest(mouseOver);
        } else if (isLookingAt(selectedBlocks)) {
            tryOpenChest(mc.objectMouseOver);
        }
    }

    private boolean isPointVisible(Vec3 point, Vec3 eyePosition, List<Block> ghostBlocks) {
        MovingObjectPosition result = RaycastUtils.rayTraceToBlocks(
                eyePosition,
                getLookEndPos(point, mc.playerController.getBlockReachDistance()),
                ghostBlocks
        );
        return result != null && result.typeOfHit != MovingObjectPosition.MovingObjectType.MISS;
    }

    private boolean isLookingAt(List<Block> blocks) {
        return mc.objectMouseOver != null &&
                mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                blocks.contains(mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock());
    }

    private void tryOpenChest(MovingObjectPosition mouseOver) {
        if (mouseOver == null || mouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK ||
                openedChests.containsKey(mouseOver.getBlockPos())) {
            return;
        }

        simulateHumanClick(mouseOver);
    }

    private void simulateHumanClick(MovingObjectPosition mop) {
        if (mop == null || mop.getBlockPos() == null) return;

        boolean success = mc.playerController.onPlayerRightClick(
                mc.thePlayer,
                mc.theWorld,
                mc.thePlayer.inventory.getCurrentItem(),
                mop.getBlockPos(),
                mop.sideHit,
                mop.hitVec
        );
        mc.thePlayer.swingItem();

        if (success && HypixelCry.config.macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.Air)) {
            openedChests.put(mop.getBlockPos(), 0);
            mc.theWorld.setBlockState(mop.getBlockPos(), Blocks.air.getDefaultState());
        }

    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        openedChests.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (HypixelCry.config.macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.Air)) {
            if (event.newState.getBlock() == Blocks.chest) {
                event.setCanceled(true);
            }
        }
    }
}

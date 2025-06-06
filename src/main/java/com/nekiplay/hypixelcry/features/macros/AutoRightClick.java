package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickBlocks;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickOpenFeatures;
import com.nekiplay.hypixelcry.utils.RaycastUtils;
import com.nekiplay.hypixelcry.utils.scheduler.Scheduler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.*;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static com.nekiplay.hypixelcry.utils.PlayerUtils.getEyePosition;
import static com.nekiplay.hypixelcry.utils.PlayerUtils.getLookEndPos;

public class AutoRightClick {
    private static final Map<BlockPos, Integer> openedChests = new LinkedHashMap<>();
    private static int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 20 * 60;
    private static final int MAX_REMOVALS_PER_TICK = 20;

    @Init
    public static void init() {
        Scheduler.INSTANCE.scheduleCyclic(AutoRightClick::onTick, 1);
    }

    private static boolean shouldSkipTick() {
        return mc.world == null ||
                mc.player == null || !HypixelCry.config.getInstance().macros.autoRightClick.enabled;
    }

    public static void onTick() {
        if (shouldSkipTick()) return;
        mc.player.sendMessage(Text.of("Tick"), false);
        if (++tickCounter % MAX_REMOVALS_PER_TICK == 0) cleanUpOldChests();
        if (mc.currentScreen == null) handleChestOpening();
    }

    private static void cleanUpOldChests() {
        Iterator<Map.Entry<BlockPos, Integer>> iterator = openedChests.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            if (entry.getValue() >= CHEST_COOLDOWN) {
                iterator.remove();
            } else {
                entry.setValue(entry.getValue() + MAX_REMOVALS_PER_TICK);
            }
        }
    }

    public static List<BlockPos> findBlocksNearby(List<Block> blocksToFind, List<BlockPos> blackListed, float radius) {
        List<BlockPos> foundBlocks = new ArrayList<>();

        if (mc == null || mc.player == null || mc.world == null ||
                blocksToFind == null || blocksToFind.isEmpty()) {
            return foundBlocks;
        }

        BlockPos playerPos = new BlockPos(mc.player.getBlockX(), mc.player.getBlockY(), mc.player.getBlockZ());
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

                    Block block = mc.world.getBlockState(checkPos).getBlock();
                    if (blocksToFind.contains(block)) {
                        foundBlocks.add(checkPos);
                    }
                }
            }
        }

        foundBlocks.sort(Comparator.comparingDouble(pos -> mc.player.squaredDistanceTo(pos.toCenterPos())));
        return foundBlocks;
    }

    private static void handleChestOpening() {
        boolean ghostHand = HypixelCry.config.getInstance().macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.GhostHand);

        List<Block> selectedBlocks = new ArrayList<>();
        List<AutoRightClickBlocks> blocks = HypixelCry.config.getInstance().macros.autoRightClick.blocks;
        if (blocks.contains(AutoRightClickBlocks.Chest)) {
            selectedBlocks.add(Blocks.CHEST);
            selectedBlocks.add(Blocks.TRAPPED_CHEST);
        }
        if (blocks.contains(AutoRightClickBlocks.Lever)) {
            selectedBlocks.add(Blocks.LEVER);
        }
        if (blocks.contains(AutoRightClickBlocks.Skull)) {
            selectedBlocks.add(Blocks.PLAYER_HEAD);
            selectedBlocks.add(Blocks.PLAYER_WALL_HEAD);

            selectedBlocks.add(Blocks.WITHER_SKELETON_SKULL);
            selectedBlocks.add(Blocks.WITHER_SKELETON_WALL_SKULL);

            selectedBlocks.add(Blocks.SKELETON_SKULL);
            selectedBlocks.add(Blocks.SKELETON_WALL_SKULL);

            selectedBlocks.add(Blocks.CREEPER_HEAD);
            selectedBlocks.add(Blocks.CREEPER_WALL_HEAD);
        }

        if (selectedBlocks.isEmpty()) {
            return;
        }

        if (ghostHand) {
            HitResult mouseOver = RaycastUtils.rayTraceToBlocks(
                    getEyePosition(),
                    getLookEndPos(4.5f),
                    selectedBlocks
            );
            if (mouseOver instanceof BlockHitResult blockHitResult) {
                tryOpenChest(blockHitResult);
            }
        } else if (isLookingAt(selectedBlocks) && mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
            tryOpenChest(blockHitResult);
        }
    }


    private static boolean isLookingAt(List<Block> blocks) {
        boolean allowCheck = mc.crosshairTarget != null &&
                mc.crosshairTarget.getType() == HitResult.Type.BLOCK;
        if (allowCheck && mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
            assert mc.world != null;
            return blocks.contains(mc.world.getBlockState(blockHitResult.getBlockPos()).getBlock());
        }
        return false;
    }

    private static void tryOpenChest(BlockHitResult mouseOver) {
        if (mouseOver == null || mouseOver.getType() != HitResult.Type.BLOCK || openedChests.containsKey(mouseOver.getBlockPos())) {
            return;
        }
        simulateHumanClick(mouseOver);

    }

    private static void simulateHumanClick(BlockHitResult mop) {
        if (mop == null || mop.getBlockPos() == null) return;

        assert mc.interactionManager != null;
        ActionResult success = mc.interactionManager.interactBlock(
                mc.player,
                Hand.MAIN_HAND,
                mop
        );
        assert mc.player != null;
        mc.player.swingHand(Hand.MAIN_HAND);

        if (HypixelCry.config.getInstance().macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.Air)) {
            openedChests.put(mop.getBlockPos(), 0);
            assert mc.world != null;
            mc.world.setBlockState(mop.getBlockPos(), Blocks.AIR.getDefaultState());
        }

    }
}
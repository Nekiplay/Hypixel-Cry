package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickBlocks;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickOpenFeatures;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.RaycastUtils;
import com.nekiplay.hypixelcry.utils.scheduler.Scheduler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    private static final Object2IntMap<BlockPos> openedChests = new Object2IntOpenHashMap<>();
    private static int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 100;
    private static final int MAX_REMOVALS_PER_TICK = 20;
    private static final Set<Block> CHEST_BLOCKS = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST);
    private static final Set<Block> SKULL_BLOCKS = Set.of(Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD);

    // Cached config values
    private static boolean enabled;
    private static List<AutoRightClickBlocks> configuredBlocks;
    private static List<AutoRightClickOpenFeatures> configuredFeatures;
    private static float range;

    @Init
    public static void init() {
        Scheduler.INSTANCE.scheduleCyclic(AutoRightClick::onTick, 1);
        BlockUpdateEvent.EVENT.register(AutoRightClick::onBlockUpdate);
    }

    private static void updateConfigCache() {
        enabled = HypixelCry.config.macros.autoRightClick.enabled;
        configuredBlocks = HypixelCry.config.macros.autoRightClick.blocks;
        configuredFeatures = HypixelCry.config.macros.autoRightClick.features;
        range = HypixelCry.config.macros.autoRightClick.range;
    }

    private static boolean shouldSkipTick() {
        return mc.world == null || mc.player == null || !enabled;
    }

    private static ActionResult onBlockUpdate(BlockUpdateEvent event) {
        Block newBlock = event.getNew().getBlock();
        return (CHEST_BLOCKS.contains(newBlock) && openedChests.containsKey(event.getBlockPos()))
                ? ActionResult.FAIL
                : ActionResult.PASS;
    }

    private static void onTick() {
        updateConfigCache();
        if (shouldSkipTick()) return;

        if (++tickCounter % MAX_REMOVALS_PER_TICK == 0) {
            cleanUpOldChests();
            if (openedChests.isEmpty()) {
                tickCounter = 0;
            }
        }

        if (mc.currentScreen == null) {
            handleChestOpening();
        }
    }

    private static void cleanUpOldChests() {
        openedChests.object2IntEntrySet().removeIf(entry -> {
            if (entry.getIntValue() >= CHEST_COOLDOWN) {
                return true;
            }
            entry.setValue(entry.getIntValue() + 1);
            return false;
        });
    }

    private static Set<Block> getAllowedBlocks() {
        if (configuredBlocks.isEmpty()) return Collections.emptySet();

        Set<Block> selectedBlocks = new HashSet<>(4); // Small initial capacity
        if (configuredBlocks.contains(AutoRightClickBlocks.Chest)) {
            selectedBlocks.addAll(CHEST_BLOCKS);
        }
        if (configuredBlocks.contains(AutoRightClickBlocks.Lever)) {
            selectedBlocks.add(Blocks.LEVER);
        }
        if (configuredBlocks.contains(AutoRightClickBlocks.Skull)) {
            selectedBlocks.addAll(SKULL_BLOCKS);
        }
        return selectedBlocks;
    }

    private static void handleChestOpening() {
        if (configuredBlocks.isEmpty()) return;

        boolean ghostHand = configuredFeatures.contains(AutoRightClickOpenFeatures.GhostHand);
        Set<Block> selectedBlocks = getAllowedBlocks();
        if (selectedBlocks.isEmpty()) return;

        BlockHitResult blockHitResult = ghostHand
                ? getGhostHandTarget(selectedBlocks)
                : getNormalTarget(selectedBlocks);

        if (blockHitResult != null) {
            tryOpenChest(blockHitResult);
        }
    }

    private static BlockHitResult getGhostHandTarget(Set<Block> blocks) {
        HitResult mouseOver = RaycastUtils.rayTraceToBlocks(
                getEyePosition(),
                getLookEndPos(range),
                new ArrayList<>(blocks)
        );
        return mouseOver instanceof BlockHitResult ? (BlockHitResult) mouseOver : null;
    }

    private static BlockHitResult getNormalTarget(Set<Block> blocks) {
        if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return null;
        }
        BlockHitResult blockHitResult = (BlockHitResult) mc.crosshairTarget;
        return isLookingAt(blocks, blockHitResult) ? blockHitResult : null;
    }

    private static boolean isLookingAt(Set<Block> blocks, BlockHitResult blockHitResult) {
        if (mc.world == null) return false;
        BlockState state = mc.world.getBlockState(blockHitResult.getBlockPos());
        return blocks.contains(state.getBlock());
    }

    private static void tryOpenChest(BlockHitResult mouseOver) {
        if (mouseOver == null
                || mouseOver.getType() != HitResult.Type.BLOCK
                || mouseOver.getBlockPos() == null
                || openedChests.containsKey(mouseOver.getBlockPos())) {
            return;
        }
        simulateHumanClick(mouseOver);
    }

    private static void simulateHumanClick(BlockHitResult mop) {
        if (mc.interactionManager == null || mc.player == null) return;

        ActionResult actionResult = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, mop);
        openedChests.put(mop.getBlockPos(), 0);

        if (actionResult instanceof ActionResult.Success) {
            mc.player.swingHand(Hand.MAIN_HAND);
            if (configuredFeatures.contains(AutoRightClickOpenFeatures.Air)) {
                updateBlockToAir(mop.getBlockPos());
            }
        }
    }

    private static void updateBlockToAir(BlockPos pos) {
        if (mc.world == null) return;

        BlockState airState = Blocks.AIR.getDefaultState();
        mc.world.setBlockState(pos, airState);
        mc.worldRenderer.updateBlock(mc.world, pos, mc.world.getBlockState(pos), airState, 0);
        mc.world.updateNeighbors(pos, Blocks.AIR);
    }
}

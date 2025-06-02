package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickBlocks;
import com.nekiplay.hypixelcry.config.enums.AutoRightClickOpenFeatures;
import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.features.system.IslandTypeChangeChecker;
import com.nekiplay.hypixelcry.utils.RaycastUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class AutoRightClick {
    private final Map<BlockPos, Integer> openedChests = new LinkedHashMap<>();
    private int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 60, MAX_REMOVALS_PER_TICK = 20;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (shouldSkipTick(event)) return;

        if (++tickCounter % MAX_REMOVALS_PER_TICK == 0) cleanUpOldChests();
        if (mc.currentScreen == null) handleChestOpening();
    }

    private boolean shouldSkipTick(TickEvent.ClientTickEvent event) {
        return event.phase == TickEvent.Phase.START || mc.theWorld == null ||
                mc.thePlayer == null || !HypixelCry.config.macros.autoRightClick.enabled || !HypixelCry.config.macros.autoRightClick.allowedIslands.contains(IslandTypeChangeChecker.getLastDetected());
    }

    private void cleanUpOldChests() {
        openedChests.entrySet().removeIf(entry -> {
            if (entry.getValue() >= CHEST_COOLDOWN) return true;
            entry.setValue(entry.getValue() + 20);
            return false;
        });
    }

    private void handleChestOpening() {
        boolean ghostHand = HypixelCry.config.macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.GhostHand);

        List<Block> selectedBlocks = new ArrayList<>();
        if (HypixelCry.config.macros.autoRightClick.blocks.contains(AutoRightClickBlocks.Chest)) {
            selectedBlocks.add(Blocks.chest);
        }
        if (HypixelCry.config.macros.autoRightClick.blocks.contains(AutoRightClickBlocks.Lever)) {
            selectedBlocks.add(Blocks.lever);
        }
        if (HypixelCry.config.macros.autoRightClick.blocks.contains(AutoRightClickBlocks.Skull)) {
            selectedBlocks.add(Blocks.skull);
        }

        if (ghostHand) {
            MovingObjectPosition mouseOver = RaycastUtils.rayTraceToBlocks(
                    getEyePosition(),
                    getLookEndPos(),
                    selectedBlocks
            );
            tryOpenChest(mouseOver);
        } else if (isLookingAt(selectedBlocks)) {
            tryOpenChest(mc.objectMouseOver);
        }
    }

    private Vec3 getEyePosition() {
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }

    private Vec3 getLookEndPos() {
        float distance = mc.playerController.getBlockReachDistance();
        Vec3 look = mc.thePlayer.getLook(1.0f);
        return getEyePosition().addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
    }

    private boolean isLookingAt(List<Block> blocks) {
        return mc.objectMouseOver != null &&
                mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK &&
                blocks.contains(mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock());
    }

    private void tryOpenChest(MovingObjectPosition mouseOver) {
        if (mouseOver == null || mouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK ||
                openedChests.containsKey(mouseOver.getBlockPos())) return;
        simulateHumanClick(mouseOver);
        openedChests.put(mouseOver.getBlockPos(), 0);
    }

    private void simulateHumanClick(MovingObjectPosition mop) {
        mc.playerController.onPlayerRightClick(
                mc.thePlayer, mc.theWorld,
                mc.thePlayer.inventory.getCurrentItem(),
                mop.getBlockPos(), mop.sideHit, mop.hitVec
        );
        mc.thePlayer.swingItem();
        if (!shouldSkipAir()) {
            mc.theWorld.setBlockState(mop.getBlockPos(), Blocks.air.getDefaultState());
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        openedChests.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (shouldSkipAir()) return;
        if (event.newState.getBlock() == Blocks.chest) {
            event.setCanceled(true);
        }
    }

    public boolean shouldSkipAir() {
        IslandType islandType = IslandTypeChangeChecker.getLastDetected();
        return !HypixelCry.config.macros.autoRightClick.features.contains(AutoRightClickOpenFeatures.Air) || islandType.equals(IslandType.Catacombs) || islandType.equals(IslandType.Private_Island);
    }
}
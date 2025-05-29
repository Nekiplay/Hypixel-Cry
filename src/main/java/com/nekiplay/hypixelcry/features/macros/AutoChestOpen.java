package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.neupages.Macros;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    private final Map<BlockPos, Integer> openedChests = new LinkedHashMap<BlockPos, Integer>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<BlockPos, Integer> eldest) {
            return size() > 100; // Ограничиваем размер карты
        }
    };
    private int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 20; // 1 секунда
    private static final double SEARCH_DISTANCE = 5.0;
    private static final int MAX_CHESTS_TO_REMOVE_PER_TICK = 20; // Чтобы не нагружать процессор

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null || !Main.config.macros.autoChestOpen.enabled) {
            return;
        }

        tickCounter++;
        if (tickCounter % 20 == 0) {
            cleanUpOldChests();
        }

        if (mc.currentScreen == null) {
            checkForChestInLineOfSight();
        }
    }

    private void cleanUpOldChests() {
        // Удаляем старые сундуки, которые больше не актуальны
        Iterator<Map.Entry<BlockPos, Integer>> iterator = openedChests.entrySet().iterator();
        int removed = 0;

        while (iterator.hasNext() && removed < MAX_CHESTS_TO_REMOVE_PER_TICK) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            if (entry.getValue() >= CHEST_COOLDOWN) {
                iterator.remove();
                removed++;
            } else {
                openedChests.put(entry.getKey(), entry.getValue() + 20);
            }
        }
    }

    private void checkForChestInLineOfSight() {
        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState state = mc.theWorld.getBlockState(pos);
            Block block = state.getBlock();
            if (block == Blocks.chest) {
                if (!openedChests.containsKey(pos)) {
                    simulateHumanClick(pos);
                    openedChests.put(pos, 0);
                    return;
                }
            }
        }


        if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.ChestFeatures.GhostHand)) {
            Vec3 startVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
            Vec3 lookVec = mc.thePlayer.getLook(1.0f);
            Vec3 endVec = startVec.addVector(lookVec.xCoord * SEARCH_DISTANCE, lookVec.yCoord * SEARCH_DISTANCE, lookVec.zCoord * SEARCH_DISTANCE);

            MovingObjectPosition mouseOver = BlockUtils.rayTraceToChest(startVec, endVec);

            if (mouseOver != null && mouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos chestPos = mouseOver.getBlockPos();
                if (!openedChests.containsKey(chestPos)) {
                    simulateHumanClick(chestPos);
                    openedChests.put(chestPos, 0);
                }
            }
        }
    }

    private void simulateHumanClick(BlockPos chestPos) {
        ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();
        if (mc.playerController.getIsHittingBlock()) {
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemstack, chestPos, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec);

            if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.ChestFeatures.Air)) {
                mc.theWorld.setBlockState(chestPos, Blocks.air.getDefaultState());
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        openedChests.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.ChestFeatures.Air) && event.newState.getBlock() == Blocks.chest) {
            if (openedChests.containsKey(event.pos)) {
                event.setCanceled(true);
            }
        }
    }
}
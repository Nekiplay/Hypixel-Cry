package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    private BlockPos lastUsed = null;
    private final Map<BlockPos, Integer> openedChests = new HashMap<>();
    private final Map<BlockPos, IBlockState> tempRemovedBlocks = new ConcurrentHashMap<>();
    private int tickCounter = 0;
    private static final int CHEST_COOLDOWN = 200; // 10 секунд
    private static final double SEARCH_DISTANCE = 6.0;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null || !Main.config.macros.autoChestOpen.enabled) {
            return;
        }

        tickCounter++;
        if (tickCounter % 20 == 0) {
            updateOpenedChestsTimers();
        }

        if (mc.currentScreen == null) {
            checkForChestInLineOfSight();
        }
    }

    private void updateOpenedChestsTimers() {
        Iterator<Map.Entry<BlockPos, Integer>> iterator = openedChests.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            if (entry.getValue() >= CHEST_COOLDOWN) {
                iterator.remove();
            } else {
                openedChests.put(entry.getKey(), entry.getValue() + 20);
            }
        }
    }

    private void checkForChestInLineOfSight() {
        // Получаем направление взгляда игрока
        MovingObjectPosition mouseOver = mc.thePlayer.rayTrace(SEARCH_DISTANCE, 1.0f);

        if (mouseOver != null) {
            // Проверяем все блоки вдоль линии взгляда
            BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
            BlockPos endPos = mouseOver.getBlockPos();

            List<BlockPos> line = getLine(playerPos, endPos);
            BlockPos foundChest = findFirstChestInLine(line);

            if (foundChest != null && !openedChests.containsKey(foundChest)) {
                // Временно делаем блоки на пути "воздухом"
                makePathTransparent(line, foundChest);

                // Имитируем человеческое нажатие
                simulateHumanClick(foundChest);

                openedChests.put(foundChest, 0);
                lastUsed = foundChest;
            }
        }
    }

    private List<BlockPos> getLine(BlockPos start, BlockPos end) {
        List<BlockPos> blocks = new ArrayList<>();
        // Упрощенный алгоритм линии (можно заменить на более точный)
        int dx = Math.abs(end.getX() - start.getX());
        int dy = Math.abs(end.getY() - start.getY());
        int dz = Math.abs(end.getZ() - start.getZ());

        int steps = Math.max(Math.max(dx, dy), dz);

        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            BlockPos pos = new BlockPos(
                    start.getX() + (end.getX() - start.getX()) * t,
                    start.getY() + (end.getY() - start.getY()) * t,
                    start.getZ() + (end.getZ() - start.getZ()) * t
            );
            blocks.add(pos);
        }
        return blocks;
    }

    private BlockPos findFirstChestInLine(List<BlockPos> line) {
        for (BlockPos pos : line) {
            IBlockState state = mc.theWorld.getBlockState(pos);
            if (state.getBlock() == Blocks.chest) {
                return pos;
            }
        }
        return null;
    }

    private void makePathTransparent(List<BlockPos> path, BlockPos chestPos) {
        for (BlockPos pos : path) {
            if (!pos.equals(chestPos)) {
                IBlockState state = mc.theWorld.getBlockState(pos);
                if (state.getBlock() != Blocks.air && !tempRemovedBlocks.containsKey(pos)) {
                    tempRemovedBlocks.put(pos, state);
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                }
            }
        }
    }

    private void restoreBlocks() {
        for (Map.Entry<BlockPos, IBlockState> entry : tempRemovedBlocks.entrySet()) {
            mc.theWorld.setBlockState(entry.getKey(), entry.getValue());
        }
        tempRemovedBlocks.clear();
    }

    private void simulateHumanClick(BlockPos chestPos) {
        // Сохраняем оригинальное состояние

        MovingObjectPosition originalMouseOver = mc.objectMouseOver;

        // Имитируем человеческое нажатие
        KeyBindUtils.rightClick();

        // Восстанавливаем состояние
        mc.objectMouseOver = originalMouseOver;

        if (Main.config.macros.autoChestOpen.rageMode) {
            mc.theWorld.setBlockState(chestPos, Blocks.air.getDefaultState());
        }

        restoreBlocks();
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        openedChests.clear();
        tempRemovedBlocks.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (Main.config.macros.autoChestOpen.rageMode && event.newState.getBlock() == Blocks.chest) {
            if (openedChests.containsKey(event.pos)) {
                event.setCanceled(true);
            }
        }
    }
}
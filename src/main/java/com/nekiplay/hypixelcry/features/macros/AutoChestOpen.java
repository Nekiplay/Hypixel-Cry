package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    public BlockPos lastUsed = null;
    public Map<BlockPos, Integer> opened = new HashMap<BlockPos, Integer>();
    private int tickCounter = 0;

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }
        if (!Main.config.macros.autoChestOpen.enabled) {
            return;
        }

        // Увеличиваем счетчик тиков и обрабатываем таймеры для opened
        tickCounter++;
        if (tickCounter % 20 == 0) { // Оптимизация: проверяем каждые 20 тиков
            List<BlockPos> toRemove = new ArrayList<BlockPos>();
            for (Map.Entry<BlockPos, Integer> entry : opened.entrySet()) {
                int newValue = entry.getValue() + 20;
                if (newValue >= 200) { // 200 тиков = 10 секунд (20 тиков/сек)
                    toRemove.add(entry.getKey());
                } else {
                    opened.put(entry.getKey(), newValue);
                }
            }
            for (BlockPos pos : toRemove) {
                opened.remove(pos);
            }
        }

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState blockState = mc.theWorld.getBlockState(pos);
            if (mc.currentScreen == null && blockState.getBlock() == Blocks.chest && (lastUsed == null || !lastUsed.equals(pos))) {
                lastUsed = pos;
                if (!opened.containsKey(pos)) {
                    opened.put(pos, 0); // Добавляем с нулевым счетчиком
                }
                KeyBindUtils.rightClick();
                if (Main.config.macros.autoChestOpen.rageMode) {
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        opened.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (Main.config.macros.autoChestOpen.rageMode) {
            if (event.newState.getBlock() == Blocks.chest) {
                if (opened.containsKey(event.pos)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
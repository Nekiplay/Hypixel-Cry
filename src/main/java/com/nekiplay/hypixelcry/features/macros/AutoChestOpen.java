package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.neupages.Macros;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.nekiplay.hypixelcry.Main.mc;

public class AutoChestOpen {
    public BlockPos lastUsed = null;
    private final Map<BlockPos, Long> opened = new WeakHashMap<>(); // Используем WeakHashMap для автоматической очистки
    private int tickCounter = 0;
    private static final int CLEANUP_INTERVAL = 20 * 5; // Оптимизация: проверяем каждые 5 секунд
    private static final long CHEST_COOLDOWN = 10_000; // 10 секунд в миллисекундах

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
        if (tickCounter % CLEANUP_INTERVAL == 0) {
            long currentTime = System.currentTimeMillis();
            opened.entrySet().removeIf(entry -> currentTime - entry.getValue() >= CHEST_COOLDOWN);
        }

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            IBlockState blockState = mc.theWorld.getBlockState(pos);
            if (mc.currentScreen == null && blockState.getBlock() == Blocks.chest && (lastUsed == null || !lastUsed.equals(pos))) {
                lastUsed = pos;
                if (!opened.containsKey(pos)) {
                    opened.put(pos, System.currentTimeMillis()); // Сохраняем текущее время
                    KeyBindUtils.rightClick();
                    if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.ChestFeatures.Air)) {
                        mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        opened.clear();
    }

    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (Main.config.macros.autoChestOpen.features.contains(Macros.AutoChestOpen.ChestFeatures.Air)) {
            if (event.newState.getBlock() == Blocks.chest) {
                if (opened.containsKey(event.pos)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
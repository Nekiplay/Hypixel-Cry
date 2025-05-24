package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.PathFinder;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class PathFinderRenderer {
    public static PathFinder pathFinder = null;
    public static List<BlockPos> blocks = new ArrayList<BlockPos>();
    public static BlockPos end = null;

    private static BlockPos lastPlayerPos = null;
    private static int updateCooldown = 0;
    private static final int UPDATE_INTERVAL = 20; // Обновлять путь каждые 10 тиков (~1 секунда)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || mc.thePlayer == null || end == null || pathFinder == null) {
            return;
        }

        BlockPos currentPos = mc.thePlayer.getPosition();
        boolean playerMoved = !currentPos.equals(lastPlayerPos);
        //boolean needsUpdate = blocks.isEmpty() || playerMoved;

        // Обновляем путь только если игрок переместился или после интервала
        if (updateCooldown <= 0) {
            // Используем упрощенный путь для оптимизации
            List<BlockPos> newPath = pathFinder.findPath(currentPos, end);
            if (newPath != null && !newPath.isEmpty()) {
                blocks = pathFinder.getSimplifiedPath(newPath);
            } else {
                blocks.clear();
            }

            lastPlayerPos = currentPos;
            updateCooldown = UPDATE_INTERVAL;
        } else {
            updateCooldown--;
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (mc.theWorld != null && pathFinder != null && blocks != null && !blocks.isEmpty())
        {
            BlockPos prevPos = blocks.get(0);

            // Проходим по всем остальным точкам
            for (int i = 1; i < blocks.size(); i++) {
                BlockPos currentPos = blocks.get(i);
                // Рисуем линию от предыдущей точки к текущей
                RenderUtils.drawLine(prevPos, currentPos, 1,
                        SpecialColor.toSpecialColor(Main.getInstance().config.esp.chestEsp.colour));
                // Обновляем предыдущую точку для следующей итерации
                prevPos = currentPos;
            }

            BlockPos end = blocks.get(blocks.size() - 1);

            RenderUtils.renderWaypointText("End", new BlockPos(end.getX() + 0.5, end.getY() + 1.8, end.getZ() + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.getInstance().config.esp.chestEsp.colour));
        }
    }
}

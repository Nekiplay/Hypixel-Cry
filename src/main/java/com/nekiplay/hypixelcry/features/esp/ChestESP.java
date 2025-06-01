package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class ChestESP {
    private static final List<BlockPos> chestLocations = new ArrayList<>();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        chestLocations.clear();

        if (!HypixelCry.dataExtractor.isInSkyblock || mc.theWorld == null || !HypixelCry.config.esp.chestEsp.enabled) return;

        double maxRangeSq = Math.pow(HypixelCry.config.esp.chestEsp.maxRange, 2);

        mc.theWorld.loadedTileEntityList.stream()
                .filter(TileEntityChest.class::isInstance)
                .map(tile -> (TileEntityChest) tile)
                .filter(tile -> HypixelCry.config.esp.chestEsp.maxRange == 0 ||
                        mc.thePlayer.getDistanceSq(tile.getPos()) <= maxRangeSq)
                .forEach(tile -> chestLocations.add(tile.getPos()));
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!HypixelCry.config.esp.chestEsp.enabled || chestLocations.isEmpty()) return;

        Color color = toSpecialColor(HypixelCry.config.esp.chestEsp.colour);

        chestLocations.forEach(pos -> {
            BlockPos centerPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

            if (shouldRenderFeature(ESPFeatures.Box)) {
                RenderUtils.drawBlockBox(centerPos, color, 1, event.partialTicks);
            }
            if (shouldRenderFeature(ESPFeatures.Text)) {
                RenderUtils.renderWaypointText("Chest", centerPos.add(0, 1.3, 0), event.partialTicks, false, color);
            }
            if (shouldRenderFeature(ESPFeatures.Tracer)) {
                RenderUtils.drawTracer(centerPos, color, 1, event.partialTicks);
            }
        });
    }

    private boolean shouldRenderFeature(ESPFeatures feature) {
        return HypixelCry.config.esp.chestEsp.features.contains(feature);
    }
}

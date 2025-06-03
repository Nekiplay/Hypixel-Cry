package com.nekiplay.hypixelcry.features.esp.mining.dwarvenmines;

import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.features.system.IslandTypeChangeChecker;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;


public class Dark_Monolith {
    private BlockPos eggPosition;
    private boolean eggFound;


    // Массив всех возможных позиций для проверки
    private static final BlockPos[] MONOLITH_LOCATIONS  = {
            new BlockPos(-15, 236, -92),
            new BlockPos(49, 202, -162),
            new BlockPos(56, 214, -25),
            new BlockPos(128, 187, 58),
            new BlockPos(150, 196, 190),
            new BlockPos(61, 204, 181),
            new BlockPos(91, 187, 131),
            new BlockPos(77, 160, 162),
            new BlockPos(-10, 162, 109),
            new BlockPos(1, 183, 25),
            new BlockPos(0, 170, 0),
            new BlockPos(-94, 201, -30),
            new BlockPos(-91, 221, -53),
            new BlockPos(-64, 206, -63)
    };

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (shouldSkipTick(event)) return;

        eggPosition = findEggInWorld();
        eggFound = eggPosition != null;
    }

    private boolean shouldSkipTick(TickEvent.ClientTickEvent event) {
        return event.phase == TickEvent.Phase.START ||
                mc.theWorld == null ||
                !HypixelCry.config.esp.dwarvenMines.darkMonolith.enabled || !IslandType.current().equals(IslandType.Dwarven_Mines);
    }

    private BlockPos findEggInWorld() {
        return Arrays.stream(MONOLITH_LOCATIONS)
                .filter(pos -> mc.theWorld.isBlockLoaded(pos))
                .map(this::findEggNearPosition)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private BlockPos findEggNearPosition(BlockPos center) {
        final int searchRadius = 5;
        Iterable<BlockPos> searchArea = BlockPos.getAllInBox(
                center.add(searchRadius, searchRadius, searchRadius),
                center.subtract(new Vec3i(searchRadius, searchRadius, searchRadius))
        );

        for (BlockPos pos : searchArea) {
            IBlockState state = mc.theWorld.getBlockState(pos);
            if (state.getBlock() == Blocks.dragon_egg) {
                return pos;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!shouldRender()) return;

        Color color = toSpecialColor(HypixelCry.config.esp.dwarvenMines.darkMonolith.colour);
        BlockPos centerPos = new BlockPos(
                eggPosition.getX() + 0.5,
                eggPosition.getY() + 0.5,
                eggPosition.getZ() + 0.5
        );

        if (shouldRenderFeature(ESPFeatures.Box)) {
            RenderUtils.drawBlockBox(centerPos, color, 1, event.partialTicks);
        }
        if (shouldRenderFeature(ESPFeatures.Text)) {
            RenderUtils.renderWaypointText("Monolith", new Vec3(eggPosition.getX() + 0.5, eggPosition.getY() + 1.5, eggPosition.getZ() + 0.5), event.partialTicks, false, color);
        }
        if (shouldRenderFeature(ESPFeatures.Tracer)) {
            RenderUtils.drawTracer(centerPos, color, 1, event.partialTicks);
        }
    }

    private boolean shouldRender() {
        return HypixelCry.config.esp.dwarvenMines.darkMonolith.enabled && eggPosition != null && IslandTypeChangeChecker.getLastDetected().equals(IslandType.Dwarven_Mines);
    }

    private boolean shouldRenderFeature(ESPFeatures feature) {
        return HypixelCry.config.esp.dwarvenMines.darkMonolith.features.contains(feature);
    }
}

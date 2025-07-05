package com.nekiplay.hypixelcry.features.esp.mining.dwarvenmines;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import com.nekiplay.hypixelcry.utils.render.RenderHelper;
import com.nekiplay.hypixelcry.utils.scheduler.Scheduler;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Objects;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class DarkMonolithESP {
    private static BlockPos eggPosition;
    private static boolean eggFound;


    @Init
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(DarkMonolithESP::render);
        Scheduler.INSTANCE.scheduleCyclic(DarkMonolithESP::onTick, 2);
    }

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

    private static void onTick() {
        eggPosition = findEggInWorld();
        eggFound = eggPosition != null;
    }

    private static BlockPos findEggInWorld() {
        if (mc.world != null) {
            return Arrays.stream(MONOLITH_LOCATIONS)
                    .filter(mc.world::isPosLoaded)
                    .map(DarkMonolithESP::findEggNearPosition)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private static BlockPos findEggNearPosition(BlockPos center) {
        final int searchRadius = 5;
        BlockPos minPos = center.add(-searchRadius, -searchRadius, -searchRadius);
        BlockPos maxPos = center.add(searchRadius, searchRadius, searchRadius);

        for (BlockPos pos : BlockPos.iterate(minPos, maxPos)) {
            assert mc.world != null;
            BlockState state = mc.world.getBlockState(pos);
            if (state.getBlock() == Blocks.DRAGON_EGG) {
                return pos;
            }
        }
        return null;
    }

    public static void render(WorldRenderContext context) {
        if (!HypixelCry.config.esp.dwarvenMines.darkMonolith.enabled || !eggFound) return;

        float[] colorComponents = SpecialColor.toSpecialColorFloatArray(HypixelCry.config.esp.dwarvenMines.darkMonolith.colour);
        int colorComponentsNoAlpha = SpecialColor.toSpecialColorIntNoAlpha(HypixelCry.config.esp.dwarvenMines.darkMonolith.colour);


        float alpha = colorComponents[3];
        float lineWidth = 1.5f;
        boolean throughWalls = true;

        if (HypixelCry.config.esp.dwarvenMines.darkMonolith.features.contains(ESPFeatures.Box)) {
            RenderHelper.renderFilled(context, eggPosition, colorComponents, alpha, throughWalls);
            RenderHelper.renderOutline(context, eggPosition, colorComponents, lineWidth, throughWalls);
        }
        if (HypixelCry.config.esp.dwarvenMines.darkMonolith.features.contains(ESPFeatures.Text)) {
            RenderHelper.renderText(context, Text.of("Dark").asOrderedText(), eggPosition.toCenterPos().add(0, 1.2, 0), colorComponentsNoAlpha, 1, 0.5f, throughWalls);
            RenderHelper.renderText(context, Text.of("Monolith").asOrderedText(), eggPosition.toCenterPos().add(0, 1, 0), colorComponentsNoAlpha, 1, 0.5f, throughWalls);
        }
        if (HypixelCry.config.esp.dwarvenMines.darkMonolith.features.contains(ESPFeatures.Tracer)) {
            RenderHelper.renderLineFromCursor(context, eggPosition.toCenterPos(), colorComponents, 1, lineWidth);
        }
    }
}

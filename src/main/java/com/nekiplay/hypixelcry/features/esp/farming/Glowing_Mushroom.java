package com.nekiplay.hypixelcry.features.esp.farming;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.events.world.SpawnParticleEvent;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class Glowing_Mushroom {
    private static final List<BlockPos> GLOWING_MUSHROOM_POSITIONS = new ArrayList<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START || HypixelCry.mc.theWorld == null) {
            return;
        }

        Iterator<BlockPos> iterator = GLOWING_MUSHROOM_POSITIONS.iterator();
        while (iterator.hasNext()) {
            BlockPos pos = iterator.next();
            IBlockState state = HypixelCry.mc.theWorld.getBlockState(pos);
            boolean isMushroom = state.getBlock() == Blocks.brown_mushroom
                    || state.getBlock() == Blocks.red_mushroom;

            if (!isMushroom) {
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public void onParticleSpawn(SpawnParticleEvent event) {
        if (event.particleType != EnumParticleTypes.SPELL_MOB) {
            return;
        }

        BlockPos pos = new BlockPos(event.position.xCoord, event.position.yCoord, event.position.zCoord);
        Block block = HypixelCry.mc.theWorld.getBlockState(pos).getBlock();

        if ((block == Blocks.brown_mushroom || block == Blocks.red_mushroom)
                && !GLOWING_MUSHROOM_POSITIONS.contains(pos)) {
            GLOWING_MUSHROOM_POSITIONS.add(pos);
        }
    }

    public static boolean isGlowingMushroom(BlockPos pos) {
        return GLOWING_MUSHROOM_POSITIONS.contains(pos);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (!HypixelCry.config.esp.desertSettlement.glowingMushrooms.enabled) {
            return;
        }

        Color color = toSpecialColor(HypixelCry.config.esp.desertSettlement.glowingMushrooms.colour);
        List<ESPFeatures> features = HypixelCry.config.esp.desertSettlement.glowingMushrooms.features;
        float partialTicks = event.partialTicks;

        for (BlockPos pos : GLOWING_MUSHROOM_POSITIONS) {
            if (features.contains(ESPFeatures.Box)) {
                RenderUtils.drawBlockBox(pos, color, 1, partialTicks);
            }

            if (features.contains(ESPFeatures.Text)) {
                BlockPos textPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5);
                RenderUtils.renderWaypointText("Mushroom", textPos, partialTicks, false, color);
            }

            if (features.contains(ESPFeatures.Tracer)) {
                RenderUtils.drawTracer(pos, color, 1, partialTicks);
            }
        }
    }
}

package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.events.world.SpawnParticleEvent;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class Glowing_Mushroom {
    public static ArrayList<BlockPos> positions = new ArrayList<>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (Main.mc.theWorld != null) {
            for (Object pos_object : positions.toArray()) {
                BlockPos pos = (BlockPos)pos_object;
                IBlockState state = Main.mc.theWorld.getBlockState(pos);
                boolean find = false;
                if (state.getBlock() == Blocks.brown_mushroom || state.getBlock() == Blocks.red_mushroom) {
                    find = true;
                }
                if (!find) {
                    positions.remove(pos);
                }
            }
        }
    }
    @SubscribeEvent
    public void onParticleSpawn(SpawnParticleEvent event) {
        if (event.particleType == EnumParticleTypes.SPELL_MOB) {
            BlockPos pos = new BlockPos(event.position.xCoord, event.position.yCoord, event.position.zCoord);
            Block block = Main.mc.theWorld.getBlockState(pos).getBlock();
            if (block == Blocks.brown_mushroom || block == Blocks.red_mushroom) {
                if (!positions.contains(pos)) {
                    positions.add(pos);
                }
            }
        }
    }

    public static boolean isGlowingMushroom(BlockPos pos) {
        for (Object pos_object : positions.toArray()) {
            BlockPos pose = (BlockPos) pos_object;
            if (pose.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.getInstance().config.esp.desertSettlement.glowingMushrooms.enabled) {
            for (Object pos_object : positions.toArray()) {
                BlockPos pos = (BlockPos) pos_object;
                if (Main.getInstance().config.esp.desertSettlement.glowingMushrooms.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawBlockBox(pos, SpecialColor.toSpecialColor(Main.getInstance().config.esp.desertSettlement.glowingMushrooms.colour), 1, event.partialTicks);
                }
                if (Main.getInstance().config.esp.desertSettlement.glowingMushrooms.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Mushroom", new BlockPos(pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.getInstance().config.esp.desertSettlement.glowingMushrooms.colour));
                }
                if (Main.getInstance().config.esp.desertSettlement.glowingMushrooms.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(pos, SpecialColor.toSpecialColor(Main.getInstance().config.esp.desertSettlement.glowingMushrooms.colour), 1, event.partialTicks);
                }
            }
        }
    }
}

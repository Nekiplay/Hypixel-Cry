package com.example.examplemod.esp;

import com.example.examplemod.Main;
import com.example.examplemod.events.world.SpawnParticleEvent;
import com.example.examplemod.utils.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;

public class Glowing_Mushroom {
    public static ArrayList<BlockPos> positions = new ArrayList<>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
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

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.configFile.GlowingMushroomESP) {
            for (Object pos_object : positions.toArray()) {
                BlockPos pos = (BlockPos) pos_object;
                RenderUtils.drawBlockBox(pos, new Color(126, 180, 65), 1, event.partialTicks);
            }
        }
    }
}
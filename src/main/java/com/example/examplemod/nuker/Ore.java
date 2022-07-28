package com.example.examplemod.nuker;

import com.example.examplemod.DataInterpretation.DataExtractor;
import com.example.examplemod.Main;
import com.example.examplemod.utils.BlockUtils;
import com.example.examplemod.utils.ExposedBlock;
import com.example.examplemod.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class Ore {
    private static BlockPos blockPos;
    private static int currentDamage;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();

    private final ArrayList<Block> ores = new ArrayList<>();
    private int damage = 10;
    private final int damageReset = 10;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work) {
            ores.clear();

            if (Main.configFile.Coal) {
                ores.add(Blocks.coal_ore);
                ores.add(Blocks.coal_block);
            }
            if (Main.configFile.Iron) {
                ores.add(Blocks.iron_ore);
                ores.add(Blocks.iron_block);
            }
            if (Main.configFile.Gold) {
                ores.add(Blocks.gold_ore);
                ores.add(Blocks.gold_block);
            }
            if (Main.configFile.Diamond) {
                ores.add(Blocks.diamond_ore);
                ores.add(Blocks.diamond_block);
            }
            if (Main.configFile.Emerald) {
                ores.add(Blocks.emerald_ore);
                ores.add(Blocks.emerald_block);
            }
            if (Main.configFile.Redstone) {
                ores.add(Blocks.lit_redstone_ore);
                ores.add(Blocks.redstone_ore);
                ores.add(Blocks.redstone_block);
            }
            if (Main.configFile.Lapis) {
                ores.add(Blocks.lapis_ore);
                ores.add(Blocks.lapis_block);
            }
            if (Main.configFile.Stone) {
                ores.add(Blocks.stone);
            }
            if (Main.configFile.Cobblestone) {
                ores.add(Blocks.cobblestone);
            }

            if(currentDamage > damage) {
                currentDamage = 0;
                damage = damageReset;
            }
            if (!Main.mc.thePlayer.onGround && currentDamage != 0) {
                damage += 1;
            }
            if(broken.size() > 10) {
                broken.clear();
            }
            if(blockPos != null && Main.mc.theWorld != null) {
                IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air || !ores.contains(blockState.getBlock())) {
                    currentDamage = 0;
                    damage = damageReset;
                    broken.clear();
                }
            }
            else{
                currentDamage = 0;
                damage = damageReset;
                broken.clear();
            }

            Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
            ArrayList<Vec3> warts = new ArrayList<>();
            double r = 8;
            BlockPos playerPos = Main.mc.thePlayer.getPosition();
            playerPos = playerPos.add(0, 1, 0);
            Vec3i vec3i = new Vec3i(r, r, r);
            Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i));
            for (BlockPos blockPos : blocks) {
                Block block = Main.mc.theWorld.getBlockState(blockPos).getBlock();
                if (block != Blocks.bedrock && block != Blocks.air && ores.contains(block)) {
                    if (!broken.contains(blockPos)) {
                        ExposedBlock exposedBlock = new ExposedBlock(blockPos);
                        if (Main.configFile.Exposed && exposedBlock.IsExposed()) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                        else if (Main.configFile.NotExposed && exposedBlock.IsNotExposed()) {
                            warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                        }
                    }
                }
            }


            double smallest = 9999;
            Vec3 closest = null;
            for (Vec3 wart : warts) {
                double dist = wart.distanceTo(playerVec);
                if (dist < smallest) {
                    smallest = dist;
                    closest = wart;
                }
            }
            if (closest != null && smallest < 5) {
                BlockPos near = new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
                if (currentDamage == 0) {
                    blockPos = near;
                    Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    PlayerUtils.swingItem();
                    damage = damageReset;
                    broken.add(near);
                }
                if (!Main.configFile.Instant) {
                    currentDamage++;
                }
            }
        }
        else {
            damage = damageReset;
        }
    }

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[3].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Ore nuker enabled"));
            }
            else {
                work = false;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Ore nuker disabled"));
            }
        }
    }
}

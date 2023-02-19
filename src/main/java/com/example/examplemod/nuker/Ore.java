package com.example.examplemod.nuker;

import com.example.examplemod.Main;
import com.example.examplemod.events.world.BlockUpdateEvent;
import com.example.examplemod.utils.PlayerUtils;
import com.example.examplemod.utils.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class Ore extends GeneralNuker {
    private static BlockPos blockPos;
    private static int currentDamage;
    public boolean work = false;
    private int damage = 10;
    private final int damageReset = 10;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Main.mc.theWorld != null && Main.mc.thePlayer != null) {
            clearBlocksToBreak();

            if (Main.configFile.OreNukerCoal) {
                addBlockToBreak(Blocks.coal_ore);
                addBlockToBreak(Blocks.coal_block);
            }
            if (Main.configFile.OreNukerIron) {
                addBlockToBreak(Blocks.iron_ore);
                addBlockToBreak(Blocks.iron_block);
            }
            if (Main.configFile.OreNukerGold) {
                addBlockToBreak(Blocks.gold_ore);
                addBlockToBreak(Blocks.gold_block);
            }
            if (Main.configFile.OreNukerDiamond) {
                addBlockToBreak(Blocks.diamond_ore);
                addBlockToBreak(Blocks.diamond_block);
            }
            if (Main.configFile.OreNukerEmerald) {
                addBlockToBreak(Blocks.emerald_ore);
                addBlockToBreak(Blocks.emerald_block);
            }
            if (Main.configFile.OreNukerRedstone) {
                addBlockToBreak(Blocks.lit_redstone_ore);
                addBlockToBreak(Blocks.redstone_ore);
                addBlockToBreak(Blocks.redstone_block);
            }
            if (Main.configFile.OreNukerLapis) {
                addBlockToBreak(Blocks.lapis_ore);
                addBlockToBreak(Blocks.lapis_block);
            }
            if (Main.configFile.OreNukerStone) {
                addBlockToBreak(Blocks.stone);
            }
            if (Main.configFile.OreNukerCobblestone) {
                addBlockToBreak(Blocks.cobblestone);
            }
            if (Main.configFile.OreNukerIce) {
                addBlockToBreak(Blocks.ice);
            }

            Main.configFile.ChangeExposedMode(this, Main.configFile.OreNukerExposedMode);

            if(currentDamage > damage) {
                currentDamage = 0;
                damage = damageReset;
            }
            if (!Main.mc.thePlayer.onGround && currentDamage != 0) {
                damage += 1;
            }
            if(blockPos != null) {
                IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
                Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
                Vec3 vec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                if (playerVec.distanceTo(vec) > 12) {
                    currentDamage = 0;
                    damage = damageReset;
                    blockPos = null;
                }
                else if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air || !isBlockToBreak(blockState.getBlock())) {
                    currentDamage = 0;
                    damage = damageReset;
                    blockPos = null;
                }
            }
            else{
                currentDamage = 0;
                damage = damageReset;
            }


            if (currentDamage == 0) {
                BlockPos near = getClosestBlock(getBlocks());
                if (near != null) {
                    blockPos = near;
                    Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    PlayerUtils.swingItem();
                    damage = damageReset;
                }
            }
            if (Main.configFile.OreNukerMode == 0) {
                currentDamage++;
            }
        }
        else {
            damage = damageReset;
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (work && blockPos != null) {
            RenderUtils.drawBlockBox(blockPos, new Color(255, 255, 255), 1, event.partialTicks);
        }
    }
    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (work) {
            if (isBlockToBreak(event.oldState.getBlock()) && !isBlockToBreak(event.newState.getBlock()) && Main.mc.thePlayer != null) {
                Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
                Vec3 vec = new Vec3(event.pos.getX(), event.pos.getY(), event.pos.getZ());
                if (playerVec.distanceTo(vec) <= 12) {
                    Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, event.pos, EnumFacing.DOWN));
                    currentDamage = 0;
                    damage = damageReset;
                    blockPos = null;
                }
            }
        }
    }

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[3].isPressed()) {
            if (!work) {
                work = true;
                blockPos = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Ore nuker enabled"));
            }
            else {
                work = false;
                blockPos = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Ore nuker disabled"));
            }
        }
    }
}

package com.example.examplemod.nuker;

import com.example.examplemod.Main;
import com.example.examplemod.events.world.BlockUpdateEvent;
import com.example.examplemod.utils.PlayerUtils;
import com.example.examplemod.utils.RenderUtils;
import net.minecraft.block.Block;
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
    private int damage = 80;
    private final int damageReset = 80;
    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        Block block = state.getBlock();
        if ((block.equals(Blocks.coal_ore) || block.equals(Blocks.coal_block)) &&  Main.myConfigFile.OreNukerCoal) {
            return true;
        }
        else if ((block.equals(Blocks.iron_ore) || block.equals(Blocks.iron_block)) &&  Main.myConfigFile.OreNukerIron) {
            return true;
        }
        else if ((block.equals(Blocks.gold_ore) || block.equals(Blocks.gold_block)) &&  Main.myConfigFile.OreNukerGold) {
            return true;
        }
        else if ((block.equals(Blocks.diamond_ore) || block.equals(Blocks.diamond_block)) &&  Main.myConfigFile.OreNukerDiamond) {
            return true;
        }
        else if ((block.equals(Blocks.emerald_ore) || block.equals(Blocks.emerald_block)) &&  Main.myConfigFile.OreNukerEmerald) {
            return true;
        }
        else if ((block.equals(Blocks.lit_redstone_ore) || block.equals(Blocks.redstone_ore) || block.equals(Blocks.redstone_block)) &&  Main.myConfigFile.OreNukerRedstone) {
            return true;
        }
        else if ((block.equals(Blocks.lapis_ore) || block.equals(Blocks.lapis_block)) &&  Main.myConfigFile.OreNukerLapis) {
            return true;
        }
        //else if ((block.equals(Blocks.stone)) &&  Main.myConfigFile.OreNukerStone) {
            //    return true;
            //}
        else if ((block.equals(Blocks.cobblestone)) &&  Main.myConfigFile.OreNukerCobblestone) {
            return true;
        }
        else if ((block.equals(Blocks.ice)) &&  Main.myConfigFile.OreNukerIce) {
            return true;
        }
        else if ((block.equals(Blocks.obsidian)) &&  Main.myConfigFile.OreNukerObsidian) {
            return true;
        }
        else if ((block.equals(Blocks.end_stone)) &&  Main.myConfigFile.OreNukerEndstone) {
            return true;
        }
        return false;
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Main.mc.theWorld != null && Main.mc.thePlayer != null) {
            Main.myConfigFile.ChangeExposedMode(this, Main.myConfigFile.OreNukerExposedMode);

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
                else if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air || !isBlockToBreak(blockState, blockPos)) {
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
            if (Main.myConfigFile.OreNukerMode == 0) {
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
            if (isBlockToBreak(event.oldState, event.pos) && !isBlockToBreak(event.newState, event.pos) && Main.mc.thePlayer != null) {
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
                currentDamage = 0;
                damage = damageReset;
                blockPos = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Ore nuker enabled"));
            }
            else {
                work = false;
                currentDamage = 0;
                damage = damageReset;
                blockPos = null;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Ore nuker disabled"));
            }
        }
    }
}

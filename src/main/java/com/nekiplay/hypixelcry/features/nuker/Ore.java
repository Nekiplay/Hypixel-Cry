package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import com.nekiplay.hypixelcry.utils.PlayerUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
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

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class Ore extends GeneralNuker {
    private static BlockPos blockPos;
    private static Block currentBlock;
    private static int currentDamage;
    public boolean work = false;
    private int damage = 80;
    private final int damageReset = 80;
    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        Block block = state.getBlock();
        if ((block.equals(Blocks.coal_ore) || block.equals(Blocks.coal_block)) &&  Main.myConfigFile.oreMainPage.OreNukerCoal) {
            return true;
        }
        else if ((block.equals(Blocks.iron_ore) || block.equals(Blocks.iron_block)) &&  Main.myConfigFile.oreMainPage.OreNukerIron) {
            return true;
        }
        else if ((block.equals(Blocks.gold_ore) || block.equals(Blocks.gold_block)) &&  Main.myConfigFile.oreMainPage.OreNukerGold) {
            return true;
        }
        else if ((block.equals(Blocks.diamond_ore) || block.equals(Blocks.diamond_block)) &&  Main.myConfigFile.oreMainPage.OreNukerDiamond) {
            return true;
        }
        else if ((block.equals(Blocks.emerald_ore) || block.equals(Blocks.emerald_block)) &&  Main.myConfigFile.oreMainPage.OreNukerEmerald) {
            return true;
        }
        else if ((block.equals(Blocks.lit_redstone_ore) || block.equals(Blocks.redstone_ore) || block.equals(Blocks.redstone_block)) &&  Main.myConfigFile.oreMainPage.OreNukerRedstone) {
            return true;
        }
        else if ((block.equals(Blocks.lapis_ore) || block.equals(Blocks.lapis_block)) &&  Main.myConfigFile.oreMainPage.OreNukerLapis) {
            return true;
        }
        else if ((block.equals(Blocks.stone)) &&  myConfigFile.oreMainPage.OreNukerStone) {
            return true;
        }
        else if ((block.equals(Blocks.cobblestone)) &&  Main.myConfigFile.oreMainPage.OreNukerCobblestone) {
            return true;
        }
        else if ((block.equals(Blocks.ice)) &&  Main.myConfigFile.oreMainPage.OreNukerIce) {
            return true;
        }
        else if ((block.equals(Blocks.obsidian)) &&  Main.myConfigFile.oreMainPage.OreNukerObsidian) {
            return true;
        }
        else if ((block.equals(Blocks.end_stone)) &&  Main.myConfigFile.oreMainPage.OreNukerEndstone) {
            return true;
        }
        return false;
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && mc.theWorld != null && mc.thePlayer != null) {
            Main.myConfigFile.ChangeExposedMode(this, Main.myConfigFile.oreMainPage.OreNukerExposedMode);

            if(currentDamage > damage) {
                currentDamage = 0;
                damage = damageReset;
            }
            if (!mc.thePlayer.onGround && currentDamage != 0) {
                damage += 1;
            }
            if(blockPos != null) {
                IBlockState blockState = mc.theWorld.getBlockState(blockPos);
                Vec3 playerVec = mc.thePlayer.getPositionVector();
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
                SetDistance(myConfigFile.MaximumNukerVericalDistance, myConfigFile.MaximumNukerVericalDistance);
                BlockPos near = getClosestBlock(getBlocks());
                if (near != null) {
                    blockPos = near;
                    currentBlock = mc.theWorld.getBlockState(near).getBlock();
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    PlayerUtils.swingItem();
                    damage = damageReset;
                }
            }
            if (!Main.myConfigFile.oreMainPage.OreNukerMode) {
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
            if (currentBlock == Blocks.coal_ore || currentBlock == Blocks.coal_block) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.coalColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.iron_ore || currentBlock == Blocks.iron_block) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.ironColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.lapis_ore || currentBlock == Blocks.lapis_block) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.lapisColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.redstone_block || currentBlock == Blocks.redstone_ore || currentBlock == Blocks.lit_redstone_ore) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.redstoneColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.gold_ore || currentBlock == Blocks.gold_block) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.goldColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.diamond_ore || currentBlock == Blocks.diamond_block) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.diamondColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.emerald_ore || currentBlock == Blocks.emerald_block) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.emeraldColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.stone) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.stoneColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.cobblestone) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.cobblestoneColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.end_stone) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.endStoneColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.obsidian) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.obsidianColor.toJavaColor(), 1, event.partialTicks);
            }
            else if (currentBlock == Blocks.ice) {
                RenderUtils.drawBlockBox(blockPos, myConfigFile.oreMainPage.iceColor.toJavaColor(), 1, event.partialTicks);
            }
        }
    }
    @SubscribeEvent
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (work) {
            if (isBlockToBreak(event.oldState, event.pos) && !isBlockToBreak(event.newState, event.pos) && mc.thePlayer != null) {
                Vec3 playerVec = mc.thePlayer.getPositionVector();
                Vec3 vec = new Vec3(event.pos.getX(), event.pos.getY(), event.pos.getZ());
                if (playerVec.distanceTo(vec) <= 12) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, event.pos, EnumFacing.DOWN));
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
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Ore nuker enabled"));
            }
            else {
                work = false;
                currentDamage = 0;
                damage = damageReset;
                blockPos = null;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Ore nuker disabled"));
            }
        }
    }
}

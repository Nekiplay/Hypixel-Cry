package com.example.examplemod.nuker;

import com.example.examplemod.Main;
import com.example.examplemod.utils.PlayerUtils;
import com.example.examplemod.utils.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;

import static com.example.examplemod.utils.Perlin2D.PerlinNoice;

public class Foraging {
    private int shovel_tick = 0;
    private static BlockPos blockPos;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Minecraft.getMinecraft().thePlayer != null) {
            if (broken.size() > 40) {
                broken.clear();
            }


            if (Main.mc.thePlayer.onGround)
            {
                InventoryPlayer inventory = Main.mc.thePlayer.inventory;
                ItemStack currentItem = inventory.getCurrentItem();

                if (currentItem != null && currentItem.getItem() instanceof ItemAxe && shovel_tick > 4) {
                    double noice = PerlinNoice(2);
                    if (noice >= Main.configFile.ForagingNukerBoostChance) {
                        for (int i = 0; i < Main.configFile.ForagingNukerBlockPesTick; i++) {
                            BlockPos sand = getSand();
                            if (sand != null) {
                                breakSand(sand); // Break block
                            }
                        }
                    }
                    else
                    {
                        BlockPos sand = getSand();
                        if (sand != null) {
                            breakSand(sand);
                        }
                    }
                }
                if (currentItem != null && currentItem.getItem() instanceof ItemAxe)
                {
                    shovel_tick++;
                }
                else
                {
                    shovel_tick = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (work && blockPos != null) {
            RenderUtils.drawBlockBox(blockPos, new Color(255, 165, 45), 1, event.partialTicks);
        }
    }

    private void breakSand(BlockPos pos) {
        Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
        PlayerUtils.swingItem();
        broken.add(pos);
        blockPos = pos;
    }

    private boolean isBadLog(BlockPos pos) {
        return false;
    }

    private BlockPos getSand() {
        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        ArrayList<Vec3> warts = new ArrayList<>();
        double r = 6;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r, r);
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i));
        for (BlockPos blockPos : blocks) {
            IBlockState block = Main.mc.theWorld.getBlockState(blockPos);
            if (block.getBlock() == Blocks.log || block.getBlock() == Blocks.log2) {
                if (!broken.contains(blockPos)) {
                    warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
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
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[6].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Foraging nuker enabled"));
            }
            else {
                work = false;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Foraging nuker disabled"));
            }
        }
    }
}
package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.FindHotbar;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.PlayerUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.world.TickRate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;

public class Foraging extends GeneralNuker {
    private int shovel_tick = 0;
    private static BlockPos blockPos;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();
    private int boostTicks = 0;
    private GeneralMiner generalMiner = new GeneralMiner();
    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        if (!broken.contains(pos)) {
            if (state.getBlock() == Blocks.log || state.getBlock() == Blocks.log2) {
                return true;
            }
        }
        return false;
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Minecraft.getMinecraft().thePlayer != null) {
            if (TickRate.INSTANCE.getTimeSinceLastTick() > 1 && false) {
                return;
            }
            if (broken.size() > 20) {
                broken.clear();
            }

            if (generalMiner.AllowInstantMining()) {
                InventoryPlayer inventory = mc.thePlayer.inventory;
                ItemStack currentItem = inventory.getCurrentItem();

                //SetDistance(myConfigFile.foragingMainPage.maximumNukerHorizontalDistance, myConfigFile.foragingMainPage.maximumNukerVericalDistance);

                if (currentItem != null && currentItem.getItem() instanceof ItemAxe && shovel_tick > 4) {
                    BoostAlgorithm();
                }

                if (currentItem != null && currentItem.getItem() instanceof ItemAxe) {
                    shovel_tick++;
                } else {
                    shovel_tick = 0;
                }
            }
        }
    }
    private void BoostAlgorithm() {
        if (boostTicks > 1 ) {
            for (int i = 0; i < 1; i++) {
                BlockPos near = getClosestBlock(getBlocks());
                breakSand(near);
            }
            boostTicks = 0;
        } else {
            BlockPos near = getClosestBlock(getBlocks());
            breakSand(near);
            boostTicks++;
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (work && blockPos != null) {
            //RenderUtils.drawBlockBox(blockPos, myConfigFile.foragingMainPage.color.toJavaColor(), 1, event.partialTicks);
            if (false) {
                //RenderUtils.drawTracer(blockPos, myConfigFile.foragingMainPage.color.toJavaColor(), 1, event.partialTicks);
            }
        }
    }

    private void breakSand(BlockPos pos) {
        blockPos = pos;
        if (pos != null) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            PlayerUtils.swingItem();

            broken.add(pos);
        }
    }

    private boolean isBadLog(BlockPos pos) {
        return false;
    }
    private void enable() {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[1].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Foraging nuker enabled"));
            }
            else {
                work = false;
                blockPos = null;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Foraging nuker disabled"));
            }
        }
    }
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        enable();
    }
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {
        enable();
    }
}

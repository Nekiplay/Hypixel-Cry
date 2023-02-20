package com.example.examplemod.nuker;

import akka.actor.dsl.Inbox;
import com.example.examplemod.DataInterpretation.DataExtractor;
import com.example.examplemod.FindHotbar;
import com.example.examplemod.Main;
import com.example.examplemod.utils.ExposedBlock;
import com.example.examplemod.utils.PlayerUtils;
import com.example.examplemod.utils.RenderUtils;
import com.example.examplemod.utils.world.TickRate;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;

public class Sand extends GeneralNuker {
    private int shovel_tick = 0;
    private static BlockPos blockPos;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();
    private int boostTicks = 0;
    private GeneralMiner generalMiner = new GeneralMiner();

    @Override
    public boolean isBlockToBreak(IBlockState state) {
        if(state.getBlock() == Blocks.sand) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Minecraft.getMinecraft().thePlayer != null) {
            if (TickRate.INSTANCE.getTimeSinceLastTick() > 1 && Main.configFile.GeneralNukerTPSGuard) {
                return;
            }
            if (broken.size() > 5) {
                broken.clear();
            }

            if (generalMiner.AllowInstantMining()) {
                InventoryPlayer inventory = Main.mc.thePlayer.inventory;
                ItemStack currentItem = inventory.getCurrentItem();

                Main.configFile.ChangeExposedMode(this, Main.configFile.SandExposedMode);

                if (!Main.configFile.SandGhostShovel) {
                    if (currentItem != null && currentItem.getItem() instanceof ItemSpade && shovel_tick > 4) {
                        BoostAlgorithm();
                    }
                    if (currentItem != null && currentItem.getItem() instanceof ItemSpade) {
                        shovel_tick++;
                    } else {
                        shovel_tick = 0;
                    }
                }
                else {
                    BoostAlgorithm();
                }
            }
        }
    }
    private void BoostAlgorithm() {
        if (boostTicks > Main.configFile.SandNukerBoostTicks) {
            for (int i = 0; i < Main.configFile.SandNukerBlockPesTick; i++) {
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
            RenderUtils.drawBlockBox(blockPos, new Color(255, 165, 45), 1, event.partialTicks);
        }
    }

    private void breakSand(BlockPos pos) {
        blockPos = pos;
        if (pos != null) {
            int last_slot = Main.mc.thePlayer.inventory.currentItem;
            if (Main.configFile.SandGhostShovel) {
                FindHotbar findHotbar = new FindHotbar();
                int shovel_slot = findHotbar.findSlotInHotbar(Items.golden_shovel);
                if (shovel_slot == -1)
                    shovel_slot = findHotbar.findSlotInHotbar(Items.diamond_shovel);
                if (shovel_slot != -1)
                    Main.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(shovel_slot));
            }
            Main.mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            if (Main.configFile.SandGhostShovel) {
                Main.mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(last_slot));
            }
            DataExtractor extractor = Main.getInstance().dataExtractor;
            if (extractor.getScoreBoardData().Zone.contains("Your")) {
                Main.mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
            } else {
                Main.mc.theWorld.setBlockState(pos, Blocks.sandstone.getDefaultState());
            }

            if (Main.mc.theWorld.getBlockState(pos.add(0, 3, 0)).getBlock() == Blocks.cactus) {
                Main.mc.theWorld.setBlockState(pos.add(0, 3, 0), Blocks.air.getDefaultState());
            }
            if (Main.mc.theWorld.getBlockState(pos.add(0, 2, 0)).getBlock() == Blocks.cactus) {
                Main.mc.theWorld.setBlockState(pos.add(0, 2, 0), Blocks.air.getDefaultState());
            }
            if (Main.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.cactus) {
                Main.mc.theWorld.setBlockState(pos.add(0, 1, 0), Blocks.air.getDefaultState());
            }
            PlayerUtils.swingItem();
            broken.add(pos);
        }
    }



    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[5].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Sand nuker enabled"));
            }
            else {
                work = false;
                Main.mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Sand nuker disabled"));
            }
        }
    }
}
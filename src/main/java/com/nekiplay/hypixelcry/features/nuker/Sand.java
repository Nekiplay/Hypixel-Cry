package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
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

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class Sand extends GeneralNuker {
    private int shovel_tick = 0;
    private static BlockPos blockPos;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();
    private int boostTicks = 0;
    private GeneralMiner generalMiner = new GeneralMiner();

    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        if(state.getBlock() == Blocks.sand) {
            return true;
        }
        else if(state.getBlock() == Blocks.mycelium) {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Minecraft.getMinecraft().thePlayer != null) {
            if (TickRate.INSTANCE.getTimeSinceLastTick() > 1 && Main.myConfigFile.GeneralNukerTPSGuard) {
                return;
            }
            if (broken.size() > 5) {
                broken.clear();
            }

            if (generalMiner.AllowInstantMining()) {
                InventoryPlayer inventory = mc.thePlayer.inventory;
                ItemStack currentItem = inventory.getCurrentItem();

                Main.myConfigFile.ChangeExposedMode(this, Main.myConfigFile.sandMainPage.sandExposedMode);
                SetDistance(myConfigFile.sandMainPage.maximumNukerHorizontalDistance, myConfigFile.sandMainPage.maximumNukerVericalDistance);
                if (currentItem != null && currentItem.getItem() instanceof ItemSpade && shovel_tick > 4) {
                    BoostAlgorithm();
                }
                if (currentItem != null && currentItem.getItem() instanceof ItemSpade) {
                    shovel_tick++;
                } else {
                    shovel_tick = 0;
                }
            }
        }
    }
    private void BoostAlgorithm() {
        if (boostTicks > Main.myConfigFile.sandMainPage.sandNukerBoostTicks) {
            for (int i = 0; i < Main.myConfigFile.sandMainPage.sandNukerBlockPesTick; i++) {
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
            RenderUtils.drawBlockBox(blockPos, Main.myConfigFile.sandMainPage.color.toJavaColor(), 1, event.partialTicks);
            if (myConfigFile.sandMainPage.Tracer) {
                RenderUtils.drawTracer(blockPos, Main.myConfigFile.sandMainPage.color.toJavaColor(), 1, event.partialTicks);
            }
        }
    }

    private void breakSand(BlockPos pos) {
        blockPos = pos;
        if (pos != null) {
            int last_slot = mc.thePlayer.inventory.currentItem;
            DataExtractor extractor = Main.getInstance().dataExtractor;
            if (extractor.getScoreBoardData().Zone.contains("Your")) {
                mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
            } else {
                mc.theWorld.setBlockState(pos, Blocks.sandstone.getDefaultState());
            }
			mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            if (mc.theWorld.getBlockState(pos.add(0, 3, 0)).getBlock() == Blocks.cactus) {
                mc.theWorld.setBlockState(pos.add(0, 3, 0), Blocks.air.getDefaultState());
            }
            if (mc.theWorld.getBlockState(pos.add(0, 2, 0)).getBlock() == Blocks.cactus) {
                mc.theWorld.setBlockState(pos.add(0, 2, 0), Blocks.air.getDefaultState());
            }
            if (mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.cactus) {
                mc.theWorld.setBlockState(pos.add(0, 1, 0), Blocks.air.getDefaultState());
            }
            PlayerUtils.swingItem();
            broken.add(pos);
        }
    }


    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[5].isPressed()) {
            if (!work) {
                work = true;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Sand nuker enabled"));
            }
            else {
                work = false;
                blockPos = null;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Sand nuker disabled"));
            }
        }
    }
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[5].isPressed()) {
            if (!work) {
                work = true;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Sand nuker enabled"));
            }
            else {
                work = false;
                blockPos = null;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Sand nuker disabled"));
            }
        }
    }
}
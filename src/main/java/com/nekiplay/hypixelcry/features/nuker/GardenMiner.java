package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.PlayerUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.world.TickRate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class GardenMiner extends GeneralNuker {
    private int shovel_tick = 0;
    private static BlockPos blockPos;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();
    private int boostTicks = 0;
    private GeneralMiner generalMiner = new GeneralMiner();
    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        if (!broken.contains(pos)) {
            if (state.getBlock() == Blocks.leaves || state.getBlock() == Blocks.leaves2) {
                return true;
            }
            else if (state.getBlock() == Blocks.red_flower || state.getBlock() == Blocks.yellow_flower) {
                return true;
            }
            else if (state.getBlock() == Blocks.tallgrass) {
                return true;
            }
            else if (state.getBlock() == Blocks.double_plant) {
                return true;
            }
        }
        return false;
    }
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (work && Minecraft.getMinecraft().thePlayer != null) {
            if (TickRate.INSTANCE.getTimeSinceLastTick() > 1 && Main.myConfigFile.GeneralNukerTPSGuard) {
                return;
            }
            if (broken.size() > 20) {
                broken.clear();
            }
            SetDistance(myConfigFile.MaximumNukerVericalDistance, myConfigFile.MaximumNukerVericalDistance);

            InventoryPlayer inventory = mc.thePlayer.inventory;
            ItemStack currentItem = inventory.getCurrentItem();

            if (shovel_tick > 4) {
                BoostAlgorithm();
            }

            if (currentItem != null) {
                shovel_tick++;
            } else {
                shovel_tick = 0;
            }
        }
    }
    private void BoostAlgorithm() {
        if (boostTicks > Main.myConfigFile.gardenMainPage.GardenNukerBoostTicks) {
            for (int i = 0; i < Main.myConfigFile.gardenMainPage.GardenNukerBlockPesTick; i++) {
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
            RenderUtils.drawBlockBox(blockPos, myConfigFile.gardenMainPage.color.toJavaColor(), 1, event.partialTicks);
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

    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        KeyBinding[] keyBindings = Main.keyBindings;
        if (keyBindings[7].isPressed()) {
            if (!work) {
                work = true;
                broken.clear();
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.GREEN + "Garden nuker enabled"));
            }
            else {
                work = false;
                mc.thePlayer.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.RED + "Garden nuker disabled"));
            }
        }
    }
}

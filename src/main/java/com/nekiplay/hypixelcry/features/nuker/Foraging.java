package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.HypixelCry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class Foraging extends GeneralNuker {
    private int shovel_tick = 0;
    private static BlockPos blockPos;
    public boolean work = false;
    private static final ArrayList<BlockPos> broken = new ArrayList<>();
    private int boostTicks = 0;
    private final GeneralMiner generalMiner = new GeneralMiner();
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
        if (work && mc.thePlayer != null) {
            if (broken.size() > 20) {
                broken.clear();
            }

            if (generalMiner.AllowInstantMining()) {
                InventoryPlayer inventory = mc.thePlayer.inventory;
                ItemStack currentItem = inventory.getCurrentItem();

                SetDistance(4.5f, 4.5f);

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
        if (boostTicks > 13) {
            for (int i = 0; i < 4; i++) {
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

    private void breakSand(BlockPos pos) {
        blockPos = pos;
        if (pos != null) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
            mc.thePlayer.swingItem();

            broken.add(pos);
        }
    }

    private boolean isBadLog(BlockPos pos) {
        return false;
    }
    private void enable() {
        if (!work) {
            work = true;
            broken.clear();
            mc.thePlayer.addChatMessage(new ChatComponentText(HypixelCry.prefix + EnumChatFormatting.GREEN + "Foraging nuker enabled"));
        }
        else {
            work = false;
            blockPos = null;
            mc.thePlayer.addChatMessage(new ChatComponentText(HypixelCry.prefix + EnumChatFormatting.RED + "Foraging nuker disabled"));
        }
    }
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() < 0) {
            return;
        }

        if (Keyboard.getEventKey() == HypixelCry.config.nukers.foraging.activationBind) {
            enable();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) {
            return;
        }

        int button = Mouse.getEventButton() - 100;
        if (button == HypixelCry.config.nukers.foraging.activationBind) {
            enable();
        }
    }
}
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

import java.util.*;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class Foraging extends GeneralNuker {
    private static final int MAX_BROKEN_BLOCKS = 20;
    private static final int BOOST_THRESHOLD = 13;
    private static final int BOOST_MULTIPLIER = 4;
    private static final int SHOVEL_COOLDOWN = 4;

    private int shovelTick = 0;
    private int boostTicks = 0;
    private boolean isWorking = false;

    private static BlockPos currentBlockPos;
    private final Map<BlockPos, Integer> brokenBlocks = new LinkedHashMap<>();
    private final GeneralMiner generalMiner = new GeneralMiner();

    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        return !brokenBlocks.containsKey(pos) &&
                (state.getBlock() == Blocks.log || state.getBlock() == Blocks.log2);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        cleanUpOldBrokenBlocks();

        if (!isWorking || mc.thePlayer == null) {
            return;
        }

        if (generalMiner.AllowInstantMining()) {
            handleForaging();
        }
    }

    private void handleForaging() {
        InventoryPlayer inventory = mc.thePlayer.inventory;
        ItemStack currentItem = inventory.getCurrentItem();

        setDistance(5.4, 7.5);

        if (currentItem != null && currentItem.getItem() instanceof ItemAxe) {
            if (shovelTick > SHOVEL_COOLDOWN) {
                processBoostAlgorithm();
            }
            shovelTick++;
        } else {
            shovelTick = 0;
        }
    }

    private void processBoostAlgorithm() {
        if (boostTicks > BOOST_THRESHOLD) {
            for (int i = 0; i < BOOST_MULTIPLIER; i++) {
                breakBlock(getClosestBlock(getBlocks()));
            }
            boostTicks = 0;
        } else {
            breakBlock(getClosestBlock(getBlocks()));
            boostTicks++;
        }
    }

    private void cleanUpOldBrokenBlocks() {
        Iterator<Map.Entry<BlockPos, Integer>> iterator = brokenBlocks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = iterator.next();
            if (entry.getValue() >= 29) {
                iterator.remove();
            } else {
                entry.setValue(entry.getValue() + 20);
            }
        }
    }

    private void breakBlock(BlockPos pos) {
        if (pos == null) {
            return;
        }

        currentBlockPos = pos;
        mc.thePlayer.sendQueue.addToSendQueue(
                new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        pos,
                        EnumFacing.DOWN
                )
        );
        mc.thePlayer.swingItem();
        brokenBlocks.put(pos, 0);
    }

    public void toggle() {
        isWorking = !isWorking;

        if (isWorking) {
            brokenBlocks.clear();
            sendMessage(EnumChatFormatting.GREEN + "Foraging nuker enabled");
        } else {
            currentBlockPos = null;
            sendMessage(EnumChatFormatting.RED + "Foraging nuker disabled");
        }
    }

    private void sendMessage(String message) {
        mc.thePlayer.addChatMessage(
                new ChatComponentText(HypixelCry.prefix + message)
        );
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() &&
                Keyboard.getEventKey() == HypixelCry.config.nukers.foraging.activationBind) {
            toggle();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState() &&
                Mouse.getEventButton() - 100 == HypixelCry.config.nukers.foraging.activationBind) {
            toggle();
        }
    }
}
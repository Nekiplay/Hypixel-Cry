package com.nekiplay.hypixelcry.features.nuker;

import com.nekiplay.hypixelcry.HypixelCry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class Foraging extends GeneralNuker {
    private static final int BOOST_THRESHOLD = 13;
    private static final int BOOST_MULTIPLIER = 4;
    private static final int SHOVEL_COOLDOWN = 4;

    private int shovelTick = 0;
    private int boostTicks = 0;
    private boolean isWorking = false;

    private final InstantMiningChecker instantMiningChecker = new InstantMiningChecker();

    public Foraging() {
        instantMiningChecker.addAllowedItem(ItemAxe.class);
    }

    @Override
    public boolean isBlockToBreak(IBlockState state, BlockPos pos) {
        return !containsBrokenBlock(pos) &&
                (state.getBlock() == Blocks.log || state.getBlock() == Blocks.log2);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        cleanUpOldBrokenBlocks();

        if (!isWorking || mc.thePlayer == null) {
            return;
        }

        if (instantMiningChecker.AllowInstantMining()) {
            handleForaging();
        }
    }

    private void handleForaging() {
        setDistance(5.4, 7.5);

        if (instantMiningChecker.AllowInstantMining()) {
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

    public void toggle() {
        isWorking = !isWorking;

        if (isWorking) {
            sendMessage(EnumChatFormatting.GREEN + "Foraging nuker enabled");
        } else {
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
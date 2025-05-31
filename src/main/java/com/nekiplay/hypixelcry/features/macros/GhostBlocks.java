package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static com.nekiplay.hypixelcry.Main.mc;

public class GhostBlocks {
    private void enable() {
        EntityPlayerSP player = mc.thePlayer;
        if (player != null) {
            MovingObjectPosition movingObjectPosition = player.rayTrace(Main.config.macros.ghostBlocks.range, 1f);
            if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = movingObjectPosition.getBlockPos();
                IBlockState state = mc.theWorld.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof BlockChest || block instanceof BlockSkull || block instanceof BlockLever) {
                    KeyBindUtils.rightClick();
                } else {
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                    KeyBindUtils.leftClick();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return;

        int keyCode = Keyboard.getEventKey();
        if (keyCode < 0) return;

        try {
            if (keyCode == Main.config.macros.ghostBlocks.ghostBlocksKeyBind) {
                enable();
            }
        } catch (IndexOutOfBoundsException e) {
            // Handle invalid key code silently
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEventMouse(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) return;

        int button = Mouse.getEventButton() - 100;

        try {
            if (button == Main.config.macros.ghostBlocks.ghostBlocksKeyBind) {
                enable();
            }
        } catch (IndexOutOfBoundsException e) {
            // Handle invalid button code silently
        }
    }
}

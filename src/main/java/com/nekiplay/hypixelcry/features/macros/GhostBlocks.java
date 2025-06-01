package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class GhostBlocks {
    private void enable() {
        EntityPlayerSP player = mc.thePlayer;
        if (player != null) {
            MovingObjectPosition movingObjectPosition = player.rayTrace(HypixelCry.config.macros.ghostBlocks.range, 1f);
            if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = movingObjectPosition.getBlockPos();
                IBlockState state = mc.theWorld.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof BlockChest || block instanceof BlockSkull || block instanceof BlockLever) {
                    KeyBindUtils.rightClick();
                } else {
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                    mc.thePlayer.swingItem();
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
            if (keyCode == HypixelCry.config.macros.ghostBlocks.ghostBlocksKeyBind) {
                enable();
            }
        } catch (IndexOutOfBoundsException ignored) { }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEventMouse(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) return;

        int button = Mouse.getEventButton() - 100;

        try {
            if (button == HypixelCry.config.macros.ghostBlocks.ghostBlocksKeyBind) {
                enable();
            }
        } catch (IndexOutOfBoundsException ignored) { }
    }
}

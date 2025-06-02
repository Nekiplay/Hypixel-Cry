package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
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
    private void handleGhostBlocks() {
        if (mc.thePlayer == null) return;

        MovingObjectPosition hitResult = mc.thePlayer.rayTrace(
                HypixelCry.config.macros.ghostBlocks.range,
                1f
        );

        if (hitResult == null || hitResult.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }

        BlockPos pos = hitResult.getBlockPos();
        Block block = mc.theWorld.getBlockState(pos).getBlock();

        if (block instanceof BlockChest || block instanceof BlockSkull || block instanceof BlockLever) {
            KeyBindUtils.rightClick();
        } else {
            mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
            mc.thePlayer.swingItem();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState() || Keyboard.getEventKey() < 0) {
            return;
        }

        if (Keyboard.getEventKey() == HypixelCry.config.macros.ghostBlocks.ghostBlocksKeyBind) {
            handleGhostBlocks();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onMouseInput(InputEvent.MouseInputEvent event) {
        if (!Mouse.getEventButtonState()) {
            return;
        }

        int button = Mouse.getEventButton() - 100;
        if (button == HypixelCry.config.macros.ghostBlocks.ghostBlocksKeyBind) {
            handleGhostBlocks();
        }
    }
}

package com.nekiplay.hypixelcry.features.macros;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import com.nekiplay.hypixelcry.utils.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
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
            MovingObjectPosition movingObjectPosition = player.rayTrace(16, 1f);
            if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = movingObjectPosition.getBlockPos();
                ItemStack hand = mc.thePlayer.getCurrentEquippedItem();
                IBlockState state = mc.theWorld.getBlockState(pos);
                Block block = state.getBlock();
                if (block instanceof BlockChest || block instanceof BlockSkull || block instanceof BlockLever) {
                    KeyBindUtils.rightClick();
                } else {
                    PlayerUtils.swingItem();
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState());
                }
            }
        }
    }
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        int keyCode = Keyboard.getEventKey();
        if (keyCode > 0 && Keyboard.getEventKeyState()) {
            if (Keyboard.isKeyDown(Main.getInstance().config.macros.ghostBlocksKeyBind)) {
                enable();
            }
        }
    }
    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onEventMouse(InputEvent.MouseInputEvent event)
    {
        int keyCode = Mouse.getEventButton();
            if (keyCode < 0 && Mouse.getEventButtonState()) {
                if (Mouse.isButtonDown(Main.getInstance().config.macros.ghostBlocksKeyBind))
                    enable();
            }
        }
    }
}

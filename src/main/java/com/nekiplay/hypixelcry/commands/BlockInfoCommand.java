package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.NEUConfig;
import com.nekiplay.hypixelcry.utils.KeyBindUtils;
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class BlockInfoCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "blockinfo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayerSP player = mc.thePlayer;
        if (player != null) {
            MovingObjectPosition movingObjectPosition = player.rayTrace(Main.getInstance().config.macros.ghostBlocks.range, 1f);
            if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = movingObjectPosition.getBlockPos();
                ItemStack hand = mc.thePlayer.getCurrentEquippedItem();
                IBlockState state = mc.theWorld.getBlockState(pos);
                Block block = state.getBlock();
                int meta = block.getMetaFromState(state);
                sender.addChatMessage(new ChatComponentText(Main.prefix + "[Meta] " + meta));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@NotNull ICommand o) {
        return 0;
    }
}

package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.world.TickRate;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TPS implements ICommand {
    @Override
    public String getCommandName() {
        return "tps";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }
    //
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.WHITE + "TPS: " + TickRate.INSTANCE.getTickRate()));
        sender.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.WHITE + "Time Sience Last Tick: " + TickRate.INSTANCE.getTimeSinceLastTick()));
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

package com.example.examplemod.commands;

import com.example.examplemod.Main;
import com.example.examplemod.remotecontrol.RemoteServer;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RemoteControl implements ICommand {
    @Override
    public String getCommandName() {
        return "remotecontrol";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <yaw> <pitch>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }
    //
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        RemoteServer.startServer(4004);

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

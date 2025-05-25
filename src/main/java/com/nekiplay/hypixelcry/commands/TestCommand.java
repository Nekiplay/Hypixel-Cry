package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import com.nekiplay.hypixelcry.features.esp.pathFinders.detections.crystalhollows.JungleTemple;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class TestCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "test";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(Collections.emptyList());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

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

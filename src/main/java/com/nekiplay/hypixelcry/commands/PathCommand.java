package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "path";
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
        if (args.length >= 3) {
            long x = Long.parseLong(args[0]);
            long y = Long.parseLong(args[1]);
            long z = Long.parseLong(args[2]);

            PathFinderRenderer.addOrUpdatePath("Custom", new BlockPos(x, y, z), Color.RED, "End");
        }
        else {
            if (PathFinderRenderer.hasPath("Custom")) {
                PathFinderRenderer.removePath("Custom");
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

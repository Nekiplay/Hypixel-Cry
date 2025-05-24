package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.NEUConfig;
import com.nekiplay.hypixelcry.features.esp.PathFinderRenderer;
import com.nekiplay.hypixelcry.utils.PathFinder;
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

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
        return new ArrayList<>(Collections.emptyList());
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length >= 3) {
            PathFinderRenderer.pathFinder = new PathFinder(mc.theWorld, 254 * 2);
            long x = Long.parseLong(args[0]);
            long y = Long.parseLong(args[1]);
            long z = Long.parseLong(args[2]);

            PathFinderRenderer.end = new BlockPos(x, y, z);
            sender.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.WHITE + "Set new path"));
        }
        else {
            PathFinderRenderer.blocks = null;
            PathFinderRenderer.end = null;
            sender.addChatMessage(new ChatComponentText(Main.prefix + EnumChatFormatting.WHITE + "Path removed"));
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

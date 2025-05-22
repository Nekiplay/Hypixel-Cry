package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.NEUConfig;
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper;
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenSettings implements ICommand {
    @Override
    public String getCommandName() {
        return "addon";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(Arrays.asList("hypixelcry", "cry"));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        MoulConfigEditor<NEUConfig> gui = new MoulConfigEditor<>(Main.getInstance().processor);
        Main.getInstance().screenToOpen = new GuiScreenElementWrapper(gui);
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

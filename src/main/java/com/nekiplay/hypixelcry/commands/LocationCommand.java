package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class LocationCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "zone";
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
        String zone = ApecUtils.removeAllCodes(Main.dataExtractor.getScoreBoardData().Zone);
        String cleanedLocation = zone.replaceAll("[^a-zA-Z\\s']", "").trim();
        mc.thePlayer.addChatMessage(new ChatComponentText( Main.prefix + "Current location: " + IslandType.current()));
        mc.thePlayer.addChatMessage(new ChatComponentText( Main.prefix + "Current zone: " + cleanedLocation));
        GuiScreen.setClipboardString(zone);
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

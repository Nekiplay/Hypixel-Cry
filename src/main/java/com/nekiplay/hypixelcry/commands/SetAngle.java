package com.nekiplay.hypixelcry.commands;

import com.nekiplay.hypixelcry.HypixelCry;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetAngle implements ICommand {
    @Override
    public String getCommandName() {
        return "setangle";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " <yaw> <pitch>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (args.length == 2) {
                float yaw = Float.parseFloat(args[0]);
                if (yaw >= -180 && yaw <= 180) {
                    float pitch = Float.parseFloat(args[1]);
                    if (pitch >= -90 && pitch <= 90) {
                        HypixelCry.mc.thePlayer.rotationYaw = yaw;
                        HypixelCry.mc.thePlayer.rotationPitch = pitch;
                    }
                    else {
                        sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + EnumChatFormatting.WHITE + "Error"));
                    }
                } else {
                    sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + EnumChatFormatting.WHITE + "Error"));
                }
            }
            else
            {
                sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + EnumChatFormatting.WHITE + "Use: " + "/" + getCommandName() + " <yaw> <pitch>"));
            }
        }
        catch (Exception e) {
            sender.addChatMessage(new ChatComponentText(HypixelCry.prefix + EnumChatFormatting.WHITE + "Error"));
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

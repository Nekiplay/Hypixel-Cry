package com.example.examplemod.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@SideOnly(Side.CLIENT)
public class iteminfocommand extends CommandBase {
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "iteminfo";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Reloads config";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        ItemStack current = player.inventory.getCurrentItem();
        if (current != null && current.hasDisplayName()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(current.serializeNBT().toString()), null);
            sender.addChatMessage(new ChatComponentText(current.serializeNBT().toString()));
        }
    }
}

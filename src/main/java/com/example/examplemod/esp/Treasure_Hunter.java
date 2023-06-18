package com.example.examplemod.esp;

import com.example.examplemod.Main;
import com.example.examplemod.utils.ApecUtils;
import com.example.examplemod.utils.RenderUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Treasure_Hunter {
    public BlockPos pos;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatMsg(ClientChatReceivedEvent event) {
        String nocodes = ApecUtils.removeAllCodes(event.message.getFormattedText());
        if (nocodes.contains("You found a treasure chest!")) {
            pos = null;
        }
        else if (nocodes.contains("NPC") && nocodes.contains("Treasure Hunter"))
        {
            if (nocodes.contains("In the Mushroom Gorge where blue meets the ceiling and the floor, you will find what you are looking for"))
                pos = new BlockPos(209,42,-522);
            else if (nocodes.contains("There is likely a treasure under the wooden bridge in the oasis"))
                pos = new BlockPos(307,72,-554);
            else if (nocodes.contains("I saw some treasure next to a house in the gorge"))
                pos = new BlockPos(295,85,-561);
            else if (nocodes.contains("I seem to recall something near the well in the village"))
                pos = new BlockPos(170,76,-375);
            else if (nocodes.contains("I saw some treasure by a cow skull near the village"))
                pos = new BlockPos(143,77,-401);
            else if (nocodes.contains("There's this guy who says he has the best sheep in the world. I think I saw something around his hut"))
                pos = new BlockPos(392,84,-366);
            else if (nocodes.contains("I remember there was a stone pillar made only of cobblestone in the oasis, could be something there"))
                pos = new BlockPos(118,65,-408);
            else if (nocodes.contains("There was a hay stack with a crop greener than usual around it, I think there is something there"))
                pos = new BlockPos(339,82,-388);
            else if (nocodes.contains("Some dirt was kicked up by the water pool in the overgrown Mushroom Cave. Have a look over there"))
                pos = new BlockPos(238,56,-405);
            else if (nocodes.contains("There are some small ruins out in the desert, might want to check it out"))
                pos = new BlockPos(317,101,-472);
            else if (nocodes.contains("I was at the upper oasis today, I recall seeing something on the cobblestone stepping stones"))
                pos = new BlockPos(189,77,-465);
            else if (nocodes.contains("Near a melon patch inside a tunnel in the mountain I spotted something"))
                pos = new BlockPos(254,100,-570);
            else if (nocodes.contains("I spotted something by an odd looking mushroom on one of the ledges in the Mushroom Gorge, you should check it out"))
                pos = new BlockPos(309,73,-553);
            else if (nocodes.contains("I was in the desert earlier, and I saw something near a red sand rock"))
                pos = new BlockPos(357,82,-324);
            else if (nocodes.contains("There's a single piece of tall grass growing in the desert, I saw something there"))
                pos = new BlockPos(283,76,-363);
            else if (nocodes.contains("Something caught my eye by the red sand near the bridge over the gorge"))
                pos = new BlockPos(306,105,-490);
            else if (nocodes.contains("I found something by a mossy stone pillar in the oasis, you should take a look"))
                pos = new BlockPos(181,93,-542);
            else if (nocodes.contains("I saw something near a farmer's cart, you should check it out"))
                pos = new BlockPos(155,90,-593);
            else if (nocodes.contains("I thought I saw something near the smallest stone pillar in the oasis"))
                pos = new BlockPos(94,65,-450);
            else if (nocodes.contains("I was down near the lower oasis yesterday, I think I saw something under the bridge"))
                pos = new BlockPos(142,70,-448);
            else if (nocodes.contains("There's a treasure chest somewhere in a small cave in the gorge"))
                pos = new BlockPos(258,70,-491);
            else if (nocodes.contains("There's a small house in the gorge, I saw some treasure near there"))
                pos = new BlockPos(297,87,-562);
            else if (nocodes.contains("Down in the Glowing Mushroom Cave, there was a weird looking mushroom, check it out"))
                pos = new BlockPos(181,46,-452);
            else if (nocodes.contains("There are some old stone structures in the Mushroom Gorge, give them a look"))
                pos = new BlockPos(225,54,-501);
            else if (nocodes.contains("Theres this guy who collects animals to experiment on, I think I saw something near his house"))
                pos = new BlockPos(261,179,-561);
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.myConfigFile != null && Main.myConfigFile.TreasureHunterESP && pos != null) {
            RenderUtils.drawNametag(EnumChatFormatting.GREEN.toString() + "Treasure", pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, event.partialTicks);
            RenderUtils.drawBlockBox(pos, new Color(10, 180, 65), 1, event.partialTicks);
        }
    }
}

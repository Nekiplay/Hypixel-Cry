package com.nekiplay.hypixelcry.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.render.Render3D;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.myConfigFile;

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
                pos = allPositions.get(0);
            else if (nocodes.contains("There is likely a treasure under the wooden bridge in the oasis"))
                pos = allPositions.get(1);
            else if (nocodes.contains("I saw some treasure next to a house in the gorge"))
                pos = allPositions.get(2);
            else if (nocodes.contains("I seem to recall something near the well in the village"))
                pos = allPositions.get(3);
            else if (nocodes.contains("I saw some treasure by a cow skull near the village"))
                pos = allPositions.get(4);
            else if (nocodes.contains("There's this guy who says he has the best sheep in the world. I think I saw something around his hut"))
                pos = allPositions.get(5);
            else if (nocodes.contains("I remember there was a stone pillar made only of cobblestone in the oasis, could be something there"))
                pos = allPositions.get(6);
            else if (nocodes.contains("There was a hay stack with a crop greener than usual around it, I think there is something there"))
                pos = allPositions.get(7);
            else if (nocodes.contains("Some dirt was kicked up by the water pool in the overgrown Mushroom Cave. Have a look over there"))
                pos = allPositions.get(8);
            else if (nocodes.contains("There are some small ruins out in the desert, might want to check it out"))
                pos = allPositions.get(9);
            else if (nocodes.contains("I was at the upper oasis today, I recall seeing something on the cobblestone stepping stones"))
                pos = allPositions.get(10);
            else if (nocodes.contains("Near a melon patch inside a tunnel in the mountain I spotted something"))
                pos = allPositions.get(11);
            else if (nocodes.contains("I spotted something by an odd looking mushroom on one of the ledges in the Mushroom Gorge, you should check it out"))
                pos = allPositions.get(12);
            else if (nocodes.contains("I was in the desert earlier, and I saw something near a red sand rock"))
                pos = allPositions.get(13);
            else if (nocodes.contains("There's a single piece of tall grass growing in the desert, I saw something there"))
                pos = allPositions.get(14);
            else if (nocodes.contains("Something caught my eye by the red sand near the bridge over the gorge"))
                pos = allPositions.get(15);
            else if (nocodes.contains("I found something by a mossy stone pillar in the oasis, you should take a look"))
                pos = allPositions.get(16);
            else if (nocodes.contains("I saw something near a farmer's cart, you should check it out"))
                pos = allPositions.get(17);
            else if (nocodes.contains("I thought I saw something near the smallest stone pillar in the oasis"))
                pos = allPositions.get(18);
            else if (nocodes.contains("I was down near the lower oasis yesterday, I think I saw something under the bridge"))
                pos = allPositions.get(19);
            else if (nocodes.contains("There's a treasure chest somewhere in a small cave in the gorge"))
                pos = allPositions.get(20);
            else if (nocodes.contains("There's a small house in the gorge, I saw some treasure near there"))
                pos = allPositions.get(21);
            else if (nocodes.contains("Down in the Glowing Mushroom Cave, there was a weird looking mushroom, check it out"))
                pos = allPositions.get(22);
            else if (nocodes.contains("There are some old stone structures in the Mushroom Gorge, give them a look"))
                pos = allPositions.get(23);
            else if (nocodes.contains("Theres this guy who collects animals to experiment on, I think I saw something near his house"))
                pos = allPositions.get(24);
        }
    }
    private static final ArrayList<BlockPos> allPositions = new ArrayList<BlockPos>(){{
        add(new BlockPos(209,42,-522));
        add(new BlockPos(307,72,-554));
        add(new BlockPos(295,85,-561));
        add(new BlockPos(170,76,-375));
        add(new BlockPos(143,77,-401));
        add(new BlockPos(392,84,-366));
        add(new BlockPos(118,65,-408));
        add(new BlockPos(339,82,-388));
        add(new BlockPos(238,56,-405));
        add(new BlockPos(317,101,-472));
        add(new BlockPos(189,77,-465));
        add(new BlockPos(254,100,-570));
        add(new BlockPos(309,73,-553));
        add(new BlockPos(357,82,-324));
        add(new BlockPos(283,76,-363));
        add(new BlockPos(306,105,-490));
        add(new BlockPos(181,93,-542));
        add(new BlockPos(155,90,-593));
        add(new BlockPos(94,65,-450));
        add(new BlockPos(142,70,-448));
        add(new BlockPos(258,70,-491));
        add(new BlockPos(297,87,-562));
        add(new BlockPos(181,46,-452));
        add(new BlockPos(225,54,-501));
        add(new BlockPos(261,179,-561));

    }};
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (myConfigFile != null && myConfigFile.treasureHunterMainPage.TreasureHunterESP && pos != null) {
            if (myConfigFile.treasureHunterMainPage.Text) {
                RenderUtils.drawNametag(EnumChatFormatting.func_175744_a(myConfigFile.treasureHunterMainPage.treasureTextColor) + "Treasure", pos, event.partialTicks);
            }
            RenderUtils.drawBlockBox(pos, myConfigFile.treasureHunterMainPage.treasureColor.toJavaColor(), 1, event.partialTicks);
            if (myConfigFile.treasureHunterMainPage.Tracer) {
                RenderUtils.drawTracer(pos, myConfigFile.treasureHunterMainPage.treasureTracerColor.toJavaColor(), 1, event.partialTicks);
            }
        }
        else if (myConfigFile != null && myConfigFile.treasureHunterMainPage.TreasureHunterESP) {
            for (BlockPos posibleTreasure: allPositions) {
                if (myConfigFile.treasureHunterMainPage.Text) {
                    RenderUtils.drawNametag(EnumChatFormatting.func_175744_a(myConfigFile.treasureHunterMainPage.treasureTextColor) + "Possible treasure", posibleTreasure, event.partialTicks);
                }
                RenderUtils.drawBlockBox(posibleTreasure, myConfigFile.treasureHunterMainPage.treasureColor.toJavaColor(), 1, event.partialTicks);
                if (myConfigFile.treasureHunterMainPage.Tracer) {
                    RenderUtils.drawTracer(posibleTreasure, myConfigFile.treasureHunterMainPage.treasureTracerColor.toJavaColor(), 1, event.partialTicks);
                }
            }
        }
    }
}

package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.DataInterpretation.DataExtractor;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

import static com.nekiplay.hypixelcry.Main.mc;

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
            if (nocodes.contains("In the Mushroom Gorge where blue meets the ceiling and floor, you will find what you are looking for"))
                pos = allPositions.get(0);
            else if (nocodes.contains("There is likely a treasure under the wooden bridge in the oasis"))
                pos = allPositions.get(1);
            else if (nocodes.contains("I saw some treasure next to a house in the gorge"))
                pos = allPositions.get(2);
            else if (nocodes.contains("I seem to recall seeing something near the well in the village"))
                pos = allPositions.get(3);
            else if (nocodes.contains("I saw some treasure by a cow skull near the village"))
                pos = allPositions.get(4);
            else if (nocodes.contains("There's this guy who says he has the best sheep in the world. I think I saw something around his hut"))
                pos = allPositions.get(5);
            else if (nocodes.contains("I remember there was a stone pillar made only of cobblestone in the oasis, could be something there"))
                pos = allPositions.get(6);
            else if (nocodes.contains("There was a haystack with a crop greener than usual around it, I think there is something near there"))
                pos = allPositions.get(7);
            else if (nocodes.contains("Some dirt was kicked up by the water pool in the overgrown Mushroom Cave. Have a look over there"))
                pos = allPositions.get(8);
            else if (nocodes.contains("There are some small ruins out in the desert, might want to check them out"))
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
            else if (nocodes.contains("There's this guy who collects animals to experiment on, I think I saw something near his house"))
                pos = allPositions.get(24);

        }
    }
    private static final ArrayList<BlockPos> allPositions = new ArrayList<BlockPos>(){{
        add(new BlockPos(209,42,-522));
        add(new BlockPos(307,72,-554));
        add(new BlockPos(295,86,-561));
        add(new BlockPos(170,76,-375));
        add(new BlockPos(143,76,-401));
        add(new BlockPos(392,83,-366));
        add(new BlockPos(118,63,-408));
        add(new BlockPos(339,82,-388));
        add(new BlockPos(238,56,-405));
        add(new BlockPos(317,101,-472));
        add(new BlockPos(189,77,-465));
        add(new BlockPos(254,100,-570));
        add(new BlockPos(309,72,-553));
        add(new BlockPos(357,82,-324));
        add(new BlockPos(283,75,-363));
        add(new BlockPos(306,104,-490));
        add(new BlockPos(181,93,-542));
        add(new BlockPos(155,89,-593));
        add(new BlockPos(94,64,-450));
        add(new BlockPos(142,70,-448));
        add(new BlockPos(258,70,-491));
        add(new BlockPos(297,86,-562));
        add(new BlockPos(181,46,-452));
        add(new BlockPos(225,54,-501));
        add(new BlockPos(261,179,-561));
    }};
    private boolean allowRender = false;
    @SubscribeEvent
    public void OnTick(TickEvent.ClientTickEvent event) {
        DataExtractor extractor = Main.getInstance().dataExtractor;
        String zone = extractor.getScoreBoardData().Zone;
        if (zone.contains("Archeologist") || zone.contains("Mushroom") || zone.contains("Oasis") || zone.contains("Shepherd") || zone.contains("Desert") || zone.contains("Site")) {
            allowRender = true;
        }
        else {
            allowRender = false;
        }
    }
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (allPositions.contains(event.pos)) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText("Breaking"));
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {

        if (allowRender) {
            if (Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.enabled && pos != null) {
                if (Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawBlockBox(pos, SpecialColor.toSpecialColor(Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.colour), 1, event.partialTicks);
                }
                if (Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Treasure", new BlockPos(pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.colour));
                }
                if (Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(pos, SpecialColor.toSpecialColor(Main.getInstance().config.esp.farmingIslands.treasureHunterFetcher.colour), 1, event.partialTicks);
                }
            }
        }

    }
}

package com.nekiplay.hypixelcry.features.esp.farming;

import com.nekiplay.hypixelcry.data.island.IslandType;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.config.neupages.ESP;
import com.nekiplay.hypixelcry.features.system.IslandTypeChangeChecker;
import com.nekiplay.hypixelcry.utils.ApecUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class Treasure_Hunter {
    public BlockPos pos;
    private boolean allowRender = false;

    private static final Map<String, Integer> MESSAGE_TO_INDEX = createMessageMap();
    private static final List<BlockPos> ALL_POSITIONS = createPositionsList();

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onChatMsg(ClientChatReceivedEvent event) {
        String message = ApecUtils.removeAllCodes(event.message.getFormattedText());

        if (message.contains("You found a treasure chest!")) {
            pos = null;
        } else if (message.contains("NPC") && message.contains("Treasure Hunter")) {
            pos = findPosition(message);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            allowRender = IslandTypeChangeChecker.getLastDetected().equals(IslandType.Desert_Settlement);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (allowRender && pos != null && HypixelCry.config.esp.desertSettlement.treasureHunterFetcher.enabled) {
            ESP.Desert_Settlement.Treasure_Hunter_Fetcher config = HypixelCry.config.esp.desertSettlement.treasureHunterFetcher;
            Color color = toSpecialColor(config.colour);

            if (config.features.contains(ESPFeatures.Box)) {
                RenderUtils.drawBlockBox(pos, color, 1, event.partialTicks);
            }
            if (config.features.contains(ESPFeatures.Text)) {
                BlockPos textPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 1.8, pos.getZ() + 0.5);
                RenderUtils.renderWaypointText("Treasure", textPos, event.partialTicks, false, color);
            }
            if (config.features.contains(ESPFeatures.Tracer)) {
                RenderUtils.drawTracer(pos, color, 1, event.partialTicks);
            }
        }
    }

    private BlockPos findPosition(String message) {
        return MESSAGE_TO_INDEX.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey()))
                .findFirst()
                .map(entry -> ALL_POSITIONS.get(entry.getValue()))
                .orElse(null);
    }

    private static Map<String, Integer> createMessageMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("In the Mushroom Gorge where blue meets the ceiling and floor, you will find what you are looking for", 0);
        map.put("There is likely a treasure under the wooden bridge in the oasis", 1);
        map.put("I saw some treasure next to a house in the gorge", 2);
        map.put("I seem to recall seeing something near the well in the village", 3);
        map.put("I saw some treasure by a cow skull near the village", 4);
        map.put("There's this guy who says he has the best sheep in the world. I think I saw something around his hut", 5);
        map.put("I remember there was a stone pillar made only of cobblestone in the oasis, could be something there", 6);
        map.put("There was a haystack with a crop greener than usual around it, I think there is something near there", 7);
        map.put("Some dirt was kicked up by the water pool in the overgrown Mushroom Cave. Have a look over there", 8);
        map.put("There are some small ruins out in the desert, might want to check them out", 9);
        map.put("I was at the upper oasis today, I recall seeing something on the cobblestone stepping stones", 10);
        map.put("Near a melon patch inside a tunnel in the mountain I spotted something", 11);
        map.put("I spotted something by an odd looking mushroom on one of the ledges in the Mushroom Gorge, you should check it out", 12);
        map.put("I was in the desert earlier, and I saw something near a red sand rock", 13);
        map.put("There's a single piece of tall grass growing in the desert, I saw something there", 14);
        map.put("Something caught my eye by the red sand near the bridge over the gorge", 15);
        map.put("I found something by a mossy stone pillar in the oasis, you should take a look", 16);
        map.put("I saw something near a farmer's cart, you should check it out", 17);
        map.put("I thought I saw something near the smallest stone pillar in the oasis", 18);
        map.put("I was down near the lower oasis yesterday, I think I saw something under the bridge", 19);
        map.put("There's a treasure chest somewhere in a small cave in the gorge", 20);
        map.put("There's a small house in the gorge, I saw some treasure near there", 21);
        map.put("Down in the Glowing Mushroom Cave, there was a weird looking mushroom, check it out", 22);
        map.put("There are some old stone structures in the Mushroom Gorge, give them a look", 23);
        map.put("There's this guy who collects animals to experiment on, I think I saw something near his house", 24);
        return map;
    }

    private static List<BlockPos> createPositionsList() {
        return Arrays.asList(
                new BlockPos(209,42,-522),
                new BlockPos(307,72,-554),
                new BlockPos(295,86,-561),
                new BlockPos(170,76,-375),
                new BlockPos(143,76,-401),
                new BlockPos(392,83,-366),
                new BlockPos(118,63,-408),
                new BlockPos(339,82,-388),
                new BlockPos(238,56,-405),
                new BlockPos(317,101,-472),
                new BlockPos(189,77,-465),
                new BlockPos(254,100,-570),
                new BlockPos(309,72,-553),
                new BlockPos(357,82,-324),
                new BlockPos(283,75,-363),
                new BlockPos(306,104,-490),
                new BlockPos(181,93,-542),
                new BlockPos(155,89,-593),
                new BlockPos(94,64,-450),
                new BlockPos(142,70,-448),
                new BlockPos(258,70,-491),
                new BlockPos(297,86,-562),
                new BlockPos(181,46,-452),
                new BlockPos(225,54,-501),
                new BlockPos(261,179,-561)
        );
    }
}

package com.nekiplay.hypixelcry.data.island;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.utils.ApecUtils;

import static com.nekiplay.hypixelcry.HypixelCry.mc;

public enum IslandType {
    Lobby("Lobby"),

    Private_Island("Private Island"),
    Garden("Garden"),

    Dungeon_Hub("Dungeon Hub"),
    Catacombs("Catacombs"),

    Hub("Hub"),

    Park("Park"),

    Gold_Mine("Gold Mine"),
    Deep_Caverns("Deep Caverns"),
    Dwarven_Mines("Dwarven Mines"),
    Crystal_Hollows("Crystal Hollows"),
    Glacite_Tunnels("Glacite Tunnels"),
    Glacite_Mineshaft("Glacite Mineshaft"),

    Farming_Islands("Farming Islands"),
    Desert_Settlement("Desert Settlement"),

    Spider_Den("Spider Den"),

    Unknown("Unknown"),
    None("None"),
    ;
    final String name;

    IslandType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static IslandType getByZone(String zone) {
        return IslandLocations.getIslandByLocation(zone, mc.thePlayer != null ? mc.thePlayer.getName() : "");
    }

    public static IslandType current() {
        if (!HypixelCry.dataExtractor.isInSkyblock) {
            return IslandType.Lobby;
        }
        if (HypixelCry.dataExtractor.isInTheCatacombs) {
            return IslandType.Catacombs;
        } else {
            String zone = ApecUtils.removeAllCodes(HypixelCry.dataExtractor.getScoreBoardData().Zone);
            return IslandType.getByZone(zone);
        }
    }
}

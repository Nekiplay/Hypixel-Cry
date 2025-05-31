package com.nekiplay.hypixelcry.DataInterpretation;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.utils.ApecUtils;

import static com.nekiplay.hypixelcry.Main.mc;

public enum IslandType {
    Lobby("Lobby"),

    Private_Island("Private Island"),
    Private_Island_Guest("Private Island Guest"),

    Garden("Garden"),
    Garden_Guest("Garden Guest"),

    Dungeon_Hub("Dungeon Hub"),
    Catacombs("Catacombs"),

    Hub("Hub"),

    Park("Park"),

    Gold_Mine("Gold Mine"),
    Deep_Caverns("Deep Caverns"),
    Dwarven_Mines("Dwarven Mines"),
    Crystal_Hollows("Crystal Hollows"),

    Farming_Islands("Farming Islands"),
    Desert_Settlement("Desert Settlement"),

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
        return IslandLocations.getIslandByLocation(zone, mc.thePlayer.getName());
    }

    public static IslandType current() {
        if (!Main.dataExtractor.isInSkyblock) {
            return IslandType.Lobby;
        }
        if (Main.dataExtractor.isInTheCatacombs) {
            return IslandType.Catacombs;
        } else {
            String zone = ApecUtils.removeAllCodes(Main.dataExtractor.getScoreBoardData().Zone);
            return IslandType.getByZone(zone);
        }
    }
}

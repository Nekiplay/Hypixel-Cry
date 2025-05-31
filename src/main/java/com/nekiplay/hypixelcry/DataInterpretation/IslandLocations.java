package com.nekiplay.hypixelcry.DataInterpretation;

import java.util.*;
import java.util.stream.Collectors;

public class IslandLocations {
    private static final Map<IslandType, List<String>> islandLocationsMap = new HashMap<>();

    static {
        // Инициализируем локации для каждого острова
		islandLocationsMap.put(
                IslandType.None,
                Collections.singletonList(
                        "None"
                )
        );
		
        islandLocationsMap.put(
                IslandType.Hub,
                Arrays.asList(
                        "Village",
                        "Farmhouse",
                        "Fashion Shop",
                        "Flower House",
                        "Mountain",
                        "Builder's House",
                        "Forest",
                        "Graveyard",
                        "Coal Mine",
                        "Catacombs Entrance",
                        "Farm",
                        "Colosseum",
                        "Fishing Outpost",
                        "Wilderness",
                        "Unincorporated",
                        "Museum",
                        "{username}'s Museum",
                        "{username}'s Atrium",
                        "{username}'s Weapons Win",
                        "{username}'s Armory",
                        "{username}'s Rarities Wi",
                        "Ruins",
                        "Tavern",
                        "Trade Center",
                        "Bazaar Alley",
                        "Auction House",
                        "Hexatorum",
                        "Library",
                        "Blacksmith",
                        "Archery Range",
                        "Weaponsmith",
                        "Community Center",
                        "Pet Care",
						"Thaumaturgist",
						"Wizard Tower"
                )
        );
		
		islandLocationsMap.put(
                IslandType.Dungeon_Hub,
                Collections.singletonList(
                        "Dungeon Hub"
                )
        );
		
		islandLocationsMap.put(
                IslandType.Park,
                Arrays.asList(
                        "Birch Park",
						"Spruce Woods",
						"Lonely Island",
						"Dark Thicket",
						"Savanna Woodland",
						"Melody's Plateau",
						"Jungle Island"
                )
        );
		
		islandLocationsMap.put(
                IslandType.Gold_Mine,
                Collections.singletonList(
                        "Gold Mine"
                )
        );
		
		islandLocationsMap.put(
                IslandType.Deep_Caverns,
                Arrays.asList(
                        "Deep Caverns",
						"Gunpowder Mines",
						"Lapis Quarry",
						"Pigmen's Den",
						"Slimehill",
						"Diamond Reserve",
						"Obsidian Sanctuary"
                )
        );
		
		
		islandLocationsMap.put(
                IslandType.Farming_Islands,
                Collections.singletonList(
                        "The Barn"
                )
        );
		
		islandLocationsMap.put(
                IslandType.Desert_Settlement,
                Arrays.asList(
                        "Desert Settlement",
						"Mushroom Desert",
						"Oasis",
						"Trapper's Den",
						"Shepherd's Keep",
						"Archaeological Site",
						"Mushroom Gorge",
						"Overgrown Mushroom Cave",
						"Glowing Mushroom Cave"
                )
        );

        islandLocationsMap.put(
                IslandType.Dwarven_Mines,
                Arrays.asList(
                        "Dwarven Village",
                        "The Lift",
                        "Dwarven Mines",
                        "Lava Springs",
                        "Palace Bridge",
                        "Royal Palace",
                        "Grand Library",
                        "Royal Quarters",
                        "Barracks of Heroes",
                        "Gates to the Mines",
                        "Rampart's Quarry",
                        "Upper Mines",
                        "Far Reserve",
                        "Goblin Burrows",
                        "Great Ice Wall",
                        "Royal Mines",
                        "Cliffside Veins",
                        "Forge Basin",
                        "The Forge"
                )
        );
    }

    /**
     * Возвращает список локаций для указанного острова (с заменой {username}).
     * Если остров не найден, возвращает пустой список.
     */
    public static List<String> getLocationsForIsland(IslandType islandType, String playerName) {
        List<String> rawLocations = islandLocationsMap.getOrDefault(islandType, Collections.emptyList());

        // Заменяем {username} на playerName
        return rawLocations.stream()
                .map(loc -> loc.replace("{username}", !playerName.isEmpty() ? playerName : "{username}"))
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, принадлежит ли локация указанному острову (с учетом {username}).
     * Возвращает true если принадлежит, false если нет.
     */
    public static boolean isLocationInIsland(String location, IslandType islandType, String playerName) {
        if (location == null) return false;

        String cleanedLocation = location.replaceAll("[^a-zA-Z\\s']", "").trim();

        // Получаем локации острова с заменой {username}
        List<String> islandLocations = getLocationsForIsland(islandType, playerName);

        // Сравниваем с очищенными версиями локаций (без учёта регистра)
        for (String islandLoc : islandLocations) {
            String cleanedIslandLoc = islandLoc.replaceAll("[^a-zA-Z\\s']", "").trim();
            if (cleanedIslandLoc.equalsIgnoreCase(cleanedLocation)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Возвращает IslandType для указанной локации (с учетом {username}).
     * Если локация не найдена, возвращает Unknown.
     */
    public static IslandType getIslandByLocation(String location, String playerName) {
        if (location == null) return IslandType.Unknown;

        for (IslandType islandType : IslandType.values()) {
            if (isLocationInIsland(location, islandType, playerName)) {
                return islandType;
            }
        }
        return IslandType.Unknown;
    }
}
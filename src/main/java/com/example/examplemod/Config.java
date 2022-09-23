package com.example.examplemod;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Comparator;


public class Config extends Vigilant {
    public static Config INSTANCE = new Config();

    @Property(type = PropertyType.SWITCH, name = "Replanish support", description = "Better work replanish.",
            category = "Crop nuker", subcategory = "General")
    public boolean CropNukerReplanish = true;

    @Property(type = PropertyType.SWITCH, name = "Only mathematical hoe", description = "Breaks only plants that are intended for your hoe and dicer.",
             category = "Crop nuker", subcategory = "General")
    public boolean CropNukerOnlyMathematicalHoe = false;

    @Property(type = PropertyType.SLIDER, name = "Max boost", description = "Boost max block per tick speed",
            category = "Crop nuker", subcategory = "General", min = 1, max = 8)
    public int CropNukerBlockPesTick = 4;

    @Property(type = PropertyType.NUMBER, name = "Boos every ticks", description = "Boost delay",
            category = "Crop nuker", subcategory = "General", max = 999999999)
    public int CropNukerBoostTicks = 13;

    @Property(type = PropertyType.SLIDER, name = "Y Range", description = "Max Y range",
            category = "Crop nuker", subcategory = "General", min = 1, max = 5)
    public int CropNukerMaxYRange = 5;

    @Property(type = PropertyType.SWITCH, name = "Remover", description = "Remove planted crops.",
            category = "Crop nuker", subcategory = "General")
    public boolean CropNukerRemover = false;

    @Property(type = PropertyType.SWITCH, name = "TPS Check", description = "Dont break if server lag.",
            category = "Crop nuker", subcategory = "General")
    public boolean CropLagGuard = true;

    @Property(type = PropertyType.SLIDER, name = "Max boost", description = "Boost max block per tick speed",
            category = "Foraging nuker", subcategory = "General", min = 1, max = 8)
    public int ForagingNukerBlockPesTick = 4;

    @Property(type = PropertyType.NUMBER, name = "Boos every ticks", description = "Boost delay",
            category = "Foraging nuker", subcategory = "General", max = 999999999)
    public int ForagingNukerBoostTicks = 13;

    @Property(type = PropertyType.SLIDER, name = "Max boost", description = "Boost max block per tick speed",
            category = "Sand nuker", subcategory = "General", min = 1, max = 8)
    public int SandNukerBlockPesTick = 1;

    @Property(type = PropertyType.NUMBER, name = "Boos every ticks", description = "Boost delay",
            category = "Sand nuker", subcategory = "General", max = 999999999)
    public int SandNukerBoostTicks = 13;
    @Property(type = PropertyType.SWITCH, name = "Exposed", description = "Mine blocks if block contact air.",
            category = "Sand nuker", subcategory = "General")
    public boolean SandExposed = false;

    @Property(type = PropertyType.SWITCH, name = "Not Exposed", description = "Mine blocks if block not contact air.",
            category = "Sand nuker", subcategory = "General")
    public boolean SandNotExposed = false;
    @Property(type = PropertyType.SWITCH, name = "Ghost Shovel", description = "Ghost switch shovel to hand.",
            category = "Sand nuker", subcategory = "General")
    public boolean SandGhostShovel = false;


    @Property(type = PropertyType.SWITCH, name = "Instant", description = "Use instant mine.",
            category = "Ore nuker", subcategory = "General")
    public boolean OreInstant = false;

    @Property(type = PropertyType.SWITCH, name = "Exposed", description = "Mine blocks if block contact air.",
            category = "Ore nuker", subcategory = "General")
    public boolean OreExposed = false;

    @Property(type = PropertyType.SWITCH, name = "Not Exposed", description = "Mine blocks if block not contact air.",
            category = "Ore nuker", subcategory = "General")
    public boolean OreNotExposed = false;

    @Property(type = PropertyType.SWITCH, name = "Coal", description = "Mine coal ore and coal block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreCoal = true;

    @Property(type = PropertyType.SWITCH, name = "Iron", description = "Mine iron ore and iron block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreIron = true;

    @Property(type = PropertyType.SWITCH, name = "Gold", description = "Mine gold ore and gold block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreGold = true;

    @Property(type = PropertyType.SWITCH, name = "Lapis", description = "Mine lapis ore and lapis block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreLapis = true;

    @Property(type = PropertyType.SWITCH, name = "Redstone", description = "Mine redstone ore and redstone block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreRedstone = true;

    @Property(type = PropertyType.SWITCH, name = "Emerald", description = "Mine emerald ore and emerald block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreEmerald = true;

    @Property(type = PropertyType.SWITCH, name = "Diamond", description = "Mine diamond ore and diamond block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreDiamond = true;

    @Property(type = PropertyType.SWITCH, name = "Stone", description = "Mine stone instantly.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreStone = false;

    @Property(type = PropertyType.SWITCH, name = "Cobblestone", description = "Mine cobblestone instantly.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreCobblestone = false;

    @Property(type = PropertyType.SWITCH, name = "Dark Monolith ESP", description = "Render Dragon Egg in Dwarden Mines",
            category = "ESP", subcategory = "Dwarden Mines", min = 20, max = 120)
    public boolean DwardenMinesDarkMonolithESP = true;

    @Property(type = PropertyType.SWITCH, name = "Hide Attack", description = "Ghost switch weapon to hand",
            category = "Exploits", subcategory = "Enable")
    public boolean HideAttack = true;
    @Property(type = PropertyType.SLIDER, name = "Weapon slot", description = "Weapon hotbar slot",
            category = "Exploits", subcategory = "Enable", min = 0, max = 8)
    public int HideAttackWeaponSlot = 0;



    public Config() {
        super(new File("./config/hypixelcry/config.toml"), "HypixelCry", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
    }

    public static class ConfigSorting extends SortingBehavior {
        @NotNull
        @Override
        public Comparator<? super Category> getCategoryComparator() {
            return (o1, o2) -> {
                if(o1.getName().equals("HypixelCry")) {
                    return -1;
                } else if(o2.getName().equals("HypixelCry")) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
    }
}

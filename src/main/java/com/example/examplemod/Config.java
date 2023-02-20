package com.example.examplemod;

import com.example.examplemod.nuker.GeneralNuker;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Comparator;


public class Config extends Vigilant {
    public static Config INSTANCE = new Config();

    @Property(type = PropertyType.SWITCH, name = "TPS Check", description = "Dont break if server lag.",
            category = "General", subcategory = "Nuker's")
    public boolean GeneralNukerTPSGuard = true;

    //region Crop Nuker
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
    //endregion

    //region Foraging Nuker
    @Property(type = PropertyType.SLIDER, name = "Max boost", description = "Boost max block per tick speed",
            category = "Foraging nuker", subcategory = "General", min = 1, max = 8)
    public int ForagingNukerBlockPesTick = 4;

    @Property(type = PropertyType.NUMBER, name = "Boos every ticks", description = "Boost delay",
            category = "Foraging nuker", subcategory = "General", max = 999999999)
    public int ForagingNukerBoostTicks = 13;
    @Property(type = PropertyType.SWITCH, name = "Ghost Axe", description = "Ghost switch axe to hand.",
            category = "Foraging nuker", subcategory = "General")
    public boolean ForagingNukerGhostAxe = false;
    //endregion

    //region Sand Nuker
    @Property(type = PropertyType.SLIDER, name = "Max boost", description = "Boost max block per tick speed",
            category = "Sand nuker", subcategory = "General", min = 1, max = 8)
    public int SandNukerBlockPesTick = 4;

    @Property(type = PropertyType.NUMBER, name = "Boos every ticks", description = "Boost delay",
            category = "Sand nuker", subcategory = "General", max = 999999999)
    public int SandNukerBoostTicks = 13;
    @Property(type = PropertyType.SELECTOR, name = "Find mode", description = "Exposed mode",
            category = "Sand nuker", subcategory = "General", options = {"Hidden", "Visible", "All"})
    public int SandExposedMode = 2;
    @Property(type = PropertyType.SWITCH, name = "Ghost Shovel", description = "Ghost switch shovel to hand.",
            category = "Sand nuker", subcategory = "General")
    public boolean SandGhostShovel = false;
    //endregion

    //region Ore Nuker
    @Property(type = PropertyType.SELECTOR, name = "Break mode", description = "Mining mode",
            category = "Ore nuker", subcategory = "General", options = {"Default", "Instant"})
    public int OreNukerMode = 0;

    @Property(type = PropertyType.SELECTOR, name = "Find mode", description = "Exposed mode",
            category = "Ore nuker", subcategory = "General", options = {"Hidden", "Visible", "All"})
    public int OreNukerExposedMode = 2;
    @Property(type = PropertyType.SWITCH, name = "Coal", description = "Mine coal ore and coal block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerCoal = true;

    @Property(type = PropertyType.SWITCH, name = "Iron", description = "Mine iron ore and iron block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerIron = true;

    @Property(type = PropertyType.SWITCH, name = "Gold", description = "Mine gold ore and gold block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerGold = true;

    @Property(type = PropertyType.SWITCH, name = "Lapis", description = "Mine lapis ore and lapis block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerLapis = true;

    @Property(type = PropertyType.SWITCH, name = "Redstone", description = "Mine redstone ore and redstone block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerRedstone = true;

    @Property(type = PropertyType.SWITCH, name = "Emerald", description = "Mine emerald ore and emerald block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerEmerald = true;

    @Property(type = PropertyType.SWITCH, name = "Diamond", description = "Mine diamond ore and diamond block.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerDiamond = true;

    @Property(type = PropertyType.SWITCH, name = "Stone", description = "Mine stone instantly.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerStone = false;

    @Property(type = PropertyType.SWITCH, name = "Cobblestone", description = "Mine cobblestone instantly.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerCobblestone = false;

    @Property(type = PropertyType.SWITCH, name = "Ice", description = "Mine ice instantly.",
            category = "Ore nuker", subcategory = "Blocks")
    public boolean OreNukerIce = false;
    //endregion

    //region Mithril Nuker
    @Property(type = PropertyType.SELECTOR, name = "Find mode", description = "Exposed mode",
            category = "Mithril nuker", subcategory = "General", options = {"Hidden", "Visible", "All"})
    public int MithrilNukerExposedMode = 2;

    @Property(type = PropertyType.SWITCH, name = "Skip Titanium", description = "Mithril nuker will ignore titanium",
            category = "Mithril nuker", subcategory = "General")
    public boolean MithrilNukerIgnoreTitanium = false;
    //endregion

    //region Dwarden Mines ESP
    @Property(type = PropertyType.SWITCH, name = "Dark Monolith ESP", description = "Render Dragon Egg in Dwarden Mines",
            category = "ESP", subcategory = "Dwarden Mines", min = 20, max = 120)
    public boolean DwardenMinesDarkMonolithESP = true;
    //endregion

    @Property(type = PropertyType.SWITCH, name = "Hide Attack", description = "Ghost switch weapon to hand",
            category = "Exploits", subcategory = "Hide Attack")
    public boolean HideAttack = true;
    @Property(type = PropertyType.SLIDER, name = "Weapon slot", description = "Weapon hotbar slot",
            category = "Exploits", subcategory = "Hide Attack", min = 0, max = 8)
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

    public void ChangeExposedMode(GeneralNuker nuker, int index) {
        boolean exposed = false;
        boolean notexposed = false;
        switch (index) {
            case 0:
                notexposed = true;
                break;
            case 1:
                notexposed = true;
                break;
            case 2:
                notexposed = true;
                exposed = true;
                break;
        }
        nuker.changeExposedConfig(exposed, notexposed);
    }
}

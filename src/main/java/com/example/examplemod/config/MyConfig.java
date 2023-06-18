package com.example.examplemod.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import com.example.examplemod.config.pages.OreNukerBlocks;
import com.example.examplemod.hud.TPSHud;
import com.example.examplemod.hud.TimeSinceLastTickHud;
import com.example.examplemod.nuker.GeneralNuker;



public class MyConfig extends Config {

    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Garden nuker",
            subcategory = "General"
    )
    public int GardenNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Garden nuker",
            subcategory = "General"
    )
    public int GardenNukerBoostTicks = 13;
    @HUD(
            name = "TPS",
            category = "HUD",
            subcategory = "TPS"
    )
    public TPSHud tpsHud = new TPSHud();

    @HUD(
            name = "Time Since Last Tick",
            category = "HUD",
            subcategory = "TPS"
    )
    public TimeSinceLastTickHud timeSinceLastTickHud = new TimeSinceLastTickHud();

    @Checkbox(
            name = "TPS Check",
            description = "Dont break if server lag.",
            category = "Nuker's",
            subcategory = "General"
    )
    public boolean GeneralNukerTPSGuard = true;

    //region Crop Nuker
    @Checkbox(
            name = "Replanish support",
            description = "Better work replanish",
            category = "Crop nuker",
            subcategory = "General"
    )
    public boolean CropNukerReplanish = true;

    @Checkbox(
            name = "Only mathematical hoe",
            description = "Breaks only plants that are intended for your hoe and dicer",
            category = "Crop nuker",
            subcategory = "General"
    )
    public boolean CropNukerOnlyMathematicalHoe = false;

    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Crop nuker",
            subcategory = "General"
    )
    public int CropNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Crop nuker",
            subcategory = "General"
    )
    public int CropNukerBoostTicks = 13;

    @Slider(
            name = "Y Range",
            step = 1,
            min = 1.0F,
            max = 5.0F,
            description = "Max Y range",
            category = "Crop nuker",
            subcategory = "General"
    )
    public int CropNukerMaxYRange = 5;

    @Checkbox(
            name = "Remover",
            description = "Remove planted crops",
            category = "Crop nuker",
            subcategory = "General"
    )
    public boolean CropNukerRemover = false;
    //endregion

    //region Foraging Nuker
    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Foraging nuker",
            subcategory = "General"
    )
    public int ForagingNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Foraging nuker",
            subcategory = "General"
    )
    public int ForagingNukerBoostTicks = 13;

    @Checkbox(
            name = "Ghost Axe",
            description = "Ghost switch axe to hand",
            category = "Foraging nuker",
            subcategory = "General"
    )
    public boolean ForagingNukerGhostAxe = false;
    //endregion

    //region Sand Nuker
    @Slider(
            name = "Max boost",
            step = 1,
            min = 1.0F,
            max = 8.0F,
            description = "Boost max block per tick speed",
            category = "Sand nuker",
            subcategory = "General"
    )
    public int SandNukerBlockPesTick = 4;

    @Slider(
            name = "Boost every ticks",
            step = 1,
            min = 0.0F,
            max = 64.0F,
            description = "Boost delay",
            category = "Sand nuker",
            subcategory = "General"
    )
    public int SandNukerBoostTicks = 13;

    @Dropdown(
            name = "Find mode",
            description = "Exposed mode",
            category = "Sand nuker",
            subcategory = "General",
            options = {"Hidden", "Visible", "All"}
    )
    public int SandExposedMode = 2;

    @Checkbox(
            name = "Ghost Shovel",
            description = "Ghost switch shovel to hand",
            category = "Sand nuker",
            subcategory = "General"
    )
    public boolean SandGhostShovel = false;
    //endregion

    //region Ore Nuker

    @Dropdown(
            name = "Break mode",
            description = "Mining mode",
            category = "Ore nuker",
            subcategory = "General",
            options = {"Default", "Instant"}
    )
    public int OreNukerMode = 0;

    @Dropdown(
            name = "Find mode",
            description = "Exposed mode",
            category = "Ore nuker",
            subcategory = "General",
            options = {"Hidden", "Visible", "All"}
    )
    public int OreNukerExposedMode = 2;
    @Page(
            name = "Nuker Blocks",
            description = "Toggles for specific nuker configurations",
            category = "Ore nuker",
            subcategory = "Blocks",
            location = PageLocation.BOTTOM
    )
    public OreNukerBlocks oreNukerBlocks = new OreNukerBlocks();
    @Checkbox(
            name = "Coal",
            description = "Mine coal",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerCoal = true;

    @Checkbox(
            name = "Iron",
            description = "Mine iron",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerIron = true;

    @Checkbox(
            name = "Gold",
            description = "Mine gold",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerGold = true;

    @Checkbox(
            name = "Lapis",
            description = "Mine lapis",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerLapis = true;

    @Checkbox(
            name = "Redstone",
            description = "Mine redstone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerRedstone = true;

    @Checkbox(
            name = "Emerald",
            description = "Mine emeralds",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerEmerald = true;

    @Checkbox(
            name = "Diamond",
            description = "Mine diamonds",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerDiamond = true;

    @Checkbox(
            name = "Stone",
            description = "Mine stone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )

    public boolean OreNukerStone = false;

    @Checkbox(
            name = "Cobblestone",
            description = "Mine cobblestone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerCobblestone = false;

    @Checkbox(
            name = "End stone",
            description = "Mine end stone",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerEndstone = false;

    @Checkbox(
            name = "Ice",
            description = "Mine ice",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerIce = false;

    @Checkbox(
            name = "Obsidian",
            description = "Mine Obsidian",
            category = "Ore nuker",
            subcategory = "Blocks"
    )
    public boolean OreNukerObsidian = false;
    //endregion

    //region Mithril Nuker
    @Dropdown(
            name = "Find mode",        // name of the component
            description = "Exposed mode",
            options = {"Hidden", "Visible", "All"},
            category = "Mithril nuker", subcategory = "General"
    )
    public int MithrilNukerExposedMode = 2;

    @Checkbox(
            name = "Skip Titanium",
            description = "Mithril nuker will ignore titanium",
            category = "Mithril nuker",
            subcategory = "General"
    )
    public boolean MithrilNukerIgnoreTitanium = false;
    //endregion

    //region Dark Monolith ESP
    @Checkbox(
            name = "Dark Monolith ESP",
            description = "Render Dragon Egg in Dwarden Mines",
            category = "ESP",
            subcategory = "Dwarden Mines"
    )
    public boolean DwardenMinesDarkMonolithESP = true;
    //endregion

    //region Glowing Mushroom ESP
    @Checkbox(
            name = "Glowing Mushroom ESP",
            description = "Render Glowing Mushrooms",
            category = "ESP",
            subcategory = "Glowing Mushroom Island"
    )
    public boolean GlowingMushroomESP = true;
    //endregion

    //region Treasure Hunter ESP
    @Checkbox(
            name = "Treasure Hunter ESP",
            description = "Render Treasure location",
            category = "ESP",
            subcategory = "Glowing Mushroom Island"
    )
    public boolean TreasureHunterESP = true;
    //endregion

    @Checkbox(
            name = "Hide Attack",
            description = "Ghost switch weapon to hand",
            category = "Exploits",
            subcategory = "Hide Attack"
    )
    public boolean HideAttack = true;

    @Slider(
            name = "Weapon slot",
            step = 1,
            max = 10.0F,
            min = 1.0F,
            description = "Weapon hotbar slot",
            category = "Exploits",
            subcategory = "Hide Attack"
    )
    public int HideAttackWeaponSlot = 1;

    public MyConfig() {
        super(new Mod("Hypixel Cry", ModType.SKYBLOCK), "hypixelcry.json");
        initialize();
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

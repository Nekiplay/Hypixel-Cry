package com.nekiplay.hypixelcry.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import com.nekiplay.hypixelcry.config.pages.esp.*;
import com.nekiplay.hypixelcry.config.pages.macros.GhostBlocksMacrosPage;
import com.nekiplay.hypixelcry.config.pages.nukers.*;
import com.nekiplay.hypixelcry.config.pages.qol.DungeonsMainPage;
import com.nekiplay.hypixelcry.features.nuker.GeneralNuker;

import java.io.IOException;


public class MyConfig extends Config {
    public transient static final String GENERAL = "General";
    public transient static final String WORLD = "World";
    public transient static final String NUKER = "Nuker's";
    public transient static final String ESP = "ESP";
    public transient static final String MACROS = "Macros";
    public transient static final String COMBAT = "Combat";
    public transient static final String QOL = "QOL";
    @Checkbox(
            name = "TPS Check",
            description = "Dont break if server lag.",
            category = WORLD,
            subcategory = NUKER
    )
    public boolean GeneralNukerTPSGuard = true;

    @Page(
            name = "Garden Nuker",
            category = WORLD,
            subcategory = NUKER,
            location = PageLocation.BOTTOM
    )
    public GardenMainPage gardenMainPage = new GardenMainPage();
    //region Crop Nuker

    @Page(
            name = "Crop Nuker",
            category = WORLD,
            subcategory = NUKER,
            location = PageLocation.BOTTOM
    )
    public CropMainPage cropMainPage = new CropMainPage();

    //endregion

    //region Foraging Nuker
    @Page(
            name = "Foraging Nuker",
            category = WORLD,
            subcategory = NUKER,
            location = PageLocation.BOTTOM
    )
    public ForagingMainPage foragingMainPage = new ForagingMainPage();
    //endregion

    //region Sand Nuker
    @Page(
            name = "Sand Nuker",
            category = WORLD,
            subcategory = NUKER,
            location = PageLocation.BOTTOM
    )
    public SandMainPage sandMainPage = new SandMainPage();
    //endregion

    //region Ore Nuker
    @Page(
            name = "Ore Nuker",
            category = WORLD,
            subcategory = NUKER,
            location = PageLocation.BOTTOM
    )
    public OreMainPage oreMainPage = new OreMainPage();
    //endregion

    //region Mithril Nuker
    @Page(
            name = "Mithril Nuker",
            category = WORLD,
            subcategory = NUKER,
            location = PageLocation.BOTTOM
    )
    public MithrilMainPage mithrilMainPage = new MithrilMainPage();
    //endregion

    @Page(
            name = "Ghost Blocks",
            category = MACROS,
            subcategory = GENERAL,
            location = PageLocation.BOTTOM
    )
    public GhostBlocksMacrosPage ghostBLockMainPage = new GhostBlocksMacrosPage();

    //region Dark Monolith ESP
    @Page(
            name = "Chest ESP",
            category = ESP,
            subcategory = GENERAL,
            location = PageLocation.BOTTOM
    )
    public ChestESPMainPage chestESPMainPage = new ChestESPMainPage();

    @Page(
            name = "Resource ESP",
            category = ESP,
            subcategory = GENERAL,
            location = PageLocation.BOTTOM
    )
    public ResourceRespawnerMainPage resourceRespawnerMainPage = new ResourceRespawnerMainPage();

    @Page(
            name = "Dark Monolith",
            category = ESP,
            subcategory = "Dwarden Mines",
            location = PageLocation.BOTTOM
    )
    public DarkMonolithMainPage darkMonolithMainPage = new DarkMonolithMainPage();
    //endregion

    //region Glowing Mushroom ESP
    @Page(
            name = "Glowing Mushroom",
            category = ESP,
            subcategory = "Glowing Mushroom Island",
            location = PageLocation.BOTTOM
    )
    public GlowingMushroomMainPage glowingMushroomMainPage = new GlowingMushroomMainPage();
    //endregion

    //region Treasure Hunter ESP
    @Page(
            name = "Treasure Hunter",
            category = ESP,
            subcategory = "Glowing Mushroom Island",
            location = PageLocation.BOTTOM
    )
    public TreasureHunterMainPage treasureHunterMainPage = new TreasureHunterMainPage();

    @Page(
            name = "Pelt mobs",
            category = ESP,
            subcategory = "Glowing Mushroom Island",
            location = PageLocation.BOTTOM
    )
    public PeltMobMainPage peltMobMainPage = new PeltMobMainPage();

    @Page(
            name = "Gifts",
            category = ESP,
            subcategory = "Jerry's Workshop",
            location = PageLocation.BOTTOM
    )
    public JerryGiftsMainPage jerryGiftsMainPage = new JerryGiftsMainPage();

    @Page(
            name = "Chocolate Eggs",
            category = ESP,
            subcategory = GENERAL,
            location = PageLocation.BOTTOM
    )
    public ChocolateEggMainPage chocolateEggMainPage = new ChocolateEggMainPage();

    @Page(
            name = "Holograms",
            category = ESP,
            subcategory = GENERAL,
            location = PageLocation.BOTTOM
    )
    public HologramsMainPage hologramsMainPage = new HologramsMainPage();

    @Page(
            name = "Dungeons",
            category = QOL,
            subcategory = GENERAL,
            location = PageLocation.BOTTOM
    )
    public DungeonsMainPage dungeonsMainPage = new DungeonsMainPage();

    @Checkbox(
            name = "Debug",
            description = "Debug for devs",
            category = "About",
            subcategory = "General"
    )
    public boolean isDebug = false;

    @Button(
            name = "Telegram",
            text = "Open",
            category = "About",
            subcategory = "Author"
    )
    Runnable runnable = () -> {    // using a lambda to create the runnable interface.
        String url_open ="https://t.me/nekiplay";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    @Button(
            name = "GitHub",
            text = "Open",
            category = "About",
            subcategory = "Author"
    )
    Runnable runnableGitHub = () -> {    // using a lambda to create the runnable interface.
        String url_open ="https://github.com/Nekiplay";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };


    public MyConfig() {
        super(new Mod("Hypixel Cry", ModType.SKYBLOCK, "/assets/hypixelcry/logo.png"), "hypixelcry/hypixelcry.json");
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

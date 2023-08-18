package com.nekiplay.hypixelcry.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import com.nekiplay.hypixelcry.config.pages.combat.autoclicker.AutoClickerMainPage;
import com.nekiplay.hypixelcry.config.pages.combat.hideattack.HideAttackMainPage;
import com.nekiplay.hypixelcry.config.pages.combat.noclickdelay.NoClickDelayMainPage;
import com.nekiplay.hypixelcry.config.pages.combat.reach.ReachMainPage;
import com.nekiplay.hypixelcry.config.pages.esp.DarkMonolithMainPage;
import com.nekiplay.hypixelcry.config.pages.esp.GlowingMushroomMainPage;
import com.nekiplay.hypixelcry.config.pages.esp.JerryGiftsMainPage;
import com.nekiplay.hypixelcry.config.pages.esp.TreasureHunterMainPage;
import com.nekiplay.hypixelcry.config.pages.nukers.*;
import com.nekiplay.hypixelcry.features.nuker.GeneralNuker;

import java.io.IOException;


public class MyConfig extends Config {
    @Checkbox(
            name = "TPS Check",
            description = "Dont break if server lag.",
            category = "World",
            subcategory = "Nuker's"
    )
    public boolean GeneralNukerTPSGuard = true;

    @Page(
            name = "Garden Nuker",
            category = "World",
            subcategory = "Nuker's",
            location = PageLocation.BOTTOM
    )
    public GardenMainPage gardenMainPage = new GardenMainPage();
    //region Crop Nuker

    @Page(
            name = "Crop Nuker",
            category = "World",
            subcategory = "Nuker's",
            location = PageLocation.BOTTOM
    )
    public CropMainPage cropMainPage = new CropMainPage();

    //endregion

    //region Foraging Nuker
    @Page(
            name = "Foraging Nuker",
            category = "World",
            subcategory = "Nuker's",
            location = PageLocation.BOTTOM
    )
    public ForagingMainPage foragingMainPage = new ForagingMainPage();
    //endregion

    //region Sand Nuker
    @Page(
            name = "Sand Nuker",
            category = "World",
            subcategory = "Nuker's",
            location = PageLocation.BOTTOM
    )
    public SandMainPage sandMainPage = new SandMainPage();
    //endregion

    //region Ore Nuker
    @Page(
            name = "Ore Nuker",
            category = "World",
            subcategory = "Nuker's",
            location = PageLocation.BOTTOM
    )
    public OreMainPage oreMainPage = new OreMainPage();
    //endregion

    //region Mithril Nuker
    @Page(
            name = "Mithril Nuker",
            category = "World",
            subcategory = "Nuker's",
            location = PageLocation.BOTTOM
    )
    public MithrilMainPage mithrilMainPage = new MithrilMainPage();
    //endregion

    //region Dark Monolith ESP
    @Page(
            name = "Dark Monolith",
            category = "ESP",
            subcategory = "Dwarden Mines",
            location = PageLocation.BOTTOM
    )
    public DarkMonolithMainPage darkMonolithMainPage = new DarkMonolithMainPage();
    //endregion

    //region Glowing Mushroom ESP
    @Page(
            name = "Glowing Mushroom",
            category = "ESP",
            subcategory = "Glowing Mushroom Island",
            location = PageLocation.BOTTOM
    )
    public GlowingMushroomMainPage glowingMushroomMainPage = new GlowingMushroomMainPage();
    //endregion

    //region Treasure Hunter ESP
    @Page(
            name = "Treasure Hunter",
            category = "ESP",
            subcategory = "Glowing Mushroom Island",
            location = PageLocation.BOTTOM
    )
    public TreasureHunterMainPage treasureHunterMainPage = new TreasureHunterMainPage();

    @Page(
            name = "Gifts",
            category = "ESP",
            subcategory = "Jerry's Workshop",
            location = PageLocation.BOTTOM
    )
    public JerryGiftsMainPage jerryGiftsMainPage = new JerryGiftsMainPage();
    //endregion

    @Page(
            name = "No Click Delay",
            category = "Combat",
            subcategory = "General",
            location = PageLocation.BOTTOM
    )
    public NoClickDelayMainPage noClickDelayMainPage = new NoClickDelayMainPage();
    @Page(
            name = "Reach",
            category = "Combat",
            subcategory = "General",
            location = PageLocation.BOTTOM
    )
    public ReachMainPage reachMainPage = new ReachMainPage();


    @Page(
            name = "AutoClicker",
            category = "Combat",
            subcategory = "General",
            location = PageLocation.BOTTOM
    )
    public AutoClickerMainPage autoClickerMainPage = new AutoClickerMainPage();

    @Page(
            name = "HideAttack",
            category = "Combat",
            subcategory = "Exploits",
            location = PageLocation.BOTTOM
    )
    public HideAttackMainPage hideAttackMainPage = new HideAttackMainPage();

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
        super(new Mod("Hypixel Cry", ModType.SKYBLOCK), "hypixelcry/hypixelcry.json");
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

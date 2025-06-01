package com.nekiplay.hypixelcry.config;

import com.google.gson.annotations.Expose;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.neupages.ESP;
import com.nekiplay.hypixelcry.config.neupages.Macros;
import com.nekiplay.hypixelcry.config.neupages.Misc;
import com.nekiplay.hypixelcry.utils.Utils;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.Social;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import io.github.notenoughupdates.moulconfig.common.MyResourceLocation;

import java.util.Arrays;
import java.util.List;

public class NEUConfig extends Config {

    @Override
    public String getTitle() {
        return "§6Hypixel Cry §7v" + HypixelCry.VERSION;
    }

    @Override
    public void saveNow() {
        HypixelCry.getInstance().saveConfig();
    }

    private Social social(String name, String iconName, String link) {
        return new Social() {
            @Override
            public void onClick() {
                Utils.openUrl(link);
            }

            @Override
            public List<String> getTooltip() {
                return Arrays.asList(name, "§7Open " + link);
            }

            @Override
            public MyResourceLocation getIcon() {
                return new MyResourceLocation("hypixelcry:social", iconName + ".png");
            }
        };
    }

    @Override
    public List<Social> getSocials() {
        return Arrays.asList(
                social(
                        "YouTube",
                        "youtube",
                        "https://www.youtube.com/@Nekiplay"
                ),
                social(
                        "GitHub",
                        "github",
                        "https://github.com/Nekiplay/Hypixel-Cry"
                )
        );
    }

    @Expose
    @Category(
            name = "Visuals",
            desc = "World rendering features"
    )
    public ESP esp = new ESP();

    @Expose
    @Category(
            name = "Macros",
            desc = "Auto and semi-auto features"
    )
    public Macros macros = new Macros();

    @Expose
    @Category(
            name = "Misc",
            desc = "Options for sub features"
    )
    public Misc misc = new Misc();
}

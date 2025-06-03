package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.features.nuker.Foraging;
import com.nekiplay.hypixelcry.features.system.IslandTypeChangeChecker;
import com.nekiplay.hypixelcry.features.esp.farming.Glowing_Mushroom;
import com.nekiplay.hypixelcry.features.esp.farming.Treasure_Hunter;
import com.nekiplay.hypixelcry.features.esp.mining.dwarvenmines.Dark_Monolith;
import com.nekiplay.hypixelcry.features.esp.mining.glacitemishafts.FrozenCourpes;
import com.nekiplay.hypixelcry.features.esp.mining.crystalhollows.AutomatonESP;
import com.nekiplay.hypixelcry.features.esp.mining.crystalhollows.YogESP;
import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import com.nekiplay.hypixelcry.features.esp.pathFinders.detections.crystalhollows.JungleTemple;
import com.nekiplay.hypixelcry.features.macros.AutoChestClose;
import com.nekiplay.hypixelcry.features.esp.*;
import com.nekiplay.hypixelcry.features.macros.*;
import com.nekiplay.hypixelcry.features.system.RotationHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Arrays;

public class FeatureRegister {
    public void register(FMLInitializationEvent event) {
        Arrays.asList(
                /* Macros */
                new AutoChestClose(),
                new GhostBlocks(),
                new AutoRightClick(),

                /* ESP */
                new ChestESP(),
                new FrozenCourpes(),
                new AutomatonESP(),
                new YogESP(),
                new Dark_Monolith(),
                new Glowing_Mushroom(),

                /* Nukers */
                new Foraging(),

                new JungleTemple(),
                new Treasure_Hunter(),
                new PathFinderRenderer()
        ).forEach(MinecraftForge.EVENT_BUS::register);
    }

    public void registerSystemFeatures(FMLInitializationEvent event) {
        Arrays.asList(
                new IslandTypeChangeChecker(),
                RotationHandler.getInstance()
        ).forEach(MinecraftForge.EVENT_BUS::register);
    }
}

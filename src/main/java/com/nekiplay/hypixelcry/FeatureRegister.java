package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.features.system.IslandTypeChangeChecker;
import com.nekiplay.hypixelcry.features.esp.farming.Glowing_Mushroom;
import com.nekiplay.hypixelcry.features.esp.farming.Treasure_Hunter;
import com.nekiplay.hypixelcry.features.esp.mining.dwarvenmines.Dark_Monolith;
import com.nekiplay.hypixelcry.features.esp.mining.glacitemishafts.FrozenCourpes;
import com.nekiplay.hypixelcry.features.esp.mining.crystalhollows.AutomatonESP;
import com.nekiplay.hypixelcry.features.esp.mining.crystalhollows.YogESP;
import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import com.nekiplay.hypixelcry.features.esp.pathFinders.detections.crystalhollows.JungleTemple;
import com.nekiplay.hypixelcry.features.qol.AutoChestClose;
import com.nekiplay.hypixelcry.features.esp.*;
import com.nekiplay.hypixelcry.features.macros.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class FeatureRegister {
    public static AutoChestClose autoChestClose = new AutoChestClose();


    public static GhostBlocks ghostBlocksMacros = new GhostBlocks();

    public void register(FMLInitializationEvent event) {
        /* Events */

        MinecraftForge.EVENT_BUS.register(autoChestClose);

        MinecraftForge.EVENT_BUS.register(ghostBlocksMacros);


        /* ESP */
        MinecraftForge.EVENT_BUS.register(new JungleTemple());

        MinecraftForge.EVENT_BUS.register(new FrozenCourpes());
        MinecraftForge.EVENT_BUS.register(new AutoChestOpen());
        MinecraftForge.EVENT_BUS.register(new AutomatonESP());
        MinecraftForge.EVENT_BUS.register(new YogESP());
        MinecraftForge.EVENT_BUS.register(new ChestESP());
        MinecraftForge.EVENT_BUS.register(new Treasure_Hunter());
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());
        MinecraftForge.EVENT_BUS.register(new Glowing_Mushroom());
        MinecraftForge.EVENT_BUS.register(new Gifts());

        MinecraftForge.EVENT_BUS.register(new PathFinderRenderer());
    }

    public void registerSystemFeatures(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new IslandTypeChangeChecker());
    }
}

package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.features.esp.mobs.AutomatonESP;
import com.nekiplay.hypixelcry.features.esp.mobs.YogESP;
import com.nekiplay.hypixelcry.features.qol.AutoChestClose;
import com.nekiplay.hypixelcry.features.esp.*;
import com.nekiplay.hypixelcry.features.esp.holograms.HologramModule;
import com.nekiplay.hypixelcry.features.macros.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class FeatureRegister {
    public static AutoChestClose autoChestClose = new AutoChestClose();


    public static GhostBlocks ghostBlocksMacros = new GhostBlocks();

    public static HologramModule hologramModule = new HologramModule();
    public void register(FMLInitializationEvent event) {
        /* Events */

        MinecraftForge.EVENT_BUS.register(autoChestClose);

        MinecraftForge.EVENT_BUS.register(ghostBlocksMacros);


        /* ESP */
        MinecraftForge.EVENT_BUS.register(new AutomatonESP());
        MinecraftForge.EVENT_BUS.register(new YogESP());
        MinecraftForge.EVENT_BUS.register(new ResourceRespawnerESP());
        MinecraftForge.EVENT_BUS.register(new ChestESP());
        MinecraftForge.EVENT_BUS.register(new Treasure_Hunter());
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());
        MinecraftForge.EVENT_BUS.register(new Glowing_Mushroom());
        MinecraftForge.EVENT_BUS.register(new PeltMobEsp());
        MinecraftForge.EVENT_BUS.register(new Gifts());
        MinecraftForge.EVENT_BUS.register(new ChocolateEgg());
        MinecraftForge.EVENT_BUS.register(new PathFinderRenderer());
    }
}

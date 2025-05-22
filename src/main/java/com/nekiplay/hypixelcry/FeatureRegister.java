package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.features.qol.AutoChestClose;
import com.nekiplay.hypixelcry.config.CustomRenderer;
import com.nekiplay.hypixelcry.features.esp.*;
import com.nekiplay.hypixelcry.features.esp.holograms.HologramModule;
import com.nekiplay.hypixelcry.features.macros.*;
import com.nekiplay.hypixelcry.features.nuker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class FeatureRegister {
    public static AutoChestClose autoChestClose = new AutoChestClose();


    public static GhostBlocks ghostBlocksMacros = new GhostBlocks();

    public static Crop cropNuker = new Crop();
    public static Foraging foragingNuker = new Foraging();
    public static GardenMiner gardenNuker = new GardenMiner();
    public static Mithril mithrilNuker = new Mithril();
    public static Ore oreNuker = new Ore();
    public static Sand sandNuker = new Sand();

    public static HologramModule hologramModule = new HologramModule();
    public void register(FMLInitializationEvent event) {
        /* Events */
        MinecraftForge.EVENT_BUS.register(new CustomRenderer());

        MinecraftForge.EVENT_BUS.register(autoChestClose);

        MinecraftForge.EVENT_BUS.register(ghostBlocksMacros);


        /* ESP */
        MinecraftForge.EVENT_BUS.register(new ResourceRespawnerESP());
        MinecraftForge.EVENT_BUS.register(new ChestESP());
        MinecraftForge.EVENT_BUS.register(new Treasure_Hunter());
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());
        MinecraftForge.EVENT_BUS.register(new Glowing_Mushroom());
        MinecraftForge.EVENT_BUS.register(new PeltMobEsp());
        MinecraftForge.EVENT_BUS.register(new Gifts());
        MinecraftForge.EVENT_BUS.register(new ChocolateEgg());
    }
}

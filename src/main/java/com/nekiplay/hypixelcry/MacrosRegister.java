package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.features.combat.HideAttack;
import com.nekiplay.hypixelcry.auto.RemoteAccess;
import com.nekiplay.hypixelcry.config.CustomRenderer;
import com.nekiplay.hypixelcry.features.combat.NoClickDelay;
import com.nekiplay.hypixelcry.features.esp.Dark_Monolith;
import com.nekiplay.hypixelcry.features.esp.Gifts;
import com.nekiplay.hypixelcry.features.esp.Glowing_Mushroom;
import com.nekiplay.hypixelcry.features.esp.Treasure_Hunter;
import com.nekiplay.hypixelcry.features.macros.AspectoftheEnd;
import com.nekiplay.hypixelcry.features.combat.AutoClicker;
import com.nekiplay.hypixelcry.features.macros.RogueSword;
import com.nekiplay.hypixelcry.features.macros.WandofHealing;
import com.nekiplay.hypixelcry.features.nuker.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class MacrosRegister {
    public void register(FMLInitializationEvent event) {
        /* Events */
        MinecraftForge.EVENT_BUS.register(new CustomRenderer());

        /* Macros */
        MinecraftForge.EVENT_BUS.register(new RogueSword());
        MinecraftForge.EVENT_BUS.register(new WandofHealing());
        MinecraftForge.EVENT_BUS.register(new AspectoftheEnd());

        /* ESP */
        MinecraftForge.EVENT_BUS.register(new Treasure_Hunter());
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());
        MinecraftForge.EVENT_BUS.register(new Glowing_Mushroom());
        MinecraftForge.EVENT_BUS.register(new Gifts());

        /* Nuker */
        MinecraftForge.EVENT_BUS.register(new Mithril());
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Ore());
        MinecraftForge.EVENT_BUS.register(new Crop());
        MinecraftForge.EVENT_BUS.register(new Sand());
        MinecraftForge.EVENT_BUS.register(new GardenMiner());

        MinecraftForge.EVENT_BUS.register(new RemoteAccess());
        MinecraftForge.EVENT_BUS.register(new HideAttack());
        MinecraftForge.EVENT_BUS.register(new AutoClicker());
        MinecraftForge.EVENT_BUS.register(new NoClickDelay());
    }
}

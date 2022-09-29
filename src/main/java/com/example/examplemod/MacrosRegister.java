package com.example.examplemod;

import com.example.examplemod.auto.HideAttack;
import com.example.examplemod.auto.RemoteAccess;
import com.example.examplemod.esp.Dark_Monolith;
import com.example.examplemod.macros.AspectoftheEnd;
import com.example.examplemod.macros.AutoClicker;
import com.example.examplemod.macros.RogueSword;
import com.example.examplemod.macros.WandofHealing;
import com.example.examplemod.nuker.Crop;
import com.example.examplemod.nuker.Foraging;
import com.example.examplemod.nuker.Ore;
import com.example.examplemod.nuker.Sand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class MacrosRegister {
    public void register(FMLInitializationEvent event) {
        /* Macros */
        MinecraftForge.EVENT_BUS.register(new RogueSword());
        MinecraftForge.EVENT_BUS.register(new WandofHealing());
        MinecraftForge.EVENT_BUS.register(new AspectoftheEnd());

        /* ESP */
        MinecraftForge.EVENT_BUS.register(new Dark_Monolith());

        /* Nuker */
        MinecraftForge.EVENT_BUS.register(new Foraging());
        MinecraftForge.EVENT_BUS.register(new Ore());
        MinecraftForge.EVENT_BUS.register(new Crop());
        MinecraftForge.EVENT_BUS.register(new Sand());

        MinecraftForge.EVENT_BUS.register(new RemoteAccess());
        MinecraftForge.EVENT_BUS.register(new HideAttack());
        MinecraftForge.EVENT_BUS.register(new AutoClicker());
    }
}

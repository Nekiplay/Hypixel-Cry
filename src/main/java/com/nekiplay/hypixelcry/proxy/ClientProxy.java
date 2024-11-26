package com.nekiplay.hypixelcry.proxy;

import com.nekiplay.hypixelcry.FeatureRegister;
import com.nekiplay.hypixelcry.commands.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

import static com.nekiplay.hypixelcry.Main.features;
import static com.nekiplay.hypixelcry.Main.keyBindings;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        features.register(event);

        keyBindings = new KeyBinding[7];
        keyBindings[0] = new KeyBinding("Crop nuker", Keyboard.KEY_NONE, "Hypixel Cry | Nuker");
        keyBindings[1] = new KeyBinding("Foraging nuker", Keyboard.KEY_NONE, "Hypixel Cry | Nuker");
        keyBindings[2] = new KeyBinding("Garden nuker", Keyboard.KEY_NONE, "Hypixel Cry | Nuker");
        keyBindings[3] = new KeyBinding("Mithril nuker", Keyboard.KEY_NONE, "Hypixel Cry | Nuker");
        keyBindings[4] = new KeyBinding("Ore nuker", Keyboard.KEY_NONE, "Hypixel Cry | Nuker");
        keyBindings[5] = new KeyBinding("Sand nuker", Keyboard.KEY_NONE, "Hypixel Cry | Nuker");


        keyBindings[6] = new KeyBinding("Ghost Blocks", Keyboard.KEY_NONE, "Hypixel Cry | Macros");
        for (KeyBinding keyBinding : keyBindings) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }
        ClientCommandHandler.instance.registerCommand(new EntityInfoCommand());
        ClientCommandHandler.instance.registerCommand(new SetAngle());
        ClientCommandHandler.instance.registerCommand(new TPS());
        ClientCommandHandler.instance.registerCommand(new OpenSettings());
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }
}

package com.nekiplay.hypixelcry;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nekiplay.hypixelcry.config.NEUConfig;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class HypixelCry implements ModInitializer  {
    public static final String MOD_ID = "hypixelcry";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final IEventBus EVENT_BUS = new EventBus();
    public static ManagedConfig<NEUConfig> config;
    @Override
    public void onInitialize() {

        config = ManagedConfig.create(new File("config/hypixelcry/config.json"), NEUConfig.class);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("cry")
                    .executes(context -> {
                        // Логика команды
                        config.openConfigGui();

                        context.getSource().sendFeedback(Text.literal("😭"));
                        return 1;
                    })
            );
        });

        Runtime.getRuntime().addShutdownHook(new Thread(config::saveToFile));

        LOGGER.info("Hello Fabric world!");
    }

    private void RegisterEvents() {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((a, b) -> {

            return true;
        });
    }
}

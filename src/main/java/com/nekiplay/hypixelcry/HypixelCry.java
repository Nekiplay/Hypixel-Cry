package com.nekiplay.hypixelcry;

import com.nekiplay.hypixelcry.annotations.Init;
import com.nekiplay.hypixelcry.config.NEUConfig;
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class HypixelCry implements ClientModInitializer   {
    public static final String MOD_ID = "hypixelcry";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final IEventBus EVENT_BUS = new EventBus();
    public static ManagedConfig<NEUConfig> config;

    private static HypixelCry INSTANCE;

    /**
     * Do not instantiate this class. Use {@link #getInstance()} instead.
     */
    @Deprecated
    public HypixelCry() {
        INSTANCE = this;
    }

    public static HypixelCry getInstance() {
        return INSTANCE;
    }


    @Override
    public void onInitializeClient() {

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

        init();

        LOGGER.info("Hello Fabric world!");
    }

    /**
     * This method is responsible for initializing all classes.
     * To have your class initialized you must annotate its initializer method with the {@code @Init} annotation.
     * At compile time, ASM completely overwrites the content of this method, so adding a call here will do nothing.
     *
     * @see Init
     */
    private static void init() {
    }
}

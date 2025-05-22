package com.nekiplay.hypixelcry.config;

import com.google.gson.annotations.Expose;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.neupages.ESP;
import com.nekiplay.hypixelcry.config.neupages.Macros;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class NEUConfig extends Config {

    @Override
    public String getTitle() {
        return "§6Hypixel Cry §7v" + Main.VERSION;
    }

    @Override
    public void executeRunnable(int runnableId) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Just executed runnableId " + runnableId));
    }


    @Expose
    @Category(
            name = "ESP",
            desc = "World rendering features"
    )
    public ESP esp = new ESP();

    @Expose
    @Category(
            name = "Macros",
            desc = "Keybind features"
    )
    public Macros macros = new Macros();
}

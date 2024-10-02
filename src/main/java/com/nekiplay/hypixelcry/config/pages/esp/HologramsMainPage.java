package com.nekiplay.hypixelcry.config.pages.esp;

import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import com.nekiplay.hypixelcry.FeatureRegister;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class HologramsMainPage {
    @Checkbox(
            name = "Enabled",
            description = "Enable/Disable render holograms",
            category = "General",
            subcategory = "General"
    )
    public boolean enableHolograms = true;
    @Button(
            name = "Reload holograms",
            text = "Reload holograms",
            category = "General",
            subcategory = "General"
    )
    Runnable runnableGitHub = () -> {    // using a lambda to create the runnable interface.
        FeatureRegister.hologramModule.reload();
    };
}

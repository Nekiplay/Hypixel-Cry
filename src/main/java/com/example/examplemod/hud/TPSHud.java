package com.example.examplemod.hud;

import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import com.example.examplemod.utils.world.TickRate;

public class TPSHud extends SingleTextHud {

    @Slider(
            name = "Decimals",
            min = 0.0f,
            max = 4.0f,
            step = 1
    )
    public int decimals = 1;
    public TPSHud() {
        super("TPS", true);
    }

    @Override
    protected String getText(boolean example) {
        return String.format("%." + decimals + "f", TickRate.INSTANCE.getTickRate());
    }
}

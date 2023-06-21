package com.nekiplay.hypixelcry.hud;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import com.nekiplay.hypixelcry.utils.world.TickRate;

public class TPSHud extends Config {
    public TPSHud() {
        super(new Mod("TPS", ModType.HUD), "hypixelcry/hud/tps.json");
        initialize();
    }
    @HUD( name = "TPS" )
    public TPSHudC tps = new TPSHudC();

    public static class TPSHudC extends SingleTextHud {
        @Slider(
                name = "Decimals",
                min = 0.0f,
                max = 4.0f,
                step = 1
        )
        public int decimals = 1;

        public TPSHudC() {
            super("TPS", true);
        }


        @Override
        protected String getText(boolean example) {
            return String.format("%." + decimals + "f", TickRate.INSTANCE.getTickRate());
        }
    }
}

package com.nekiplay.hypixelcry.hud;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import com.nekiplay.hypixelcry.utils.world.TickRate;

public class TimeSinceLastTickHud extends Config {
    public TimeSinceLastTickHud() {
        super(new Mod("Time Since Last Tick", ModType.HUD), "hypixelcry/hud/timesincelasttick.json");
        initialize();
    }
    @HUD( name = "Time Since Last Tick" )
    public TimeSinceLastTickC tps = new TimeSinceLastTickC();

    public static class TimeSinceLastTickC extends SingleTextHud {
        @Slider(
                name = "Decimals",
                min = 0.0f,
                max = 4.0f,
                step = 1
        )
        public int decimals = 1;

        @Checkbox(
                name = "Only is lagging",
                description = "Show if only if server lagging"
        )
        public boolean onlyIsLagging = false;

        @Override
        protected boolean shouldShow() {
            return super.shouldShow() && (!onlyIsLagging || TickRate.INSTANCE.getTimeSinceLastTick() > 1.2f);

        }

        public TimeSinceLastTickC() {
            super("Time Since Last Tick", true);

        }

        @Override
        protected String getText(boolean example) {
            return String.format("%." + decimals + "f", TickRate.INSTANCE.getTimeSinceLastTick());
        }
    }
}
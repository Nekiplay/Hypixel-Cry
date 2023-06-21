package com.nekiplay.hypixelcry.nuker;

import com.nekiplay.hypixelcry.Main;

public class GeneralMiner {
    private int ground_ticks = 0;

    public boolean AllowInstantMining() {
        if (Main.mc.thePlayer.onGround && ground_ticks > 4)
        {
            return true;
        }
        else if (Main.mc.thePlayer.onGround) {
            ground_ticks++;
        }
        else if (!Main.mc.thePlayer.onGround) {
            ground_ticks = 0;
        }
        return false;
    }
}

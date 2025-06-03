package com.nekiplay.hypixelcry.features.nuker;


import static com.nekiplay.hypixelcry.HypixelCry.mc;

public class GeneralMiner {
    private int ground_ticks = 0;

    public boolean AllowInstantMining() {
        if (mc.thePlayer.onGround && ground_ticks > 4)
        {
            return true;
        }
        else if (mc.thePlayer.onGround) {
            ground_ticks++;
        }
        else if (!mc.thePlayer.onGround) {
            ground_ticks = 0;
        }
        return false;
    }
}
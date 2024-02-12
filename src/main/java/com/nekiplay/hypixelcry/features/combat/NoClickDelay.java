package com.nekiplay.hypixelcry.features.combat;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.MillisecondEvent;
import com.nekiplay.hypixelcry.mixins.MinecraftAccessor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class NoClickDelay {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (myConfigFile != null && myConfigFile.noClickDelayMainPage.enabled) {
            try {
                MinecraftAccessor mca = (MinecraftAccessor) mc;
                mca.setLeftClickDelay(0);
            }
            catch (Exception ignore) { }
        }
    }
}

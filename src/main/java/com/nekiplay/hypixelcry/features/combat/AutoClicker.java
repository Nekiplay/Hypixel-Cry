package com.nekiplay.hypixelcry.features.combat;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.events.MillisecondEvent;
import com.nekiplay.hypixelcry.mixins.MinecraftAccessor;
import com.nekiplay.hypixelcry.utils.Perlin2D;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class AutoClicker {
    private Robot bot;

    public AutoClicker() {
        try {
            bot = new Robot();
        } catch (AWTException ignore) {
        }
    }

    private long lastClickTime = 0;

    public static int PerlinNoice(int multiply) {
        Perlin2D perlin = new Perlin2D(new Random().nextInt());
        float Phi = 0.70710678118f;
        float noice = perlin.Noise(5, 5) + perlin.Noise((25 - 25) * Phi, (25 + 25) * Phi) * -1;
        return (int) (noice * multiply);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void TickEvent(MillisecondEvent event) {
        if (Main.keyBindings[8].isKeyDown() && Main.mc.thePlayer != null)
        {
            if (System.currentTimeMillis() - lastClickTime > (long) 1000 / myConfigFile.autoClickerMainPage.CPS) {
                try {
                    if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY || mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
                        lastClickTime = System.currentTimeMillis() + PerlinNoice(myConfigFile.autoClickerMainPage.Randomization) - 1;
                        MinecraftAccessor mca = (MinecraftAccessor) Main.mc;
                        mca.clickMouse();
                    }
                }
                catch (Exception ignore) { }
            }
            //bot.mousePress(InputEvent.BUTTON1_MASK);
            //bot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }
}

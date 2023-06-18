package com.example.examplemod.macros;

import com.example.examplemod.Main;
import com.example.examplemod.events.MillisecondEvent;
import com.example.examplemod.mixins.MinecraftAccessor;
import com.example.examplemod.utils.Perlin2D;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;

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
            if (System.currentTimeMillis() - lastClickTime > (long) 40 + PerlinNoice(15) - 1) {
                try {
                    lastClickTime = System.currentTimeMillis();
                    MinecraftAccessor mc = (MinecraftAccessor) Main.mc;
                    mc.clickMouse();
                }
                catch (Exception ignore) { }
            }
            //bot.mousePress(InputEvent.BUTTON1_MASK);
            //bot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }
}

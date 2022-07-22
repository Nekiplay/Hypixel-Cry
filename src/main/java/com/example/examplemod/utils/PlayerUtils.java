package com.example.examplemod.utils;

import com.example.examplemod.Main;
import net.minecraft.util.MovingObjectPosition;

public class PlayerUtils {
    public static void swingItem() {
        MovingObjectPosition movingObjectPosition = Main.mc.objectMouseOver;
        if (movingObjectPosition != null && movingObjectPosition.entityHit == null) {
            Main.mc.thePlayer.swingItem();
        }
    }
}

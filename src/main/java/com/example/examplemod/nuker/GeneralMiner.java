package com.example.examplemod.nuker;

import com.example.examplemod.Main;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

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

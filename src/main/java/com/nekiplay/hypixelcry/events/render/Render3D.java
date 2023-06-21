package com.nekiplay.hypixelcry.events.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class Render3D extends Event {
    public float partialTicks;

    public Render3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

package com.nekiplay.hypixelcry.events;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntityMove extends Event {
    public EntityLiving entity;
    public Vec3 location;

    public EntityMove(EntityLiving entity, Vec3 location) {
        this.entity = entity;
        this.location = location;
    }
}

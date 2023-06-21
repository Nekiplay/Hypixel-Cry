package com.nekiplay.hypixelcry.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AttackEntity extends Event  {
    public EntityPlayer player;
    public Entity attacked;

    public AttackEntity(EntityPlayer player, Entity entity) {
        this.player = player;
        this.attacked = entity;
    }
}

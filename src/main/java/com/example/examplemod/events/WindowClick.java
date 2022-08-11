package com.example.examplemod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WindowClick extends Event {
    public int windowId;
    public int slotId;
    public int mouseButtonClicked;
    public int mode;
    public EntityPlayer playerIn;

    public WindowClick(int windowId, int slotId, int mouseButtonClicked, int mode, EntityPlayer playerIn) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.mouseButtonClicked = mouseButtonClicked;
        this.mode = mode;
        this.playerIn = playerIn;
    }
}

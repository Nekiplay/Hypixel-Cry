package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.events.minecraft.render.RenderEntityModelEvent;
import com.nekiplay.hypixelcry.utils.OutlineUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class TestESP {
    @SubscribeEvent
    public void onUnloadWorld(RenderEntityModelEvent event)
    {
        OutlineUtils.outlineEntity(event, Color.RED, 5, false);
    }
}

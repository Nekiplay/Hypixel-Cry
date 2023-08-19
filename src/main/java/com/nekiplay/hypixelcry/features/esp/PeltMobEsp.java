package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.Main.myConfigFile;

public class PeltMobEsp {
    private Entity rendered = null;
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        if (myConfigFile != null && myConfigFile.peltMobMainPage.EnableESP) {
            double dist = 99999;
            if (mc.theWorld != null) {
                List<Entity> entityList = mc.theWorld.getLoadedEntityList();
                for (Entity entity : entityList) {
                    if (entity.hasCustomName()) {
                        String title = entity.getDisplayName().getFormattedText();
                        if (title.contains("Trackable") || title.contains("Untrackable") || title.contains("Undetected") || title.contains("Endangered") || title.contains("Elusive")) {
                            double distance = mc.thePlayer.getDistanceSqToEntity(entity);
                            if (dist <= dist) {
                                rendered = entity;
                                dist = distance;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {

        if (rendered != null && myConfigFile != null && myConfigFile.peltMobMainPage.EnableESP) {
            RenderUtils.drawEntityBox(rendered, myConfigFile.peltMobMainPage.Color.toJavaColor(), 1, event.partialTicks);
            if (myConfigFile.peltMobMainPage.EnableTracer) {
                RenderUtils.drawTracer(rendered, myConfigFile.peltMobMainPage.Color.toJavaColor(), event.partialTicks);
            }
        }

    }
}

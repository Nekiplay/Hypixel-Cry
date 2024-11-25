package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
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
        if (myConfigFile != null && myConfigFile.peltMobMainPage.enableESP) {
            double dist = 99999;
            if (mc.theWorld != null) {
                List<Entity> entityList = mc.theWorld.getLoadedEntityList();
                rendered = null;
                for (Entity entity : entityList) {
                    String nametag = EntityUtils.getCustomNametag(entity);
                    if (nametag != null) {
                        if (nametag.contains("Trackable") || nametag.contains("Untrackable") || nametag.contains("Undetected") || nametag.contains("Endangered") || nametag.contains("Elusive")) {
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

        if (rendered != null && myConfigFile != null && myConfigFile.peltMobMainPage.enableESP) {
            RenderUtils.renderWaypointText("Pelt Mob", new BlockPos(rendered.getPosition().getX() + 0.5, rendered.getPosition().getY() + 0.5, rendered.getPosition().getZ() + 0.5), event.partialTicks, false, myConfigFile.peltMobMainPage.color.toJavaColor());
            if (myConfigFile.peltMobMainPage.enableTracer) {
                RenderUtils.drawTracer(rendered.getPosition(), myConfigFile.peltMobMainPage.color.toJavaColor(), 1, event.partialTicks);
            }
        }

    }
}

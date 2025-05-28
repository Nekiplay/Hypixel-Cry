package com.nekiplay.hypixelcry.features.esp;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import com.nekiplay.hypixelcry.utils.SpecialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nekiplay.hypixelcry.Main.mc;

public class FrozenCourpes {
    public List<EntityArmorStand> courses = new ArrayList<EntityArmorStand>();

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (Main.mc.theWorld != null) {
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandHeadName(armorStand);
                    if (head != null && !head.isEmpty()) {
                        if (head.contains("Lapis Armor Helmet")) {
                            if (!courses.contains((armorStand))) {
                                courses.add(armorStand);

                                if (Main.config.esp.glaciteTunnels.frozenCourpes.enabledPathFinder) {
                                    PathFinderRenderer.addOrUpdatePath(Integer.toString(armorStand.getEntityId()), armorStand.getPosition(), SpecialColor.toSpecialColor(Main.config.esp.glaciteTunnels.frozenCourpes.colour), "Courpe");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event)
    {
        for (EntityArmorStand armorStand : courses) {
            if (PathFinderRenderer.hasPath(Integer.toString(armorStand.getEntityId()))) {
                PathFinderRenderer.removePath(Integer.toString(armorStand.getEntityId()));
            }
        }
        courses.clear();
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.config.esp.glaciteTunnels.frozenCourpes.enabled)
        {
            for (EntityArmorStand entity : courses) {
                if (Main.config.esp.chestEsp.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawEntityBox(entity, SpecialColor.toSpecialColor(Main.config.esp.glaciteTunnels.frozenCourpes.colour), 1, event.partialTicks);
                }
                if (Main.config.esp.chestEsp.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Courpe", new BlockPos(entity.posX + 0.5, entity.posY + entity.height + 0.5, entity.posZ + 0.5), event.partialTicks, false, SpecialColor.toSpecialColor(Main.config.esp.glaciteTunnels.frozenCourpes.colour));
                }
                if (Main.config.esp.chestEsp.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(entity.getPosition(), SpecialColor.toSpecialColor(Main.config.esp.glaciteTunnels.frozenCourpes.colour), 1, event.partialTicks);
                }
            }
        }
    }
}

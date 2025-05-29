package com.nekiplay.hypixelcry.features.esp.mining.glacitemishafts;

import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.ESPFeatures;
import com.nekiplay.hypixelcry.config.neupages.ESP;
import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.nekiplay.hypixelcry.Main.mc;
import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class FrozenCourpes {
    private final Set<EntityArmorStand> courses = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Set<Integer> removedPathFinders = ConcurrentHashMap.newKeySet();
    private EntityArmorStand currentTarget = null;
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 1000; // 1 second in milliseconds

    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START || mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime < UPDATE_INTERVAL) {
            return;
        }
        lastUpdateTime = currentTime;

        // Update course list
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityArmorStand && !courses.contains(entity)) {
                EntityArmorStand armorStand = (EntityArmorStand) entity;
                String head = EntityUtils.getArmorStandHeadName(armorStand);
                if (head != null && head.contains("Lapis Armor Helmet")) {
                    courses.add(armorStand);
                }
            }
        }

        // Clean up invalid entities
        courses.removeIf(armorStand -> !armorStand.isEntityAlive() || mc.theWorld.getEntityByID(armorStand.getEntityId()) == null);

        // Handle path finding based on priority
        if (Main.config.esp.glaciteMineshafts.frozenCourpes.enabledPathFinder) {
            if (Main.config.esp.glaciteMineshafts.frozenCourpes.priority == ESP.Glacite_Mineshafts.Frozen_Courpes.Priority.All) {
                handleAllPriority();
            } else if (Main.config.esp.glaciteMineshafts.frozenCourpes.priority == ESP.Glacite_Mineshafts.Frozen_Courpes.Priority.Nearest) {
                handleNearestPriority();
            }
        }

        // Check if player reached any target
        for (EntityArmorStand armorStand : courses) {
            if (armorStand.getDistanceToEntity(mc.thePlayer) <= 7) {
                String entityId = Integer.toString(armorStand.getEntityId());
                if (PathFinderRenderer.hasPath(entityId)) {
                    PathFinderRenderer.removePath(entityId);
                }
                removedPathFinders.add(armorStand.getEntityId());
                if (armorStand.equals(currentTarget)) {
                    currentTarget = null;
                }
            }
        }
    }

    private void handleAllPriority() {
        for (EntityArmorStand armorStand : courses) {
            if (!removedPathFinders.contains(armorStand.getEntityId())) {
                PathFinderRenderer.addOrUpdatePath(
                        Integer.toString(armorStand.getEntityId()),
                        armorStand.getPosition(),
                        toSpecialColor(Main.config.esp.glaciteMineshafts.frozenCourpes.colour),
                        "Courpe"
                );
            }
        }
    }

    private void handleNearestPriority() {
        // Remove all existing paths first
        for (EntityArmorStand armorStand : courses) {
            PathFinderRenderer.removePath(Integer.toString(armorStand.getEntityId()));
        }

        // Find the nearest valid target
        currentTarget = courses.stream()
                .filter(armorStand -> !removedPathFinders.contains(armorStand.getEntityId()))
                .min(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)))
                .orElse(null);

        if (currentTarget != null) {
            PathFinderRenderer.addOrUpdatePath(
                    Integer.toString(currentTarget.getEntityId()),
                    currentTarget.getPosition(),
                    toSpecialColor(Main.config.esp.glaciteMineshafts.frozenCourpes.colour),
                    "Courpe"
            );
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        courses.forEach(armorStand ->
                PathFinderRenderer.removePath(Integer.toString(armorStand.getEntityId())));
        courses.clear();
        removedPathFinders.clear();
        currentTarget = null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Main.config.esp.glaciteMineshafts.frozenCourpes.enabled) {
            for (EntityArmorStand entity : courses) {
                if (entity == null || !entity.isEntityAlive()) continue;

                BlockPos pos = new BlockPos(entity.posX + 0.5, entity.posY + entity.height + 0.5, entity.posZ + 0.5);
                Color color = toSpecialColor(Main.config.esp.glaciteMineshafts.frozenCourpes.colour);

                if (Main.config.esp.glaciteMineshafts.frozenCourpes.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawEntityBox(entity, color, 1, event.partialTicks);
                }
                if (Main.config.esp.glaciteMineshafts.frozenCourpes.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Courpe", pos, event.partialTicks, false, color);
                }
                if (Main.config.esp.glaciteMineshafts.frozenCourpes.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(entity.getPosition(), color, 1, event.partialTicks);
                }
            }
        }
    }
}
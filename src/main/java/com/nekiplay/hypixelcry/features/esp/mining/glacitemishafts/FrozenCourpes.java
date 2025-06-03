package com.nekiplay.hypixelcry.features.esp.mining.glacitemishafts;

import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.config.enums.ESPFeatures;
import com.nekiplay.hypixelcry.config.enums.PathFinderPriority;
import com.nekiplay.hypixelcry.features.esp.pathFinders.PathFinderRenderer;
import com.nekiplay.hypixelcry.utils.EntityUtils;
import com.nekiplay.hypixelcry.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.nekiplay.hypixelcry.HypixelCry.mc;
import static com.nekiplay.hypixelcry.utils.SpecialColor.toSpecialColor;

public class FrozenCourpes {
    public List<EntityArmorStand> courses = new ArrayList<EntityArmorStand>();
    private final List<Integer> removedPathFinders = new ArrayList<>();
    private EntityArmorStand currentTarget = null;

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            return;
        }
        if (HypixelCry.mc.theWorld != null && HypixelCry.mc.thePlayer != null) {
            // Update course list
            for (Entity entity : mc.theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityArmorStand) {
                    EntityArmorStand armorStand = (EntityArmorStand) entity;
                    String head = EntityUtils.getArmorStandHeadName(armorStand);
                    if (head != null && head.contains("Lapis Armor Helmet")) {
                        if (!courses.contains(armorStand)) {
                            courses.add(armorStand);
                        }
                    }
                }
            }

            // Handle path finding based on priority
            if (HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.enabledPathFinder) {
                if (HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.priority == PathFinderPriority.All) {
                    // Original behavior for All priority
                    for (EntityArmorStand armorStand : courses) {
                        if (!removedPathFinders.contains(armorStand.getEntityId())) {
                            PathFinderRenderer.addOrUpdatePath(Integer.toString(armorStand.getEntityId()), armorStand.getPosition(),
                                    toSpecialColor(HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.colour), "Courpe");
                        }
                    }
                } else if (HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.priority == PathFinderPriority.Nearest) {
                    // New behavior for Nearest priority
                    handleNearestPriority();
                }
            }

            // Check if player reached any target
            for (EntityArmorStand armorStand : new ArrayList<>(courses)) {
                double distance = HypixelCry.mc.thePlayer.getDistanceToEntity(armorStand);
                if (distance <= 7) {
                    if (PathFinderRenderer.hasPath(Integer.toString(armorStand.getEntityId()))) {
                        PathFinderRenderer.removePath(Integer.toString(armorStand.getEntityId()));
                    }
                    if (!removedPathFinders.contains(armorStand.getEntityId())) {
                        removedPathFinders.add(armorStand.getEntityId());
                    }
                    if (armorStand.equals(currentTarget)) {
                        currentTarget = null; // Reset current target if reached
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void handleNearestPriority() {
        // Remove all existing paths first
        for (EntityArmorStand armorStand : courses) {
            PathFinderRenderer.removePath(Integer.toString(armorStand.getEntityId()));
        }

        // Filter out reached targets and find the nearest one
        List<EntityArmorStand> validTargets = new ArrayList<>();
        for (EntityArmorStand armorStand : courses) {
            if (!removedPathFinders.contains(armorStand.getEntityId())) {
                validTargets.add(armorStand);
            }
        }

        if (!validTargets.isEmpty()) {
            // Sort by distance to player
            validTargets.sort(Comparator.comparingDouble(e -> HypixelCry.mc.thePlayer.getDistanceToEntity(e)));

            // If we don't have a current target or the current target is no longer valid
            if (currentTarget == null || !validTargets.contains(currentTarget)) {
                currentTarget = validTargets.get(0); // Get the nearest
            }

            // Add path only to the current target
            PathFinderRenderer.addOrUpdatePath(Integer.toString(currentTarget.getEntityId()),
                    currentTarget.getPosition(),
                    toSpecialColor(HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.colour),
                    "Courpe");
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        for (EntityArmorStand armorStand : courses) {
            if (PathFinderRenderer.hasPath(Integer.toString(armorStand.getEntityId()))) {
                PathFinderRenderer.removePath(Integer.toString(armorStand.getEntityId()));
            }
        }
        courses.clear();
        removedPathFinders.clear();
        currentTarget = null;
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.enabled) {
            for (EntityArmorStand entity : courses) {
                if (HypixelCry.config.esp.chestEsp.features.contains(ESPFeatures.Box)) {
                    RenderUtils.drawEntityBox(entity, toSpecialColor(HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.colour), 1, event.partialTicks);
                }
                if (HypixelCry.config.esp.chestEsp.features.contains(ESPFeatures.Text)) {
                    RenderUtils.renderWaypointText("Courpe", new Vec3(entity.posX + 0.5, entity.posY + entity.height + 0.5, entity.posZ + 0.5), event.partialTicks, false, toSpecialColor(HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.colour));
                }
                if (HypixelCry.config.esp.chestEsp.features.contains(ESPFeatures.Tracer)) {
                    RenderUtils.drawTracer(entity.getPosition(), toSpecialColor(HypixelCry.config.esp.glaciteMineshafts.frozenCourpes.colour), 1, event.partialTicks);
                }
            }
        }
    }
}
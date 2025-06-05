package com.nekiplay.hypixelcry.events.render;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class WorldRenderEvent {
    private static final WorldRenderEvent INSTANCE = new WorldRenderEvent();

    public MatrixStack matrixStack;
    public Camera camera;
    public float partialTicks;

    public static WorldRenderEvent get(MatrixStack matrixStack, Camera camera, float partialTicks) {
        INSTANCE.matrixStack = matrixStack;
        INSTANCE.camera = camera;
        INSTANCE.partialTicks = partialTicks;

        return INSTANCE;
    }
}

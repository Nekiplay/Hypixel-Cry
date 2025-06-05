package com.nekiplay.hypixelcry.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.nekiplay.hypixelcry.HypixelCry;
import com.nekiplay.hypixelcry.events.render.GameRenderEvent;
import com.nekiplay.hypixelcry.events.render.WorldRenderEvent;
import com.nekiplay.hypixelcry.events.world.BlockUpdateEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract MinecraftClient getClient();

    @Shadow
    @Final
    private Camera camera;

    @Shadow
    public abstract void tick();

    @Shadow
    @Final
    private LightmapTextureManager lightmapTextureManager;

    @Inject(method = "render", at = @At("HEAD"))
    public void hookGameRender(CallbackInfo callbackInfo) {
        HypixelCry.EVENT_BUS.post(new GameRenderEvent());
    }

    @Inject(method = "renderWorld", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    public void hookWorldRender(RenderTickCounter renderTickCounter, CallbackInfo ci, @Local(ordinal = 2) Matrix4f matrix4f2) {
        // TODO: Improve this
        var newMatStack = new MatrixStack();

        newMatStack.multiplyPositionMatrix(matrix4f2);

        HypixelCry.EVENT_BUS.post(WorldRenderEvent.get(newMatStack, this.camera, renderTickCounter.getTickDelta(false)));
    }
}

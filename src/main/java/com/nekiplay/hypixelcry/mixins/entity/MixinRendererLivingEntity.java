package com.nekiplay.hypixelcry.mixins.entity;

import com.nekiplay.hypixelcry.events.minecraft.render.RenderEntityModelEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> {
    @Shadow
    protected ModelBase mainModel;

    @Inject(method = "renderLayers", at = @At("RETURN"), cancellable = true)
    private void onRenderLayers(T entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float partialTicks, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_, CallbackInfo ci) {
        RenderEntityModelEvent renderEntityModelEvent = new RenderEntityModelEvent(
                entitylivingbaseIn, p_177093_2_, p_177093_3_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_, mainModel
        );
        //MinecraftForge.EVENT_BUS.post(renderEntityModelEvent);
        if (renderEntityModelEvent.isCanceled()) {
            ci.cancel();
        }
    }
}

package com.nekiplay.hypixelcry.mixins.render;

import cc.polyfrost.oneconfig.events.EventManager;
import com.nekiplay.hypixelcry.events.EntityMove;
import com.nekiplay.hypixelcry.events.render.Render3D;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
@SideOnly(Side.CLIENT)
public class MixinEntityRenderer {
    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        Render3D render3D = new Render3D(partialTicks);
        MinecraftForge.EVENT_BUS.post(render3D);
    }
}

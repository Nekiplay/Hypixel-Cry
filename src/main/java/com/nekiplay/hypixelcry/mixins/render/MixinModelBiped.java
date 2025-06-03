package com.nekiplay.hypixelcry.mixins.render;

import com.nekiplay.hypixelcry.features.system.RotationHandler;
import com.nekiplay.hypixelcry.mixins.client.MinecraftAccessor;
import com.nekiplay.hypixelcry.mixins.entity.EntityPlayerSPAccessor;
import com.nekiplay.hypixelcry.utils.AngleUtils;
import com.nekiplay.hypixelcry.utils.helper.RotationConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelBiped.class, priority = Integer.MAX_VALUE)
public class MixinModelBiped {

    @Unique
    private final Minecraft hypixelCry$mc = Minecraft.getMinecraft();
    @Shadow
    public ModelRenderer bipedHead;

    @Inject(method = {"setRotationAngles"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelBiped;swingProgress:F")})
    public void onSetRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!RotationHandler.getInstance().isEnabled() || RotationHandler.getInstance().getConfiguration() != null && RotationHandler.getInstance().getConfiguration().getRotationType() != RotationConfiguration.RotationType.SERVER || entityIn == null || !entityIn.equals(hypixelCry$mc.thePlayer)) {
            return;
        }

        this.bipedHead.rotateAngleX = ((EntityPlayerSPAccessor) entityIn).getLastReportedPitch() / 57.295776f;
        float partialTicks = ((MinecraftAccessor) hypixelCry$mc).getTimer().renderPartialTicks;
        float yawOffset = hypixelCry$mc.thePlayer.renderYawOffset + AngleUtils.normalizeAngle(hypixelCry$mc.thePlayer.renderYawOffset - hypixelCry$mc.thePlayer.prevRenderYawOffset) * partialTicks;
        float calcNetHead = MathHelper.wrapAngleTo180_float(((EntityPlayerSPAccessor) entityIn).getLastReportedYaw() - yawOffset);
        this.bipedHead.rotateAngleY = calcNetHead / 57.295776f;
    }
}
package com.nekiplay.hypixelcry.mixins.render;

import com.google.common.base.Predicates;
import com.nekiplay.hypixelcry.Main;
import com.nekiplay.hypixelcry.config.pages.combat.reach.ReachMainPage;
import com.nekiplay.hypixelcry.events.render.Render3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EntityRenderer.class)
@SideOnly(Side.CLIENT)
public class MixinEntityRenderer {
    @Shadow
    private Entity pointedEntity;
    @Shadow
    private Minecraft mc;

    @Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
    private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        Render3D render3D = new Render3D(partialTicks);
        MinecraftForge.EVENT_BUS.post(render3D);
    }

    @Inject(method = "getMouseOver", at = @At("HEAD"), cancellable = true)
    private void getMouseOver(float p_getMouseOver_1_, CallbackInfo ci) {
        try {
            Entity entity = this.mc.getRenderViewEntity();
            if (entity != null && this.mc.theWorld != null) {
                this.mc.mcProfiler.startSection("pick");
                this.mc.pointedEntity = null;

                final ReachMainPage reach = Main.myConfigFile.reachMainPage;

                assert reach != null;
                double d0 = reach.enabled ? reach.combatRange : (double) this.mc.playerController.getBlockReachDistance();
                this.mc.objectMouseOver = entity.rayTrace(this.mc.playerController.getBlockReachDistance(), p_getMouseOver_1_);
                double d1 = d0;
                Vec3 vec3 = entity.getPositionEyes(p_getMouseOver_1_);
                boolean flag = false;
                if (this.mc.playerController.extendedReach()) {
                    d0 = 6.0D;
                    d1 = 6.0D;
                } else if (d0 > 3.0D) {
                    flag = true;
                }

                if (this.mc.objectMouseOver != null) {
                    d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
                }

                if (reach.enabled) {
                    d1 = reach.combatRange;

                    final MovingObjectPosition movingObjectPosition = entity.rayTrace(d1, p_getMouseOver_1_);

                    if (movingObjectPosition != null) d1 = movingObjectPosition.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = entity.getLook(p_getMouseOver_1_);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                this.pointedEntity = null;
                Vec3 vec33 = null;
                float f = 1.0F;
                List<Entity> list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, p_apply_1_ -> p_apply_1_.canBeCollidedWith()));
                double d2 = d1;

                for (int j = 0; j < list.size(); ++j) {
                    Entity entity1 = list.get(j);
                    if (entity1 != null) {
                        float f1 = entity1.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                        if (axisalignedbb.isVecInside(vec3)) {
                            if (d2 >= 0.0D) {
                                this.pointedEntity = entity1;
                                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        } else if (movingobjectposition != null) {
                            double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                            if (d3 < d2 || d2 == 0.0D) {
                                if (entity1 == entity.ridingEntity && !entity.canRiderInteract()) {
                                    if (d2 == 0.0D) {
                                        this.pointedEntity = entity1;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                } else {
                                    this.pointedEntity = entity1;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }

                if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > (reach.enabled ? reach.combatRange : 3.0D)) {
                    this.pointedEntity = null;
                    this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing) null, new BlockPos(vec33));
                }

                if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                    this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);
                    if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                        this.mc.pointedEntity = this.pointedEntity;
                    }
                }

                this.mc.mcProfiler.endSection();
            }

            ci.cancel();
        }
        catch (Exception ignored) {

        }
    }
}

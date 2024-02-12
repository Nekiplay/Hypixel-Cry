package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.BlockClick;
import com.nekiplay.hypixelcry.events.EntityMove;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinMinecraft {
    @Inject(method = "clickBlock", at = @At("HEAD"))
    private void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        BlockClick blockClick = new BlockClick(loc);
        MinecraftForge.EVENT_BUS.post(blockClick);
    }
}

package com.nekiplay.hypixelcry.mixins;

import com.nekiplay.hypixelcry.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        PacketEvent.Send send = new PacketEvent.Send(packet);
        MinecraftForge.EVENT_BUS.post(send);
        if (send.isCanceled())
            callbackInfo.cancel();
    }
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        PacketEvent.Recive recive = new PacketEvent.Recive(packet);
        MinecraftForge.EVENT_BUS.post(recive);
        if (recive.isCanceled())
            ci.cancel();
    }
}
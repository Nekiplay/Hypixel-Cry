package com.nekiplay.hypixelcry.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Accessor("leftClickCounter")
    void setLeftClickDelay(int delay);

    @Invoker("clickMouse")
    void clickMouse();

    @Invoker("rightClickMouse")
    void rightClickMouse();

    @Accessor("timer")
    Timer getTimer();
}

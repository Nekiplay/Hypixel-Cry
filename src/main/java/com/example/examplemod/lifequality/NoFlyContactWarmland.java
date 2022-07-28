package com.example.examplemod.lifequality;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class NoFlyContactWarmland {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        KeyBinding bind = FMLClientHandler.instance().getClient().gameSettings.keyBindForward;
        if (bind.isKeyDown()) { //Player is going forwards, make them stop
            KeyBinding.setKeyBindState(bind.getKeyCode(), false);
        }
    }
}

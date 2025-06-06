package com.nekiplay.hypixelcry.events;

import com.nekiplay.hypixelcry.utils.misc.input.KeyAction;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public class MouseButtonEvent {
    public static final Event<MouseButtonEvent.KeyCallback> EVENT = EventFactory.createArrayBacked(
            MouseButtonEvent.KeyCallback.class,
            (listeners) -> (event) -> {
                for (MouseButtonEvent.KeyCallback listener : listeners) {
                    ActionResult result = listener.onKeyEvent(event);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    private int button;
    private KeyAction action;

    public MouseButtonEvent(int button, KeyAction action) {
        this.button = button;
        this.action = action;
    }

    public int getButton() {
        return button;
    }

    public KeyAction getAction() {
        return action;
    }

    public interface KeyCallback {
        ActionResult onKeyEvent(MouseButtonEvent event);
    }
}

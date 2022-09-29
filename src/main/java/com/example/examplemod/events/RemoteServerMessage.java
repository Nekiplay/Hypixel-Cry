package com.example.examplemod.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class RemoteServerMessage extends Event {
    public String message;

    public RemoteServerMessage(String message) {
        this.message = message;
    }
}

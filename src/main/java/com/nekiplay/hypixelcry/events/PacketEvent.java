package com.nekiplay.hypixelcry.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent extends Event {
    public Packet<?> packet;
    public PacketEvent(Packet<?> packet)
    {
        this.packet = packet;
    }

    public static class Recive extends PacketEvent {
        public Recive(Packet<?> packet) {
            super(packet);
        }
    }
    public static class Send extends PacketEvent {
        public Packet<?> packet;
        public Send(Packet<?> packet) {
            super(packet);
        }
    }
}
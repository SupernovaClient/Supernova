package me.royalty.supernova.events.network;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

public class EventReceivePacket extends EventCancellable {

    @Getter @Setter
    private Packet<?> packet;

    public EventReceivePacket(Packet<?> packet) {
        this.packet = packet;
    }
}

package io.github.nevalackin.events.network;

import best.azura.eventbus.core.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

public class EventSendPacket implements Event {

	@Getter
	@Setter
	private Packet<?> packet;
	public boolean cancelled;

	public EventSendPacket(Packet<?> packet) {
		this.packet = packet;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}

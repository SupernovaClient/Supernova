package io.github.nevalackin.events.network;

import best.azura.eventbus.core.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket implements Event {

	private Packet<?> packet;

	public boolean isCancelled;

	public EventReceivePacket(Packet<?> packet) {
		this.packet = packet;
	}

	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}
}

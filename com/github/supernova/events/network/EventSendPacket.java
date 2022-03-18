package com.github.supernova.events.network;

import best.azura.eventbus.core.Event;
import net.minecraft.network.Packet;

public class EventSendPacket implements Event {

	private Packet<?> packet;
	public boolean cancelled;

	public EventSendPacket(Packet<?> packet) {
		this.packet = packet;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}
}

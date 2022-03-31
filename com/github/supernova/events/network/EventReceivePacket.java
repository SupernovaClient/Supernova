package com.github.supernova.events.network;

import best.azura.eventbus.core.Event;
import net.minecraft.network.Packet;

public class EventReceivePacket implements Event {

	private Packet<?> packet;

	public boolean cancelled;

	public EventReceivePacket(Packet<?> packet) {
		this.packet = packet;
	}

	public void setCancelled(boolean cancelled) {
		cancelled = cancelled;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}
}

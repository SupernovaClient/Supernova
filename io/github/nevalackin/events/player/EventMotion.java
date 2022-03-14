package io.github.nevalackin.events.player;

import best.azura.eventbus.core.Event;
import lombok.Getter;
import lombok.Setter;

public class EventMotion implements Event {
	@Getter
	@Setter
	private double x, y, z;
	@Getter
	@Setter
	private boolean pre = true;
	public boolean cancelled;

	public EventMotion(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}

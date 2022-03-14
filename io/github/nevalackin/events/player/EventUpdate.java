package io.github.nevalackin.events.player;

import best.azura.eventbus.core.Event;
import lombok.Getter;
import lombok.Setter;

public class EventUpdate implements Event {
	@Getter
	@Setter
	private double x, y, z;
	@Getter
	@Setter
	private float yaw, pitch;
	@Getter
	@Setter
	private boolean onGround;

	public boolean cancelled;
	@Getter
	@Setter
	private boolean pre;

	public EventUpdate(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean isPre) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.pre = isPre;
	}

	public EventUpdate(double x, double y, double z, float yaw, float pitch, boolean onGround) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.onGround = onGround;
		this.pre = true;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}

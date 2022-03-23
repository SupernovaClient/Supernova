package com.github.supernova.events.player;

import best.azura.eventbus.core.Event;

public class EventMotion implements Event {
	private double x, y, z;
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

	public double getZ() {
		return z;
	}

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setPre(boolean pre) {
		this.pre = pre;
	}

	public boolean pre() {
		return pre;
	}
}

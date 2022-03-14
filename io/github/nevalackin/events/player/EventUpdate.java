package io.github.nevalackin.events.player;

import best.azura.eventbus.core.Event;

public class EventUpdate implements Event {
	private double x, y, z;
	private float yaw, pitch;
	private boolean onGround;

	public boolean cancelled;
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

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public double getX() {
		return x;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setPre(boolean pre) {
		this.pre = pre;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public boolean isPre() {
		return pre;
	}
	public boolean isPost() {
		return !pre;
	}

	public boolean isOnGround() {
		return onGround;
	}
}

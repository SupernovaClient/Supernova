package io.github.nevalackin.events.misc;

import best.azura.eventbus.core.Event;
import org.lwjgl.input.Keyboard;

public class EventKey implements Event {

	private final int keyCode;

	public EventKey(int keyCode) {
		this.keyCode = keyCode;
	}

	public String getKey() {
		return Keyboard.getKeyName(this.keyCode);
	}

	public int getKeyCode() {
		return keyCode;
	}
}

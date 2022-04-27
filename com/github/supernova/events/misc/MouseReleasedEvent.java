package com.github.supernova.events.misc;

import best.azura.eventbus.core.Event;

public class MouseReleasedEvent implements Event {
    private final float mouseX, mouseY;

    public MouseReleasedEvent(float mouseX, float mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public float getMouseY() {
        return mouseY;
    }
    public float getMouseX() {
        return mouseX;
    }
}

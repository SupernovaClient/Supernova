package com.github.supernova.events.misc;

import best.azura.eventbus.core.Event;

public class MouseClickedEvent implements Event {

    private final float mouseX,mouseY;
    private final int mouseButton;
    private boolean isUsed = false;

    public MouseClickedEvent(float mouseX, float mouseY, int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
    }

    public float getMouseX() {
        return mouseX;
    }
    public float getMouseY() {
        return mouseY;
    }
    public int getMouseButton() {
        return mouseButton;
    }
    public boolean shouldUse() {
        return !isUsed;
    }
    public void setUsed(boolean used) {
        this.isUsed = used;
    }
}

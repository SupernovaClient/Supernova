package com.github.supernova.events.render;

import best.azura.eventbus.core.Event;

public class EventRender3D implements Event {

    private final float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}

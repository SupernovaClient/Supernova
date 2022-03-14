package io.github.nevalackin.events.render;

import best.azura.eventbus.core.Event;
import lombok.Getter;

public class EventRender2D implements Event {

    @Getter
    private final float partialTicks;

    public EventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
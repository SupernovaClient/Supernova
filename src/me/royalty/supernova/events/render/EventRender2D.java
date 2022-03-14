package me.royalty.supernova.events.render;

import com.darkmagician6.eventapi.events.callables.Event;
import lombok.Getter;

public class EventRender2D extends Event {

    @Getter
    private final float partialTicks;

    public EventRender2D(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
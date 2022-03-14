package me.royalty.supernova.events.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.Getter;
import lombok.Setter;

public class EventMotion extends EventCancellable {
    @Getter @Setter
    private double x,y,z;
    @Getter @Setter
    private boolean pre = true;

    public EventMotion(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

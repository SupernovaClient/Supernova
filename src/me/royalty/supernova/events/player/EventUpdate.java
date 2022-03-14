package me.royalty.supernova.events.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.Getter;
import lombok.Setter;

public class EventUpdate extends EventCancellable {
    @Getter @Setter
    private double x, y, z;
    @Getter @Setter
    private float yaw, pitch;
    @Getter @Setter
    private boolean onGround;

    @Getter @Setter
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

}

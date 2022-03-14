package me.royalty.supernova.events.misc;

import com.darkmagician6.eventapi.events.callables.Event;
import lombok.Getter;
import org.lwjgl.input.Keyboard;

public class EventKey extends Event {

    @Getter
    private final int keyCode;

    public EventKey(int keyCode) {
        this.keyCode = keyCode;
    }
    public String getKey() {
        return Keyboard.getKeyName(this.keyCode);
    }

}

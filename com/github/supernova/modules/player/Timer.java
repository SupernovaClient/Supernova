package com.github.supernova.modules.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.value.impl.NumberValue;

@ModuleAnnotation(name = "Timer", displayName = "Timer", description = "send the c03", category = Category.PLAYER)
public class Timer extends Module {
    public final NumberValue timerSpeedValue = new NumberValue("Timer Speed", 1, 0.1, 10, 0.1);

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = event -> {
        if(mc.timer.timerSpeed != timerSpeedValue.getFloat()) {
            mc.timer.timerSpeed = timerSpeedValue.getFloat();
        }
    };
}

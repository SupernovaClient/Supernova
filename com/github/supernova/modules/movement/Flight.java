package com.github.supernova.modules.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.player.MovementUtil;

@ModuleAnnotation(name = "Flight", displayName = "Flight", description = "Fly", category = Category.MOVEMENT)
public class Flight extends Module {
    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = event -> {
        mc.thePlayer.motionY = mc.thePlayer.isSneaking() ? -0.5 : mc.gameSettings.keyBindJump.isKeyDown() ? 0.5 : 0;
        if(mc.thePlayer.isMoving()) {
            mc.thePlayer.setSpeed(4);
        }
        mc.thePlayer.capabilities.allowFlying = true;
        mc.thePlayer.setSprinting(true);
    };
}

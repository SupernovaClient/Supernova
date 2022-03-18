package com.github.supernova.modules.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.player.MovementUtil;
import com.github.supernova.value.impl.BooleanValue;
import com.github.supernova.value.impl.EnumValue;

@ModuleAnnotation(name = "Speed", category = Category.MOVEMENT, displayName = "Speed")
public class Speed extends Module {
	public EnumValue<mode> modes = new EnumValue<>("Modes", mode.WATCHDOG);
	public BooleanValue jump = new BooleanValue("Jump", false);

	public Speed() {
		setValues(modes, jump);
	}

	@EventHandler
	public Listener<EventUpdate> eventUpdateListener = eventUpdate -> {
		if (mc.thePlayer.onGround) {
			if (MovementUtil.isMoving()) {
				mc.thePlayer.setSpeed(0.42);
				if (jump.getCurrentValue()) mc.thePlayer.jump();
			} else mc.thePlayer.setSpeed(0);
		}
	};

	public enum mode {

		WATCHDOG("Watchdog");

		mode(String name) {
		}

	}
}

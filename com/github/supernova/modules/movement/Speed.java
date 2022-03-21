package com.github.supernova.modules.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.util.player.MovementUtil;
import com.github.supernova.value.impl.BooleanValue;
import com.github.supernova.value.impl.EnumValue;

@ModuleAnnotation(name = "Speed", category = Category.MOVEMENT, displayName = "Speed")
public class Speed extends Module {
	public EnumValue<Mode> modes = new EnumValue<>("Modes", Mode.WATCHDOG);
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

	public enum Mode implements ModeEnum {

		WATCHDOG("Watchdog");

		private final String name;
		Mode(String name) {
			this.name = name;
		}
		@Override
		public String getName() {
			return name;
		}
	}
}

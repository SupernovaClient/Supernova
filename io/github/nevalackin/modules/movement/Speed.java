package io.github.nevalackin.modules.movement;

import io.github.nevalackin.modules.Category;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.modules.ModuleAnnotation;
import io.github.nevalackin.value.impl.MultiEnumValue;

@ModuleAnnotation(name = "Speed", category = Category.MOVEMENT,displayName = "Speed")
public class Speed extends Module {
	public MultiEnumValue<mode> modes = new MultiEnumValue<>("Modes", Speed.mode.values(), mode.WATCHDOG);

	public Speed() {
		setValues(modes);
	}

	public enum mode {

		WATCHDOG("Watchdog");

		mode(String name) {
		}

	}
}

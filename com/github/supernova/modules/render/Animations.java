package com.github.supernova.modules.render;

import com.github.supernova.modules.Category;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.value.impl.EnumValue;

@ModuleAnnotation(category = Category.RENDER, displayName = "Animations", name = "Animations")
public class Animations {

	private final EnumValue<modes> modesEnumValue = new EnumValue<>("Mode", modes.OLD);

	enum modes implements ModeEnum {
		OLD("1.7"),
		NORMAL("1.8"),
		SWANK("Swank"),
		SWONG("Swong"),
		SWING("Swing");

		private String name;
		modes(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return null;
		}
	}
}

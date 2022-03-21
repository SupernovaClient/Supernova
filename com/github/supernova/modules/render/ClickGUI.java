package com.github.supernova.modules.render;

import com.github.supernova.Supernova;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.value.impl.EnumValue;
import org.lwjgl.input.Keyboard;

@ModuleAnnotation(displayName = "Click GUI", description = "Clicky Thingy", category = Category.RENDER, name = "Click GUI")

public class ClickGUI extends Module {

	EnumValue<GuiMode> guiModeValue = new EnumValue<>("Mode", GuiMode.DROPDOWN);

	public ClickGUI() {
		setValues(guiModeValue);
		setKeyCode(Keyboard.KEY_RSHIFT);
	}

	@Override
	public void onEnable() {

		if (guiModeValue.getCurrentValue() == GuiMode.DROPDOWN || guiModeValue.getCurrentValue() == GuiMode.ASTOLFO) {
			mc.displayGuiScreen(Supernova.INSTANCE.getClickGui());
		}

		super.onEnable();
		toggleModule();
	}


	enum GuiMode implements ModeEnum {
		DROPDOWN("Dropdown"),
		ASTOLFO("Astolfo");

		final String name;

		GuiMode(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}

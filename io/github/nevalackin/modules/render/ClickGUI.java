package io.github.nevalackin.modules.render;

import io.github.nevalackin.Supernova;
import io.github.nevalackin.modules.Category;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.value.impl.EnumValue;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {


    EnumValue<GuiMode> guiModeValue = new EnumValue<>("Mode", GuiMode.DROPDOWN);

    public ClickGUI() {
        super("Click GUI", "CLicky Thingy", Category.RENDER, false);
        setValues(guiModeValue);
        setKeyCode(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {

        if (guiModeValue.getCurrentValue() == GuiMode.DROPDOWN) {
            mc.displayGuiScreen(Supernova.INSTANCE.getClickGui());
        }

        super.onEnable();
        toggleModule();
    }


    enum GuiMode {
        DROPDOWN("Dropdown");

        final String name;
        GuiMode(String name) {
            this.name = name;
        }
    }
}

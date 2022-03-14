package io.github.nevalackin.gui.guiscreen.click.impl.module.values;

import io.github.nevalackin.gui.guiscreen.click.impl.module.ModuleDropdown;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.value.impl.NumberValue;

import java.io.IOException;

public class NumberComponent extends ValueComponent {

    private NumberValue value;
    private ModuleDropdown parent;

    public NumberComponent(ModuleDropdown parent, NumberValue value) {
        this.parent = parent;
        this.value = value;
    }

    @Override
    public void render(float posX, float posY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }
}

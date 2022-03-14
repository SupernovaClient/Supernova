package io.github.nevalackin.gui.guiscreen.click.impl.module.values;

import io.github.nevalackin.gui.guiscreen.click.impl.module.ModuleDropdown;
import io.github.nevalackin.value.impl.BooleanValue;

import java.io.IOException;

public class BooleanComponent extends ValueComponent {

    private ModuleDropdown parent;
    private BooleanValue value;

    public BooleanComponent(ModuleDropdown parent, BooleanValue value) {
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

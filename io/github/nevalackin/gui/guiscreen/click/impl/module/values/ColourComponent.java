package io.github.nevalackin.gui.guiscreen.click.impl.module.values;

import io.github.nevalackin.gui.guiscreen.click.impl.module.ModuleDropdown;
import io.github.nevalackin.value.impl.ColourValue;

import java.io.IOException;

public class ColourComponent extends ValueComponent {

    private ModuleDropdown parent;
    private ColourValue value;

    public ColourComponent(ModuleDropdown parent, ColourValue value) {
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

package io.github.nevalackin.gui.guiscreen.click.impl;

import net.minecraft.client.Minecraft;

import java.io.IOException;

public abstract class Component {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static final float COMPONENT_HEIGHT = 20;
    public static float COMPONENT_WIDTH = 120;

    public float getComponentHeight() {
        return COMPONENT_HEIGHT;
    }
    public float getComponentWidth() {
        return COMPONENT_WIDTH;
    }

    public abstract void render(float posX, float posY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;
    public abstract void mouseReleased(int mouseX, int mouseY, int state);
    public abstract void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);
}

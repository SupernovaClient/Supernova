package com.github.supernova.gui.guiscreen.click.impl.module.values;

import com.github.supernova.gui.guiscreen.click.impl.module.ModuleDropdown;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.impl.EnumValue;

import javax.vecmath.Vector2f;
import java.io.IOException;

public class ModeComponent extends ValueComponent {

    private ModuleDropdown parent;
    private EnumValue<? extends ModeEnum> value;

    public ModeComponent(ModuleDropdown parent, EnumValue<? extends ModeEnum> value) {
        this.parent = parent;
        this.value = value;
    }

    private float posX, posY;
    @Override
    public void render(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        RenderUtil.drawRectWidth(posX,posY,getComponentWidth(),getComponentHeight(),0xFF3A3A3A);
        Vector2f mouse = MouseUtil.getMousePos();
        mc.blockyFontObj.drawStringWithShadow(value.getValueName(),posX+5,posY+COMPONENT_HEIGHT/2f-mc.blockyFontObj.FONT_HEIGHT/2f,
                hoveredComponent((int) mouse.x, (int) mouse.y) ? 0xFFEEEEEE : 0xFFDADADA);
        int width = mc.blockyFontObj.getStringWidth(value.getValueString());
        mc.blockyFontObj.drawStringWithShadow(value.getValueString(),posX+COMPONENT_WIDTH-width-5,posY+COMPONENT_HEIGHT/2f-mc.blockyFontObj.FONT_HEIGHT/2f, 0xFFDADADA);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(hovered(mouseX,mouseY)) {
            if(mouseButton == 1) {
                value.decrement();
            } else if (mouseButton == 0) {
                value.increment();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }
    public boolean hovered(int mouseX, int mouseY) {
        return mouseX > posX && mouseX < posX + getComponentWidth() &&
                mouseY > posY && mouseY < posY + getComponentHeight();
    }
    public boolean hoveredComponent(int mouseX, int mouseY) {
        return mouseX > posX && mouseX < posX + COMPONENT_WIDTH &&
                mouseY > posY && mouseY < posY + COMPONENT_HEIGHT;
    }
}

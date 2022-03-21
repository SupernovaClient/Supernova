package com.github.supernova.gui.guiscreen.click.impl.module.values;

import com.github.supernova.gui.guiscreen.click.impl.module.ModuleDropdown;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.impl.MultiEnumValue;

import java.io.IOException;
import java.util.ArrayList;

public class MultiModeComponent extends ValueComponent {

    private ModuleDropdown parent;
    private MultiEnumValue<? extends ModeEnum> value;
    private boolean expanded = false;

    public MultiModeComponent(ModuleDropdown parent, MultiEnumValue<?> value) {
        this.parent = parent;
        this.value = value;
    }

    private float posX, posY;
    @Override
    public void render(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        RenderUtil.drawRectWidth(posX,posY,getComponentWidth(),getComponentHeight(),0xFF3A3A3A);
        mc.blockyFontObj.drawStringWithShadow(value.getValueName(),posX+5,posY+COMPONENT_HEIGHT/2f-mc.blockyFontObj.FONT_HEIGHT/2f, 0xFFDADADA);
        String expandedString = expanded ? "-" : ">";
        int expandedColour = expanded ? 0xFFBABABA : 0xFFDADADA;
        mc.blockyFontObj.drawStringWithShadow(expandedString, this.posX + COMPONENT_WIDTH - 10, this.posY + 1 + (COMPONENT_HEIGHT / 2f - mc.blockyFontObj.FONT_HEIGHT / 2f), expandedColour);
        if(expanded) {
            posY += COMPONENT_HEIGHT;
            for(String mode : value.getAllValuesString()) {
                int width = mc.blockyFontObj.getStringWidth(mode);
                mc.blockyFontObj.drawStringWithShadow(mode, this.posX + COMPONENT_WIDTH - 5 - width, posY + 1 + (COMPONENT_HEIGHT / 2f - mc.blockyFontObj.FONT_HEIGHT / 2f), 0xffdadada);
                posY += COMPONENT_HEIGHT;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(hoveredComponent(mouseX,mouseY)) {
            if(mouseButton == 0 || mouseButton == 1) {
                expanded = !expanded;
            }
        } else if (expanded && hovered(mouseX,mouseY)) {
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

    private boolean hoveredComponent(int mouseX, int mouseY) {
        return mouseX > posX && mouseX < posX + COMPONENT_WIDTH &&
                mouseY > posY && mouseY < posY + COMPONENT_HEIGHT;
    }

    @Override
    public float getComponentHeight() {
        return expanded ? super.getComponentHeight()+(value.getAllValues().size()*COMPONENT_HEIGHT) :super.getComponentHeight();
    }
}

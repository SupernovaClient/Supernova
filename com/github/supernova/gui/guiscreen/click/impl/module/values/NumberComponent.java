package com.github.supernova.gui.guiscreen.click.impl.module.values;

import com.github.supernova.gui.guiscreen.click.impl.module.ModuleDropdown;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.modules.render.HUD;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.math.MathUtil;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.impl.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.io.IOException;
import java.math.BigDecimal;

public class NumberComponent extends ValueComponent {

    private NumberValue value;
    private ModuleDropdown parent;

    public NumberComponent(ModuleDropdown parent, NumberValue value) {
        this.parent = parent;
        this.value = value;
    }

    private float posX, posY;
    @Override
    public void render(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        Vector2f mouse = MouseUtil.getMousePos();
        boolean doInt = value.getIncrement() % 1 == 0;
        double currentValue = value.getDouble();
        double minValue = value.getMin();
        double maxValue = value.getMax();
        if(!Mouse.isButtonDown(0)) {
            buttonHeld = false;
        }
        if(buttonHeld) {
            int dx = (int)(mouse.x - (posX+2));
            dx = (int) Math.max(0,Math.min(dx,COMPONENT_WIDTH-4));
            float percentage = dx / (COMPONENT_WIDTH-4);
            double newValue = (maxValue-minValue)*percentage+minValue;
            newValue = MathUtil.roundToNearest(newValue, value.getIncrement());
            value.setCurrentValue(newValue);
        }
        double valuePercentage = (currentValue-minValue) / (maxValue-minValue);
        HUD hudModule = (HUD)ModuleManager.INSTANCE.get(HUD.class);
        RenderUtil.drawRectWidth(posX,posY,getComponentWidth(),getComponentHeight(),0xFF3A3A3A);
        double scale = 0.6;
        RenderUtil.drawRectWidth(posX+2,posY+2,(COMPONENT_WIDTH-4)*(float)valuePercentage, COMPONENT_HEIGHT-4,hudModule.hudColourValue.getCurrentValue());
        RenderUtil.drawRectOutlineWidth(posX+2,posY+2,(COMPONENT_WIDTH-4), COMPONENT_HEIGHT-4,0xFF303030,0.8f);
        mc.blockyFontObj.drawStringWithShadow(value.getValueName(),posX+5,posY+COMPONENT_HEIGHT/2f-mc.blockyFontObj.FONT_HEIGHT/2f,
                hoveredComponent((int) mouse.x, (int) mouse.y) ? 0xFFEEEEEE : 0xFFDADADA);
        String format = "%."+ BigDecimal.valueOf(value.getIncrement()).scale() +"f";
        String currentValueString = doInt ? (int)currentValue+"" : String.format(format, currentValue);
        int width = mc.blockyFontObj.getStringWidth(currentValueString);
        mc.blockyFontObj.drawStringWithShadow(currentValueString,posX+COMPONENT_WIDTH-5-width,posY+COMPONENT_HEIGHT/2f-mc.blockyFontObj.FONT_HEIGHT/2f, 0xFFDADADA);
    }

    private boolean buttonHeld = false;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0 && hoveredSlider(mouseX,mouseY)) {
            buttonHeld = true;
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
    public boolean hoveredSlider(int mouseX, int mouseY) {
        return mouseX+2 > posX && mouseX+2 < posX + COMPONENT_WIDTH-4 &&
                mouseY+2 > posY && mouseY+2 < posY + COMPONENT_HEIGHT-4;
    }
}

package com.github.supernova.gui.guiscreen.click.impl.module.values;

import com.github.supernova.gui.guiscreen.click.impl.module.ModuleDropdown;
import com.github.supernova.util.input.MouseUtil;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.impl.ColourValue;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.io.IOException;

public class ColourComponent extends ValueComponent {

    private ModuleDropdown parent;
    private ColourValue value;
    private Modes currentPickerMode = Modes.HUE;

    private float hue, saturation, brightness;

    public ColourComponent(ModuleDropdown parent, ColourValue value) {
        this.parent = parent;
        this.value = value;
        float[] hsb = Color.RGBtoHSB(value.getCurrentValue().getRed(),
                value.getCurrentValue().getGreen(),
                value.getCurrentValue().getBlue(), null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
    }

    private void updateValue() {
        value.setCurrentValue(Color.getHSBColor(hue,saturation,brightness));
    }
    boolean setValue = false;
    private float posX, posY;
    @Override
    public void render(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        RenderUtil.drawRectWidth(posX,posY,getComponentWidth(),getComponentHeight(),0xFF3A3A3A);
        posX += 3;
        posY += 3;

        float minXPos = posX+1.2f;
        float maxXPos = posX+getComponentWidth()-7.2f;
        float toalLength = maxXPos - minXPos;
        Vector2f mouse = MouseUtil.getMousePos();

        if(setValue) {
            if(hoveredColour((int)mouse.x,(int)mouse.y) && Mouse.isButtonDown(0)) {
                float deltaX = mouse.x - minXPos;
                float percentage = deltaX / toalLength;
                percentage = Math.min(1,Math.max(0, percentage));
                if(currentPickerMode == Modes.HUE) {
                    hue = percentage;
                } else if (currentPickerMode == Modes.SATURATION) {
                    saturation = percentage;
                } else if (currentPickerMode == Modes.BRIGHTNESS) {
                    brightness = percentage;
                }
                updateValue();
            }
        }
        float currentXPos = 0;
        float length = getComponentWidth()/6;
        RenderUtil.drawRectOutlineWidth(posX,posY,getComponentWidth()-6,getComponentHeight()-6, value.getInt(), 1);
        switch (currentPickerMode.name()) {
            case "HUE":
                currentXPos = hue * toalLength;
                RenderUtil.drawGradientHorizontal(posX,posY,posX-6+(length),posY+getComponentHeight()-6,
                        Color.getHSBColor(0/360f,saturation,brightness).getRGB(), Color.getHSBColor(60/360f,saturation,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*1),posY,posX-6+(length*2),posY+getComponentHeight()-6,
                        Color.getHSBColor(60/360f,saturation,brightness).getRGB(), Color.getHSBColor(120/360f,saturation,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*2),posY,posX-6+(length*3),posY+getComponentHeight()-6,
                        Color.getHSBColor(120/360f,saturation,brightness).getRGB(), Color.getHSBColor(180/360f,saturation,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*3),posY,posX-6+(length*4),posY+getComponentHeight()-6,
                        Color.getHSBColor(180/360f,saturation,brightness).getRGB(), Color.getHSBColor(240/360f,saturation,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*4),posY,posX-6+(length*5),posY+getComponentHeight()-6,
                        Color.getHSBColor(240/360f,saturation,brightness).getRGB(), Color.getHSBColor(300/360f,saturation,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*5),posY,posX-6+(length*6),posY+getComponentHeight()-6,
                        Color.getHSBColor(300/360f,saturation,brightness).getRGB(), Color.getHSBColor(360/360f,saturation,brightness).getRGB());
                break;
            case "BRIGHTNESS":
                currentXPos = brightness * toalLength;
                RenderUtil.drawGradientHorizontal(posX,posY,posX-6+(length),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,saturation,0).getRGB(), Color.getHSBColor(hue,saturation,60/360f).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*1),posY,posX-6+(length*2),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,saturation,60/360f).getRGB(), Color.getHSBColor(hue,saturation,120/360f).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*2),posY,posX-6+(length*3),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,saturation,120/360f).getRGB(), Color.getHSBColor(hue,saturation,180/360f).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*3),posY,posX-6+(length*4),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,saturation,180/360f).getRGB(), Color.getHSBColor(hue,saturation,240/360f).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*4),posY,posX-6+(length*5),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,saturation,240/360f).getRGB(), Color.getHSBColor(hue,saturation,300/360f).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*5),posY,posX-6+(length*6),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,saturation,300/360f).getRGB(), Color.getHSBColor(hue,saturation,1).getRGB());
                break;
            case "SATURATION":
                currentXPos = saturation * toalLength;
                RenderUtil.drawGradientHorizontal(posX,posY,posX-6+(length),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,0,brightness).getRGB(), Color.getHSBColor(hue,60/360f,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*1),posY,posX-6+(length*2),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,60/360f,brightness).getRGB(), Color.getHSBColor(hue,120/360f,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*2),posY,posX-6+(length*3),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,120/360f,brightness).getRGB(), Color.getHSBColor(hue,180/360f,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*3),posY,posX-6+(length*4),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,180/360f,brightness).getRGB(), Color.getHSBColor(hue,240/360f,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*4),posY,posX-6+(length*5),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,240/360f,brightness).getRGB(), Color.getHSBColor(hue,300/360f,brightness).getRGB());
                RenderUtil.drawGradientHorizontal(posX-6+(length*5),posY,posX-6+(length*6),posY+getComponentHeight()-6,
                        Color.getHSBColor(hue,300/360f,brightness).getRGB(), Color.getHSBColor(hue,1,brightness).getRGB());
                break;
        }
        currentXPos += minXPos;
        RenderUtil.drawRectWidth(currentXPos-0.5f,posY+1,1,getComponentHeight()-8,
                0xAAAAAAAA);
        RenderUtil.drawRectOutlineWidth(currentXPos-0.5f,posY+1,1,getComponentHeight()-8,
                0xDD222222, 0.8f);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(hoveredColour(mouseX,mouseY)) {
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                if(mouseButton == 0) {
                    value.switchMode();

                }
            } else {
                if (mouseButton == 1) {
                    if (currentPickerMode == Modes.HUE) {
                        currentPickerMode = Modes.SATURATION;
                    } else if (currentPickerMode == Modes.SATURATION) {
                        currentPickerMode = Modes.BRIGHTNESS;
                    } else if (currentPickerMode == Modes.BRIGHTNESS) {
                        currentPickerMode = Modes.HUE;
                    }
                } else if (mouseButton == 0) {
                    setValue = true;
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        setValue = false;
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

    }
    public boolean hovered(int mouseX, int mouseY) {
        return mouseX > posX && mouseX < posX + getComponentWidth() &&
                mouseY > posY && mouseY < posY + getComponentHeight();
    }
    public boolean hoveredColour(int mouseX, int mouseY) {
        return mouseX >= posX+3 && mouseX <= posX+3 + getComponentWidth()-6 &&
                mouseY >= posY+3 && mouseY <= posY+3 + getComponentHeight()-6;
    }

    @Override
    public float getComponentHeight() {
        return 22;
    }

    enum Modes {
        HUE,
        SATURATION,
        BRIGHTNESS;
    }
}

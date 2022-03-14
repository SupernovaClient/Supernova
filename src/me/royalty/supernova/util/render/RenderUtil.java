package me.royalty.supernova.util.render;

import net.minecraft.client.gui.Gui;

import java.awt.*;

public class RenderUtil {
    public static void drawRectWidth(float x, float y, float width, float height, int colour) {
        Gui.drawRect(x,y,x+width,y+height, colour);
    }
    public static void drawRectWidth(float x, float y, float width, float height, Color colour) {
        Gui.drawRect(x,y,x+width,y+height, colour.getRGB());
    }
    public static void drawRectOutline(float x, float y, float x2, float y2, int colour, float thickness) {
        Gui.drawRect(x-thickness,y-thickness,x2+thickness,y,colour); //TOP
        Gui.drawRect(x-thickness,y2,x2+thickness,y2+thickness,colour); //BOTTOM
        Gui.drawRect(x-thickness,y,x,y2,colour); //LEFT
        Gui.drawRect(x2+thickness,y,x2,y2,colour); //RIGHT
    }
    public static void drawRectOutlineWidth(float x, float y, float width, float height, int colour, float thickness) {
        Gui.drawRect(x-thickness,y-thickness,x+width+thickness,y,colour); //TOP
        Gui.drawRect(x-thickness,y+height,x+width+thickness,y+height+thickness,colour); //BOTTOM
        Gui.drawRect(x-thickness,y,x,y+height,colour); //LEFT
        Gui.drawRect(x+width+thickness,y,x+width,y+height,colour); //RIGHT
    }
    public static Color astolfoColour(int yOffset, double speed) {
        float hue = (float) (((System.currentTimeMillis() + yOffset) % speed) / speed);
        if (hue > 0.5) hue = 0.5F - (hue - 0.5f);
        hue += 0.5F;
        return Color.getHSBColor(hue, 0.5f, 1F);
    }
    public static Color getRainbow(double speed, double offset, float saturation, float brightness) {
        long currentTime = System.currentTimeMillis()/10;
        currentTime *= speed;
        currentTime += offset;
        return Color.getHSBColor((currentTime%360)/360f,saturation,brightness);
    }
}

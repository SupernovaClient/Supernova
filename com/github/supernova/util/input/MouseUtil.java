package com.github.supernova.util.input;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;

public class MouseUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static Vector2f getMousePos() {
        ScaledResolution sr = new ScaledResolution(mc);
        float posX = Mouse.getX();
        float posY = mc.displayHeight - Mouse.getY();
        posX /= (float) sr.getScaleFactor();
        posY /= ((float) sr.getScaleFactor());
        return new Vector2f(posX,posY-1); // idk why y value is 1 less but eh
    }
}

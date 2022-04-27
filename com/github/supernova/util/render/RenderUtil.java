package com.github.supernova.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class RenderUtil {

    public static void drawCustomBox(float x, float y, float width, float height, int barColour, float barSize) {
        drawRectWidth(x,y,width,height,0xFF232323);
        drawGradientVertical(x,y,x+width,y+barSize,
                barColour, new Color(barColour).darker().darker().getRGB());
        drawGradientVertical(x,y+barSize,x+width,y+barSize+(height/10f),
                new Color(0xFF232323).brighter().getRGB(), 0xFF232323);
        drawGradientVertical(x,y+height-(height/10f), x+width, y+height,
                0xFF232323, new Color(0xFF232323).darker().getRGB());
        drawRectOutlineWidth(x,y,width,height,0xFF101010,1);
        drawRectOutlineWidth(x,y,width,barSize,0xFF101010, 0.5f);
    }

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
  
    public static void drawGradientVertical(float left, float top, float right, float bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawGradientHorizontal(float left, float top, float right, float bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, top, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static boolean isHovered(int mouseX, int mouseY, float x, float y, float x2, float y2) {
        if (mouseX >= x && mouseX <= x2) {
            return mouseY >= y && mouseY <= y2;
        }
        return false;
    }
    public static boolean isHoveredWidth(int mouseX, int mouseY, float x, float y, float width, float height) {
        if (mouseX >= x && mouseX <= x+width) {
            return mouseY >= y && mouseY <= y + height;
        }
        return false;
    }
    public static void color(int color) {
        color(color, 1);
    }
    public static void color(int color, float alpha) {
        Color colour = new Color(color);
        GlStateManager.color(colour.getRed()/255f, colour.getGreen()/255f, colour.getBlue()/255f, alpha);
    }


    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }
}

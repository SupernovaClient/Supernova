package io.github.nevalackin.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Render3DUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void glVertexBox(AxisAlignedBB aa) {
        GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);

        GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);

        GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);

        GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);

        GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);

        GL11.glVertex3d(aa.minX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.maxZ);
        GL11.glVertex3d(aa.minX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.minX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.minZ);
        GL11.glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
        GL11.glVertex3d(aa.maxX, aa.minY, aa.maxZ);
    }
    public static void drawWireAxisBoundingBox(AxisAlignedBB bb, Color color, int alpha) {
        mc.entityRenderer.disableLightmap();
        GL11.glPushMatrix();
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f,alpha/255f);
        glVertexBox(bb);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}

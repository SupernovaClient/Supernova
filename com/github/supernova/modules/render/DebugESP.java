package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.render.Render3DUtil;
import com.github.supernova.util.render.RenderUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleAnnotation(name = "Debug ESP", description = "Shows entity data", category = Category.RENDER, displayName = "Debug ESP")
public class DebugESP extends Module {

    @Override
    public void onEnable() {
        super.onEnable();
    }

    public final Listener<EventUpdate> eventUpdate = event -> {
    };

    @EventHandler
    public final Listener<EventRender3D> eventRender3D = event -> {
        for(Entity entity : mc.theWorld.getLoadedEntityList()) {
            if(entity == mc.thePlayer) continue;
            if(entity.getName().equals("Armor Stand")) continue;
            if(entity.getName().contains("Travel to:")) continue;
            if(entity instanceof EntityOtherPlayerMP) {
                renderData(entity, event.getPartialTicks(), "Other Player");
            }
            if(!(entity instanceof EntityLiving)) {
                continue;
            }
            EntityLiving ent = (EntityLiving) entity;
            String entityName = ent.getName();
            float entityHealth = ent.getHealth();

            Vec3 offset = Render3DUtil.getRenderOffset(event.getPartialTicks());
            Render3DUtil.drawWireAxisBoundingBox(entity.getEntityBoundingBox().offset(-offset.xCoord, -offset.yCoord, -offset.zCoord),new Color(0xFFFFFFFF), 255);

            renderData(entity, event.getPartialTicks(), entityName, String.valueOf(entityHealth), "");


        }
    };
    private void renderData(Entity entity, float partialTicks, String... data) {

        Vec3 playerPos = Render3DUtil.getRenderOffset(partialTicks);
        Vec3 entityPos = Render3DUtil.getEntityRenderOffset(entity, partialTicks);

        float distance = (0.016666668F * (mc.thePlayer.getDistanceToEntity(entity)) / 2);
        GL11.glPushMatrix();
        GL11.glTranslated(
                entityPos.xCoord - playerPos.xCoord
                ,entityPos.yCoord - playerPos.yCoord + entity.height + 0.2
                ,entityPos.zCoord - playerPos.zCoord);
        GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-distance, -distance, distance);
        GL11.glScaled(.4,.4,.4);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        float yPos = entity.height+0.5f;
        int index = 0;
        for(String line : data) {
            int xPos = mc.blockyFontObj.getStringWidth(line)/2;
            yPos -= 10;
            mc.blockyFontObj.drawStringWithShadow(line,-xPos,yPos, RenderUtil.astolfoColour(index*40, 10000).getRGB());
            index++;
        }


        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }
}

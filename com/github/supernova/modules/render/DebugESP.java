package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.network.EventReceivePacket;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.random.SkyblockUtil;
import com.github.supernova.util.render.Render3DUtil;
import com.github.supernova.util.render.RenderUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@ModuleAnnotation(name = "Debug ESP", description = "Shows entity data", category = Category.RENDER, displayName = "Debug ESP")
public class DebugESP extends Module {

    private final JsonParser parser = new JsonParser();

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdate = event -> {
        if(mc.thePlayer.getHeldItem() != null) {
            ItemStack item = mc.thePlayer.getHeldItem();
            String skyblockID = SkyblockUtil.getSkyblockID(item);
            // ExtraAttributes - ID
        }
    };
    @EventHandler
    public final Listener<EventReceivePacket> eventReceivePacketListener = event -> {
    };

    @EventHandler
    public final Listener<EventRender3D> eventRender3D = event -> {
        for(Entity entity : mc.theWorld.getLoadedEntityList()) {
            if(entity == mc.thePlayer) continue;
            if(entity.getClass().getName().equals(EntityArmorStand.class.getName())) continue;
            if(entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                boolean shouldAttack = !isActualPlayer(ent);
                renderData(entity, event.getPartialTicks(), String.valueOf(shouldAttack + "Balls"), ent.getCustomNameTag(), ent.getName());
                if(!shouldAttack) {
                Vec3 offset = Render3DUtil.getRenderOffset(event.getPartialTicks());
                Vec3 entOffset = Render3DUtil.getEntityRenderOffset(ent, event.getPartialTicks());
                Render3DUtil.drawWireAxisBoundingBox(ent.getEntityBoundingBox()
                                .offset(-entOffset.xCoord, -entOffset.yCoord, -entOffset.zCoord)
                                .offset(entOffset.xCoord-offset.xCoord,entOffset.yCoord-offset.yCoord,entOffset.zCoord-offset.zCoord)
                        , new Color(0xFFAAFFBB), 255);
                }
            }



        }
    };
    private boolean shouldAttack(EntityLivingBase entity) {
        if(entity instanceof EntityMob) {
            return true;
        }
        String entityName = entity.getName();

        return true;
    }
    private boolean isActualPlayer(EntityLivingBase entityPlayer) {
        boolean isPlayer = false;
        if(entityPlayer instanceof EntityPlayer) {
            for(Score score : mc.thePlayer.getWorldScoreboard().getScores()) {
                if (score.getObjective().getName().equals("health") && score.getPlayerName().contains(entityPlayer.getName())) {
                    isPlayer = true;
                }
            }
        }
        return isPlayer;
    }
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

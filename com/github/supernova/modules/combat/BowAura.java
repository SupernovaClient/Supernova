package com.github.supernova.modules.combat;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.render.Render3DUtil;
import com.github.supernova.value.impl.BooleanValue;
import com.github.supernova.value.impl.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;

@ModuleAnnotation(name = "Bow Aura", displayName = "BowAura", description = "Auto shoots entities with longbow", category = Category.COMBAT)
public class BowAura extends Module {

    public final NumberValue rangeValue = new NumberValue("Range", 15, 1, 30, 1);
    public final BooleanValue teamcheckValue = new BooleanValue("Team Check", true);
    public final BooleanValue dungeoncheckValue = new BooleanValue("Dungeon Check", true);

    private final ArrayList<EntityLivingBase> targets = new ArrayList<>();

    public BowAura() {
        setValues(teamcheckValue, dungeoncheckValue, rangeValue);
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = event -> {
        targets.clear();
        targets.addAll(getTargets());
        Scoreboard scoreboard = mc.thePlayer.getWorldScoreboard();
        for(Score score : scoreboard.getScores()) {
            ScoreObjective objective = score.getObjective();
            if(objective.getName().equals("SBScoreboard")) {
                //Supernova.INSTANCE.chat(score.getScorePoints()+"");
            }
        }
    };

    @EventHandler
    public final Listener<EventRender3D> eventRender3DListener = event -> {
        for(EntityLivingBase entity : targets) {
            Vec3 offset = Render3DUtil.getRenderOffset(event.getPartialTicks());
            Vec3 entOffset = Render3DUtil.getEntityRenderOffset(entity, event.getPartialTicks());
            Render3DUtil.drawWireAxisBoundingBox(entity.getEntityBoundingBox()
                            .offset(-entOffset.xCoord, -entOffset.yCoord, -entOffset.zCoord)
                            .offset(entOffset.xCoord-offset.xCoord,entOffset.yCoord-offset.yCoord,entOffset.zCoord-offset.zCoord)
                    , new Color(0xFFAAFFBB), 255);
        }
    };

    @EventHandler
    public final Listener<EventRender2D> eventRender2DListener = event -> {

    };

    private boolean isOnTeam(EntityPlayer player) {
        for(Score score : mc.thePlayer.getWorldScoreboard().getScores()) {
            if (score.getObjective().getName().equals("health") && score.getPlayerName().contains(player.getName())) {
                return true;
            }
        }
        return false;
    }
    private ArrayList<EntityLivingBase> getTargets() {
        ArrayList<EntityLivingBase> targets = new ArrayList<>();

        if(dungeoncheckValue.getCurrentValue()) {
            boolean isInDungeon = false;
            //TODO: Check if in dungeon
            if(!isInDungeon) return targets;
        }

        for(Entity entity : mc.theWorld.getLoadedEntityList()) {
            if(mc.thePlayer.getDistanceToEntity(entity) >= rangeValue.getDouble()) continue;
            if(!(entity instanceof EntityLivingBase)) continue;
            if(entity instanceof EntityWither) {
                if(entity.isInvisible()) continue;
            }
            if(entity instanceof EntityArmorStand) continue;
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if(entity.isInvisible() && !(entityLiving.getName().contains("Shadow") || entityLiving.getName().contains("Fel"))) continue;
            if(!(entity instanceof EntityPlayer) || !teamcheckValue.getCurrentValue()) {
                targets.add(entityLiving);
                continue;
            }
            EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
            if(isOnTeam(entityPlayer)) continue;
            targets.add(entityLiving);
        }
        return targets;
    }
}

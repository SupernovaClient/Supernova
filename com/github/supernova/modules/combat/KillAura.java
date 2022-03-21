package com.github.supernova.modules.combat;

import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.value.impl.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;

import java.util.ArrayList;

@ModuleAnnotation(name = "KillAura", displayName = "Kill Aura", category = Category.COMBAT)
public class KillAura extends Module {

	private final ArrayList<Entity> targets = new ArrayList<>();

	protected final NumberValue range = new NumberValue("Range", 4.2F, 3.0F, 6.0F, 0.5F);

	public KillAura() {
		setValues();
	}

	private boolean isOnTeam(EntityPlayer player) {
		for (Score score : mc.thePlayer.getWorldScoreboard().getScores()) {
			if (score.getObjective().getName().equals("health") && score.getPlayerName().contains(player.getName())) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<EntityLivingBase> getTargets() {
		ArrayList<EntityLivingBase> targets = new ArrayList<>();
		for (Entity entity : mc.theWorld.getLoadedEntityList()) {
			if (mc.thePlayer.getDistanceToEntity(entity) >= range.getFloat()) continue;
			if (!(entity instanceof EntityLivingBase)) continue;
			if (entity instanceof EntityWither) {
				if (entity.isInvisible()) continue;
			}
			if (entity instanceof EntityArmorStand) continue;
			EntityLivingBase entityLiving = (EntityLivingBase) entity;
			if (entity.isInvisible() && !(entityLiving.getName().contains("Shadow") || entityLiving.getName().contains("Fel")))
				continue;
			assert entityLiving instanceof EntityPlayer;
			EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			if (isOnTeam(entityPlayer)) continue;
			targets.add(entityLiving);
		}
		return targets;
	}

}

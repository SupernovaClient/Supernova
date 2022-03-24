package com.github.supernova.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class MovementUtil {

	public static final double SNEAK_MULTI = 0.3F;
	public static final double SOUL_MULTI = 0.5F;
	public static final double WALK_SPEED = 0.221;
	public static final double SWIM_MULTI = 0.3F;
	public static final double AIRBORNE_MULTI = 0.5F;

	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected final static EntityPlayerSP player = mc.thePlayer;

	public static boolean isMoving() {
		return player.moveForward != 0 || player.moveStrafing != 0;
	}

}

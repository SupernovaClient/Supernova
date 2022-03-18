package com.github.supernova.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class MovementUtil {

	protected static final Minecraft mc = Minecraft.getMinecraft();
	protected final static EntityPlayerSP player = mc.thePlayer;

	public static boolean isMoving() {
		return player.moveForward != 0 || player.moveStrafing != 0;
	}




}

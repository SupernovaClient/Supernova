package com.github.supernova.events.player;

import best.azura.eventbus.core.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventCollide implements Event {

	private final Block block;
	private final BlockPos position;
	private AxisAlignedBB AABB;

	public EventCollide(Block block, BlockPos pos, AxisAlignedBB axisalignedbb) {
		this.block = block;
		this.position = pos;
		this.AABB = axisalignedbb;
	}

	public AxisAlignedBB getAABB() {
		return AABB;
	}

	public void setAABB(AxisAlignedBB AABB) {
		this.AABB = AABB;
	}

	public BlockPos getPosition() {
		return position;
	}

	public Block getBlock() {
		return block;
	}
}

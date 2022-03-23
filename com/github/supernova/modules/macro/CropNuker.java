package com.github.supernova.modules.macro;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.player.EventMotion;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.ModeEnum;
import com.github.supernova.util.client.TimerUtil;
import com.github.supernova.util.game.SkyblockUtil;
import com.github.supernova.util.math.MathUtil;
import com.github.supernova.util.render.ColourUtil;
import com.github.supernova.util.render.Render3DUtil;
import com.github.supernova.value.impl.BooleanValue;
import com.github.supernova.value.impl.MultiEnumValue;
import com.github.supernova.value.impl.NumberValue;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@ModuleAnnotation(name = "Crop Nuker", displayName = "Crop Nuker", description = "Breaks Crops", category = Category.MACRO)
public class CropNuker extends Module {

	public BooleanValue autoMoveValue = new BooleanValue("Auto Move", true);
	public BooleanValue toolCheckValue = new BooleanValue("Tool Check", true);
	public BooleanValue rotationValue = new BooleanValue("Rotate", true);
	public MultiEnumValue<EnumCropTypes> nukerBlockTargetValue = new MultiEnumValue<>("Crops", EnumCropTypes.values(),
			EnumCropTypes.NETHERWART, EnumCropTypes.WHEAT, EnumCropTypes.POTATO, EnumCropTypes.COCOA,
			EnumCropTypes.CARROT, EnumCropTypes.MELON, EnumCropTypes.PUMPKIN);
	public NumberValue breakRangeValue = new NumberValue("Range", 4.5, 0.1, 6, 0.1);
	public NumberValue breakBPSValue = new NumberValue("BPS", 80, 1, 200, 1);

	private final TimerUtil breakTimer = new TimerUtil();
	private final TimerUtil lastClearTimer = new TimerUtil();

	public CropNuker() {
		super();
		setValues(breakBPSValue, breakRangeValue, nukerBlockTargetValue, autoMoveValue, toolCheckValue, rotationValue);
	}

	@Override
	public void onEnable() {
		breakTimer.reset();
		if(!runningThread) {
			runThread();
		}
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	private EnumMoveDirection currentMoveDirection = null;
	private CopyOnWriteArrayList<BlockPos> blocksToBreak = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<BlockPos> brokenBlocks = new CopyOnWriteArrayList<>();

	private int disconnectedTicks = 0;
	private boolean isDisconnected = false;

	@EventHandler
	public final Listener<EventUpdate> eventUpdate = event -> {
		SkyblockUtil.isOnIsland();
		if(isDisconnected) {
			disconnectedTicks++;
			rejoin(disconnectedTicks);
		} else {
			disconnectedTicks = 0;
		}
		blocksToBreak = getBlocksInRange()
				.stream()
				.sorted(Comparator.comparingDouble(mc.thePlayer::getDistanceSq))
				.collect(Collectors.toCollection(CopyOnWriteArrayList::new));
		if(rotationValue.getCurrentValue()) {
			float newYaw = getMovementYaw();
			float currentYaw = mc.thePlayer.rotationYaw % 360;
			float difference;
			if (newYaw > currentYaw) {
				difference = MathHelper.wrapAngleTo180_float(newYaw - currentYaw);
			} else {
				difference = MathHelper.wrapAngleTo180_float(currentYaw - newYaw);
			}
			if (newYaw < currentYaw) {
				mc.thePlayer.rotationYaw -= difference / 3.8;
			} else {
				mc.thePlayer.rotationYaw += difference / 3.8;
			}
		}
	};

	private void rejoin(int currentTicks) {

	}

	private boolean runningThread = false;
	private void runThread() {
		if(runningThread) return;
		runningThread = true;
		new Thread("Nuker Thread") {
			@Override
			public void run() {
				while (!this.isInterrupted() && isEnabled()) {
					if (blocksToBreak.size() > 0) {
						if (breakTimer.elapsed(MathUtil.bpsToMillis(breakBPSValue.getDouble()), true)) {
							BlockPos blockPos = getNextBlock();
							mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(
									C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
									blockPos,
									EnumFacing.DOWN));
							brokenBlocks.add(blockPos);
							blocksToBreak.remove(blockPos);
							lastClearTimer.reset();
						}
					}
					if (lastClearTimer.elapsed(150, true)) {
						brokenBlocks.clear();
					}
				}
				runningThread = false;
			}
		}.start();
	}

	@EventHandler
	public final Listener<EventRender2D> eventRender2D = event -> {
	};

	@EventHandler
	public final Listener<EventRender3D> eventRender3D = event -> {
		if (blocksToBreak.size() > 0) {
			BlockPos nextBlock = getNextBlock();
			if (nextBlock == null) return;
			AxisAlignedBB bbox = mc.theWorld.getBlockState(nextBlock).getBlock().getSelectedBoundingBox(mc.theWorld, nextBlock);
			float partialTicks = event.getPartialTicks();
			Vec3 offset = Render3DUtil.getRenderOffset(partialTicks);
			bbox = bbox.offset(-offset.xCoord, -offset.yCoord, -offset.zCoord);
			Render3DUtil.drawWireAxisBoundingBox(bbox, ColourUtil.astolfoColour(0, 10000), 255);
		}
	};

	@EventHandler
	public final Listener<EventMotion> eventMotion = event -> {
		if(!event.pre()) return;
		if (!autoMoveValue.getCurrentValue()) return;
		if(currentMoveDirection == null || mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.setPosition(
					Math.floor(mc.thePlayer.posX)+0.5,
					mc.thePlayer.posY,
					Math.floor(mc.thePlayer.posZ)+0.5);
			updateDirection();
		} else {
			double movementSpeed = getMovementSpeed();
			event.setX(currentMoveDirection.x * movementSpeed);
			event.setZ(currentMoveDirection.z * movementSpeed);
		}
	};

	private double getMovementSpeed() {
		double baseSpeed = 0.22f;
		return baseSpeed * (SkyblockUtil.getSpeedPercentage()/100f);
	}

	private BlockPos getNextBlock() {
		if (blocksToBreak.size() < 1) return null;
		blocksToBreak.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceSq));
		return blocksToBreak.get(0);
	}

	private void updateDirection() {
		ArrayList<EnumMoveDirection> openDirections = getOpenDirections();
		if(openDirections.size() == 0) return;
		currentMoveDirection = openDirections.get(0);
	}

	private ArrayList<EnumMoveDirection> getOpenDirections() {
		ArrayList<EnumMoveDirection> openDir = new ArrayList<>();
		for(EnumMoveDirection dir : EnumMoveDirection.values()) {
			if(currentMoveDirection != null) {
				if(dir == currentMoveDirection) continue;
			}
			if(isOpenDir(dir)) {
				openDir.add(dir);
			}
		}
		openDir.remove(getOpposite(currentMoveDirection));
		if(openDir.size() == 0) {
			openDir.add(getOpposite(currentMoveDirection));
		}
		return openDir;
	}

	private boolean isOpenDir(EnumMoveDirection direction) {
		BlockPos feetBlock = getPlayerPos().add(direction.x,direction.y,direction.z);
		BlockPos headBlock = getPlayerPos().add(direction.x,direction.y+1,direction.z);
		IBlockState feetBlockState = mc.theWorld.getBlockState(feetBlock);
		IBlockState headBlockState = mc.theWorld.getBlockState(headBlock);
		Block feet = feetBlockState.getBlock();
		Block head = headBlockState.getBlock();
			return (head instanceof BlockBush || head instanceof BlockAir) &&
					(feet instanceof BlockBush || feet instanceof BlockAir);
	}

	private float getMovementYaw() {
		if (currentMoveDirection == null) return mc.thePlayer.rotationYaw;
		float a = 0, b = 0;
		if (currentMoveDirection.z == -1) {
			b = 180;
		}
		if (currentMoveDirection.x == 1) {
			a = 270;
		}
		if (currentMoveDirection.x == -1) {
			a = 90;
		}
		return a + b;
	}

	private EnumMoveDirection getOpposite(EnumMoveDirection direction) {
		EnumMoveDirection returnDirection = null;
		if (direction == EnumMoveDirection.NORTH) {
			returnDirection = EnumMoveDirection.SOUTH;
		} else if (direction == EnumMoveDirection.WEST) {
			returnDirection = EnumMoveDirection.EAST;
		} else if (direction == EnumMoveDirection.SOUTH) {
			returnDirection = EnumMoveDirection.NORTH;
		} else if (direction == EnumMoveDirection.EAST) {
			returnDirection = EnumMoveDirection.WEST;
		}
		return returnDirection;
	}

	private CopyOnWriteArrayList<BlockPos> getBlocksInRange() {
		BlockPos playerPos = getPlayerPos();
		BlockPos pos1 = playerPos.add(-6, -6, -6);
		BlockPos pos2 = playerPos.add(6, 6, 6);
		CopyOnWriteArrayList<BlockPos> targetBlocks = new CopyOnWriteArrayList<>();
		for (BlockPos pos : BlockPos.getAllInBox(pos1, pos2)) {
			if(brokenBlocks.contains(pos)) continue;
			IBlockState blockState = mc.theWorld.getBlockState(pos);
			Block block = blockState.getBlock();
			for (EnumCropTypes type : nukerBlockTargetValue.getEnabledValues()) {
				if (isType(block, type)) {
					if(toolCheckValue.getCurrentValue()) {
						if(!isTool(mc.thePlayer.getHeldItem(),type)) {
							continue;
						}
					}
					if(type == EnumCropTypes.MELON || type == EnumCropTypes.PUMPKIN) {
						targetBlocks.add(pos);
					} else if (getMaxBlockAge(blockState) == getBlockAge(blockState)) {
						targetBlocks.add(pos);
					}
					break;
				}
			}
		}
		return targetBlocks.stream().filter((block) -> mc.thePlayer.getDistance(block.getX(),block.getY(),block.getZ()) <= breakRangeValue.getCurrentValue())
				.collect(Collectors.toCollection(CopyOnWriteArrayList::new));
	}

	private BlockPos getPlayerPos() {
		return new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	}

	private boolean isType(Block block, EnumCropTypes type) {
		return block.getClass().getName().equals(type.blockType.getName());
	}
	private boolean isTool(ItemStack item, EnumCropTypes type) {
		if(item != null) {
			if(item.getItem() != null) {
				return item.getItem().getClass().equals(type.tool);
			}
		}
		return false;
	}

	private int getBlockAge(IBlockState blockState) {
		for (Map.Entry<IProperty, Comparable> entry : blockState.getProperties().entrySet()) {
			if (entry.getKey().getName().equals("age")) {
				return (int) entry.getValue();
			}
		}
		return -1;
	}

	private int getMaxBlockAge(IBlockState blockState) {

		for (Map.Entry<IProperty, Comparable> entry : blockState.getProperties().entrySet()) {
			if (entry.getKey().getName().equals("age")) {
				ArrayList<Integer> values = new ArrayList<>();
				entry.getKey().getAllowedValues().forEach(value -> {
					values.add(Integer.parseInt(value.toString()));
				});
				return values.get(values.size() - 1);
			}
		}
		return Integer.MAX_VALUE;
	}

	enum EnumCropTypes implements ModeEnum {
		NETHERWART(BlockNetherWart.class, "Nether Wart", ItemHoe.class),
		COCOA(BlockCocoa.class, "Cocoa Bean", ItemAxe.class),
		PUMPKIN(BlockPumpkin.class, "Pumpkin", ItemAxe.class),
		CARROT(BlockCarrot.class, "Carrot", ItemHoe.class),
		POTATO(BlockPotato.class, "Potato", ItemHoe.class),
		WHEAT(BlockCrops.class, "Wheat", ItemHoe.class),
		MELON(BlockMelon.class, "Melon", ItemAxe.class);

		Class<? extends Block> blockType = null;
		private String name;
		private Class<? extends Item> tool;
		EnumCropTypes(Class<? extends Block> blockType, String name, Class<? extends Item> breakItem) {
			this.name = name;
			this.blockType = blockType;
			this.tool = breakItem;
		}

		@Override
		public String getName() {
			return name;
		}
		public Class<? extends Item> getTool() {
			return this.tool;
		}
	}

	enum EnumMoveDirection {
		NORTH(0, 0, -1),
		EAST(1, 0, 0),
		SOUTH(0, 0, 1),
		WEST(-1, 0, 0);

		public final double x, y, z;

		EnumMoveDirection(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}

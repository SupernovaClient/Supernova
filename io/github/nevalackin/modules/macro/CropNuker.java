package io.github.nevalackin.modules.macro;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import io.github.nevalackin.events.player.EventMotion;
import io.github.nevalackin.events.player.EventUpdate;
import io.github.nevalackin.events.render.EventRender2D;
import io.github.nevalackin.events.render.EventRender3D;
import io.github.nevalackin.modules.Category;
import io.github.nevalackin.modules.Module;
import io.github.nevalackin.modules.ModuleAnnotation;
import io.github.nevalackin.util.MathUtil;
import io.github.nevalackin.util.client.TimerUtil;
import io.github.nevalackin.util.render.Render3DUtil;
import io.github.nevalackin.util.render.RenderUtil;
import io.github.nevalackin.value.impl.BooleanValue;
import io.github.nevalackin.value.impl.MultiEnumValue;
import io.github.nevalackin.value.impl.NumberValue;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

@ModuleAnnotation(name = "Crop Nuker", displayName = "Crop Nuker", description = "Breaks Crops", category = Category.MACRO)
public class CropNuker extends Module {

	public BooleanValue autoMoveValue = new BooleanValue("Auto Move", false);
	public MultiEnumValue<EnumCropTypes> nukerBlockTargetValue = new MultiEnumValue<>("Crops", EnumCropTypes.values(), EnumCropTypes.NETHERWART);
	public NumberValue breakRangeValue = new NumberValue("Range", 5.5, 0.1, 6, 0.1);
	public NumberValue breakBPSValue = new NumberValue("BPS", 40, 1, 80, 1);

	private final TimerUtil breakTimer = new TimerUtil();

	public CropNuker() {
		super();
		setValues(breakRangeValue, nukerBlockTargetValue, autoMoveValue);
	}

	@Override
	public void onEnable() {
		breakTimer.reset();
		super.onEnable();
	}

	private final EnumMoveDirection currentMoveDirection = null;
	private ArrayList<BlockPos> blocksToBreak = new ArrayList<>();

	@EventHandler
	public final Listener<EventUpdate> eventUpdate = event -> {
		blocksToBreak = getBlocksInRange(breakRangeValue.getDouble());
	};

	@EventHandler
	public final Listener<EventRender2D> eventRender2D = event -> {
		mc.blockyFontObj.drawStringWithShadow(blocksToBreak.size() + "", 5, 20, 0xFFDADADA);
	};

	@EventHandler
	public final Listener<EventRender3D> eventRender3D = event -> {
		if (blocksToBreak.size() > 0) {
			BlockPos nextBlock = getNextBlock();
			if (nextBlock == null) return;
			AxisAlignedBB bbox = mc.theWorld.getBlockState(nextBlock).getBlock().getSelectedBoundingBox(mc.theWorld, nextBlock);
			Vec3 camera = ActiveRenderInfo.getPosition();
			EntityPlayerSP player = mc.thePlayer;
			float partialTicks = event.getPartialTicks();
			double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
			double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
			double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
			bbox = bbox.offset(-d0, -d1, -d2);
			Render3DUtil.drawWireAxisBoundingBox(bbox, RenderUtil.astolfoColour(0, 10000), 255);
		}
	};

	@EventHandler
	public final Listener<EventMotion> eventMotion = event -> {
		if (blocksToBreak.size() > 0) {
			if (breakTimer.elapsed(MathUtil.bpsToMillis(breakBPSValue.getDouble()), true)) {
				BlockPos blockPos = getNextBlock();
				mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
						blockPos,
						EnumFacing.DOWN));
				blocksToBreak.remove(blockPos);
			}
		}

		if (!autoMoveValue.getCurrentValue()) return;
		updateDirection();
	};

	private BlockPos getNextBlock() {
		if (blocksToBreak.size() < 1) return null;
		blocksToBreak.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceSq));
		return blocksToBreak.get(0);
	}

	private void updateDirection() {

	}

	private ArrayList<BlockPos> getBlocksInRange(double range) {
		range /= 2;
		BlockPos playerPos = getPlayerPos();
		BlockPos pos1 = playerPos.add(-range, -range, -range);
		BlockPos pos2 = playerPos.add(range, range, range);
		ArrayList<BlockPos> targetBlocks = new ArrayList<>();
		for (BlockPos pos : BlockPos.getAllInBox(pos1, pos2)) {
			IBlockState blockState = mc.theWorld.getBlockState(pos);
			Block block = blockState.getBlock();
			for (EnumCropTypes type : nukerBlockTargetValue.getEnabledValues()) {
				if (isType(block, type)) {
					if (getMaxBlockAge(blockState) == getBlockAge(blockState)) {
						targetBlocks.add(pos);
					}
					break;
				}
			}
		}
		return targetBlocks;
	}

	private BlockPos getPlayerPos() {
		return new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	}

	private boolean isType(Block block, EnumCropTypes type) {
		return block.getClass().getName().equals(type.blockType.getName());
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
				int maxValue = Integer.MAX_VALUE;
				ArrayList<Integer> values = new ArrayList<>();
				entry.getKey().getAllowedValues().forEach(value -> {
					values.add(Integer.parseInt(value.toString()));
				});
				return values.get(values.size() - 1);
			}
		}
		return Integer.MAX_VALUE;
	}

	enum EnumCropTypes {
		NETHERWART(BlockNetherWart.class),
		WHEAT(BlockCrops.class),
		CARROT(BlockCarrot.class),
		POTATO(BlockPotato.class);

		Class<? extends Block> blockType = null;

		EnumCropTypes(Class<? extends Block> blockType) {
			this.blockType = blockType;
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

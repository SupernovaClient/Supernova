package com.github.supernova.modules.macro;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.client.TimerUtil;
import com.github.supernova.value.impl.NumberValue;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@ModuleAnnotation(name = "Test Nuker", displayName = "Test Nuker", category = Category.MACRO)
public class TestNuker extends Module {

    public final NumberValue delayValue = new NumberValue("Delay (s)", 1, 0, 5, 0.1);
    private final TimerUtil delayTimer = new TimerUtil();

    public TestNuker() {
        setValues(delayValue);
    }

    @Override
    public void onEnable() {
        delayTimer.reset();
        super.onEnable();
    }

    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = event -> {
        if(delayTimer.elapsed((long) (delayValue.getDouble()*1000),true)) {
            breakAll(getBlocksInRange());
        }
        ArrayList<Entity> entList = mc.theWorld.getLoadedEntityList().stream().filter(ent -> ent instanceof EntityCreeper).collect(Collectors.toCollection(ArrayList::new));
        entList.forEach(entity -> {
            EntityCreeper ent = (EntityCreeper) entity;
            C02PacketUseEntity packetUseEntity = new C02PacketUseEntity(ent, C02PacketUseEntity.Action.INTERACT);
            mc.thePlayer.sendQueue.addToSendQueueSilent(packetUseEntity);
        });
    };

    private void breakAll(ArrayList<BlockPos> blocks) {
        for(BlockPos pos : blocks) {
            mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                    pos,
                    EnumFacing.DOWN));
        }
    }

    private ArrayList<BlockPos> getBlocksInRange() {
        ArrayList<BlockPos> blocks = new ArrayList<>();
        for(BlockPos pos : BlockPos.getAllInBox(playerPos().add(-4,-4,-4), playerPos().add(4,4,4))) {
            IBlockState state = mc.theWorld.getBlockState(pos);
            if(state.getBlock() instanceof BlockNetherWart || state.getBlock() instanceof BlockCrops) {
                if(getMaxBlockAge(state) == getBlockAge(state)) blocks.add(pos);
            } else if(state.getBlock() instanceof BlockSkull) {
                blocks.add(pos);
            }
        }
        return blocks;
    }
    private BlockPos playerPos() {
        return new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
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

}

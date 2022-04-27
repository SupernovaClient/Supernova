package com.github.supernova.modules.render;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import com.github.supernova.Supernova;
import com.github.supernova.events.network.EventSendPacket;
import com.github.supernova.events.player.EventUpdate;
import com.github.supernova.events.render.EventRender2D;
import com.github.supernova.events.render.EventRender3D;
import com.github.supernova.modules.Category;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleAnnotation;
import com.github.supernova.util.render.Render3DUtil;
import com.github.supernova.value.impl.ColourValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.Sys;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleAnnotation(name = "SkullFinder", description = "Finds skulls and outlines them", category = Category.RENDER, displayName = "SkullFinder")
public class SkullFinder extends Module {

    CopyOnWriteArrayList<BlockPos> skullBlocks = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<BlockPos> foundSkulls = new CopyOnWriteArrayList<>();
    public ColourValue notfoundColour = new ColourValue("Not Found Colour", new Color(0xFFFF6060));
    public ColourValue foundColour = new ColourValue("Found Colour", new Color(0xFF60FF60));

    @Override
    public void onEnable() {
        skullBlocks.clear();
        new Thread(() -> {
            BlockPos playerPos = getPlayerPos();
            BlockPos pos2 = playerPos.add(250,100,250);
            BlockPos pos1 = playerPos.add(-250,-100,-250);
            Iterable<BlockPos> iter = BlockPos.getAllInBox(pos1,pos2);
            iter.forEach(pos -> {
                if(!enabled) return;
                IBlockState state = mc.theWorld.getBlockState(pos);
                Block blockType = state.getBlock();
                if(blockType instanceof BlockSkull) {
                    skullBlocks.add(pos);
                }
            });
        }).start();
        super.onEnable();
    }
    @EventHandler
    public final Listener<EventUpdate> eventUpdateListener = event -> {
        for(BlockPos pos : skullBlocks) {
            if(!foundSkulls.contains(pos)) {
                C08PacketPlayerBlockPlacement packet = new C08PacketPlayerBlockPlacement();
            }
        }

    };
    @EventHandler
    public final Listener<EventSendPacket> eventSendPacketListener = event -> {

        if(event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage packetChat = (C01PacketChatMessage) event.getPacket();
            if(packetChat.getMessage().contains("SendSkullLocations")) {
                Supernova.INSTANCE.chat("Starting Messages");
                new Thread(() -> {
                    for(BlockPos pos : skullBlocks) {
                        try {
                            Thread.sleep(3500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(!isEnabled()) return;
                        mc.thePlayer.sendChatMessage("Supernova | Skull Location #"+ (skullBlocks.indexOf(pos)+1) +": X:"+pos.getX()+" Y:"+pos.getY()+" Z:"+pos.getZ());
                    }
                    Supernova.INSTANCE.chat("Finished Messages");
                }).start();
            }
        }

        if(event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement placementPacket = (C08PacketPlayerBlockPlacement) event.getPacket();
            if(skullBlocks.contains(placementPacket.getPosition())) {
                System.out.println(placementPacket.getPlacedBlockDirection());
                System.out.println(placementPacket.getPosition());
                System.out.println(placementPacket.getPlacedBlockOffsetX());
                System.out.println(placementPacket.getPlacedBlockOffsetY());
                System.out.println(placementPacket.getPlacedBlockOffsetZ());
                foundSkulls.add(placementPacket.getPosition());
            }
        }
    };
    @EventHandler
    public final Listener<EventRender3D> eventRender3DListener = event -> {
        for(BlockPos pos : skullBlocks) {
            AxisAlignedBB bbox = mc.theWorld.getBlockState(pos).getBlock().getSelectedBoundingBox(mc.theWorld, pos);
            float partialTicks = event.getPartialTicks();
            Vec3 offset = Render3DUtil.getRenderOffset(partialTicks);
            bbox = bbox.offset(-offset.xCoord, -offset.yCoord, -offset.zCoord);
            Render3DUtil.drawWireAxisBoundingBox(bbox, new Color(foundSkulls.contains(pos) ?  foundColour.getInt() : notfoundColour.getInt()), 255);
        }
    };
    public BlockPos getPlayerPos() {
        return new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
    }
}

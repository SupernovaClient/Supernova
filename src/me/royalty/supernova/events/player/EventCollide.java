package me.royalty.supernova.events.player;

import com.darkmagician6.eventapi.events.callables.Event;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventCollide extends Event {

    @Getter
    private final Block block;
    @Getter
    private final BlockPos position;
    @Getter @Setter
    private AxisAlignedBB AABB;

    public EventCollide(Block block, BlockPos pos, AxisAlignedBB axisalignedbb) {
        this.block = block;
        this.position = pos;
        this.AABB = axisalignedbb;
    }

}

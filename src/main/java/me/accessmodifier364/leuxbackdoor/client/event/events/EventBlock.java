package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventBlock extends EventCancellable {

    public BlockPos pos;
    public EnumFacing facing;

    private int stage;

    public EventBlock(final int stage, final BlockPos pos, final EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
        this.stage = stage;
    }

    public void set_stage(int stage) {
        this.stage = stage;
    }

    public int get_stage() {
        return this.stage;
    }

}

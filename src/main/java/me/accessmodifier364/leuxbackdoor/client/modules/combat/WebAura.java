package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BlockUtil;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WebAura extends Module {
    public WebAura() {
        super(Category.combat);
        this.name        = "WebAura";
        this.description = "webs ppl coz its crazy annoying";
    }

    Setting range = create("Range", "Range", 5.0, 1.0, 8.0);
    Setting rotate = create("Rotate","Rotate", false);
    Setting delayTick = create("Delay", "Delay", 3, 0, 10);
    Setting packet = create("Packet","Packet", true);
    Setting lowFeet = create("Low Feet","Low Feet", false);
    Setting legs = create("Legs","Legs", true);
    Setting chest = create("Chest","Chest", true);
    Setting head = create("Head","Head", false);

    EntityPlayer player;
    boolean r = false;
    int delay = 0;

    @Override
    public void enable(){
        if(mc.player == null || mc.world == null) return;
        r = false;
        delay = 0;
    }

    @Override
    public void update(){
            r = rotate.get_value(true);
            trap();
    }


    private void trap() {
        if(delay < this.delayTick.get_value(1)){
            delay++;
            return;
        }
        else {
            delay = 0;
        }
        this.player = getTarget(this.range.get_value(1f), false);
        List<Vec3d> placeTargets = this.getPos();
        if(placeTargets == null)return;
        this.placeList(placeTargets);
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || trapped && player.isInWeb) continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (!(mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }

    private List<Vec3d> getPos() {
        ArrayList<Vec3d> list = new ArrayList<>();
        if(player == null)return null;
        Vec3d baseVec = this.player.getPositionVector();
        if (this.lowFeet.get_value(true)) {
            list.add(baseVec.add(0.0, -1.0, 0.0));
        }
        if (this.legs.get_value(true)) {
            list.add(baseVec);
        }
        if (this.chest.get_value(true)) {
            list.add(baseVec.add(0.0, 1.0, 0.0));
        }
        if(this.head.get_value(true)){
            list.add(baseVec.add(0.0, 2.0, 0.0));
        }
        return list;
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, false);
            if (placeability != 3 && placeability != 1) continue;
            this.placeBlock(position);
        }
    }

    private void placeBlock(BlockPos pos) {
        int oldSlot = mc.player.inventory.currentItem;
        if(InventoryUtil.findHotbarBlockz(BlockWeb.class) == -1)return;
        mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.findHotbarBlockz(BlockWeb.class)));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.r, this.packet.get_value(true), true);
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        mc.playerController.updateController();
        mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
    }

}

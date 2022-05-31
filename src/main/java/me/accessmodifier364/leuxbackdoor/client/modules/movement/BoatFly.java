package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPlayerTravel;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.PlayerUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;

import java.util.Objects;

public class BoatFly extends Module {
    public BoatFly() {
        super(Category.movement);
        this.name = "BoatFly";
        this.description = "fly with entities";
    }

    private final PlayerUtil util = new PlayerUtil();
    private int ticks;
    Setting hSpeed = create("HSpeed", "HSpeed", 1.0, 0.1f, 10.0);
    Setting vSpeed = create("VSpeed", "VSpeed", 1.0, 0.1f, 10.0);
    Setting gSpeed = create("GSpeed", "GSpeed", 0.1f, 0.0, 1.0);
    Setting tickDelay = create("TickDelay", "TickDelay", 1, 0, 20);
    Setting fixYaw = create("FixYaw", "FixYaw", true);
    Setting gravity = create("Gravity", "Gravity", true);
    Setting bypass = create("Bypass", "Bypass", false);

    @EventHandler
    private final Listener<EventPacket.SendPacket> onSendPacket = new Listener<>(event -> {
        if (mc.player == null || !mc.player.isRiding() || !bypass.get_value(true)) {
            return;
        }
        if (event.get_packet() instanceof CPacketVehicleMove && ticks++ >= tickDelay.get_value(1) + 1) {
            mc.player.connection.sendPacket(new CPacketUseEntity(Objects.requireNonNull(mc.player.getRidingEntity()), EnumHand.MAIN_HAND));
            ticks = 0;
            return;
        }
        if (event.get_packet() instanceof CPacketPlayer.Rotation || event.get_packet() instanceof CPacketInput) {
            event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> onReceivePacket = new Listener<>(event -> {
        if (mc.player == null || !mc.player.isRiding() || !bypass.get_value(true)) {
            return;
        }
        if (event.get_packet() instanceof SPacketMoveVehicle || event.get_packet() instanceof SPacketPlayerPosLook) {
            event.cancel();
        }
    });

    @EventHandler
    private final Listener<EventPlayerTravel> onTravel = new Listener<>(event -> {
        if (mc.player == null || !mc.player.isRiding()) {
            return;
        }
        Entity e = mc.player.getRidingEntity();
        if (fixYaw.get_value(true)) {
            assert e != null;
            e.rotationYaw = mc.player.rotationYaw;
        }
        assert e != null;
        e.setNoGravity(!gravity.get_value(true));
        if (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f) {
            util.setBoatSpeed(hSpeed.get_value(1.0), e);
        }
        e.motionY = mc.player.movementInput.sneak ? -vSpeed.get_value(1.0) : (mc.player.ticksExisted % 2 != 0 ? -gSpeed.get_value(1.0) / 10.0 : (mc.player.movementInput.jump ? vSpeed.get_value(1.0) : gSpeed.get_value(1.0) / 10.0));
        event.cancel();
    });
}
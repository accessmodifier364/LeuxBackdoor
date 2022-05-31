package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import io.netty.util.internal.ConcurrentSet;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventMotionUpdate;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventMove;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.Timer;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketFly extends Module {
    public PacketFly() {
        super(Category.movement);
        this.name = "PacketFly";
        this.description = "Uses packets to fly";
    }

    Setting flight = create("Flight", "Flight", true);
    Setting flightMode = create("FMode", "FMode", 0,0,1);
    Setting doAntiFactor = create("Factorize", "Factorize", true);
    Setting antiFactor = create("AntiFactor", "AntiFactor", 0.7,0.1,3.0);
    Setting extraFactor = create("ExtraFactor", "ExtraFactor", 0.7, 0.1, 3.0);
    Setting strafeFactor = create("StrafeFactor", "StrafeFactor", false);
    Setting loops = create("Loops", "Loops", 1,1,10);
    Setting clearTeleMap = create("ClearMap", "ClearMap", false);
    Setting mapTime = create("ClearTime", "ClearTime", 30, 1, 500);
    Setting clearIDs = create("ClearIDs", "ClearIDs", false);
    Setting setYaw = create("SetYaw", "SetYaw", false);
    Setting setID = create("SetID", "SetID", false);
    Setting setMove = create("SetMove", "SetMove", false);
    Setting nocliperino = create("NoClip", "NoClip", false);
    Setting sendTeleport = create("Teleport", "Teleport", false);
    Setting resetID = create("ResetID", "ResetID", false);
    Setting setPos = create("SetPos", "SetPos", true);
    Setting invalidPacket = create("InvalidPacket", "InvalidPacket", false);

    private final Set<CPacketPlayer> packets = new ConcurrentSet();
    private final Map<Integer, IDtime> teleportmap = new ConcurrentHashMap<>();
    private int flightCounter = 0;
    private int teleportID = 0;

    @Override
    public void onTick() {
        teleportmap.entrySet().removeIf(idTime -> clearTeleMap.get_value(true) && idTime.getValue().getTimer().passedS(mapTime.get_value(1)));
    }

    @EventHandler
    private final Listener<EventMotionUpdate> player_move2 = new Listener<>(event -> {
        mc.player.setVelocity(0.0, 0.0, 0.0);
        double speed;
        boolean checkCollisionBoxes = checkHitBoxes();
        speed = mc.player.movementInput.jump && (checkCollisionBoxes || !EntityUtil.isMoving()) ? (flight.get_value(true) && !checkCollisionBoxes ? (flightMode.get_value(1) == 0 ? (resetCounter(10) ? -0.032 : 0.062) : (resetCounter(20) ? -0.032 : 0.062)) : 0.062) : (mc.player.movementInput.sneak ? -0.062 : (!checkCollisionBoxes ? (resetCounter(4) ? (flight.get_value(true) ? -0.04 : 0.0) : 0.0) : 0.0));
        if (doAntiFactor.get_value(true) && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0) {
            speed /= antiFactor.get_value(1.0);
        }
        double[] strafing = getMotion(strafeFactor.get_value(true) && checkCollisionBoxes ? 0.031 : 0.26);
        for (int i = 1; i < loops.get_value(1) + 1; ++i) {
            mc.player.motionX = strafing[0] * (double)i * extraFactor.get_value(1.0);
            mc.player.motionY = speed * (double)i;
            mc.player.motionZ = strafing[1] * (double)i * extraFactor.get_value(1.0);
            sendPackets(mc.player.motionX, mc.player.motionY, mc.player.motionZ, sendTeleport.get_value(true));
        }
    });

    @EventHandler
    private final Listener<EventMove> player_move = new Listener<>(event -> {
        if (setMove.get_value(true) && flightCounter != 0) {
            event.set_x(mc.player.motionX);
            event.set_y(mc.player.motionY);
            event.set_z(mc.player.motionZ);
            if (nocliperino.get_value(true) && checkHitBoxes()) {
                mc.player.noClip = true;
            }
        }
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> receive_event_packet = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer) {
            if (!packets.remove(event.get_packet())) {
                event.cancel();
            }
        }
    });

    @EventHandler
    private final Listener<PlayerSPPushOutOfBlocksEvent> listener_push = new Listener<>(event -> {
        event.setCanceled(true);
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> sendPacket = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketPlayerPosLook && mc.player != null && mc.world != null) {
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.get_packet();
            if (mc.player.isEntityAlive() && mc.world.isBlockLoaded(pos, false) && !(mc.currentScreen instanceof GuiDownloadTerrain) && clearIDs.get_value(true)) {
                teleportmap.remove(packet.getTeleportId());
            }
            if (setYaw.get_value(true)) {
                packet.yaw = mc.player.rotationYaw;
                packet.pitch = mc.player.rotationPitch;
            }
            if (setID.get_value(true)) {
                teleportID = packet.getTeleportId();
            }
        }
    });

    private boolean checkHitBoxes() {
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    private boolean resetCounter(int counter) {
        if (++flightCounter >= counter) {
            flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private void sendPackets(double x, double y, double z, boolean teleport) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = mc.player.getPositionVector().add(vec);
        Vec3d outOfBoundsVec = outOfBoundsVec(vec, position);
        packetSender(new CPacketPlayer.Position(position.x, position.y, position.z, mc.player.onGround));
        if (invalidPacket.get_value(true)) {
            packetSender(new CPacketPlayer.Position(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, mc.player.onGround));
        }
        if (setPos.get_value(true)) {
            mc.player.setPosition(position.x, position.y, position.z);
        }
        teleportPacket(position, teleport);
    }

    private void teleportPacket(Vec3d pos, boolean shouldTeleport) {
        if (shouldTeleport) {
            mc.player.connection.sendPacket(new CPacketConfirmTeleport(++teleportID));
            teleportmap.put(teleportID, new IDtime(pos, new Timer()));
        }
    }

    private Vec3d outOfBoundsVec(Vec3d offset, Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        packets.add(packet);
        mc.player.connection.sendPacket(packet);
    }

    private void clean() {
        teleportmap.clear();
        flightCounter = 0;
        if (resetID.get_value(true)) {
            teleportID = 0;
        }
        packets.clear();
    }

    public static class IDtime {
        private final Vec3d pos;
        private final Timer timer;

        public IDtime(Vec3d pos, Timer timer) {
            this.pos = pos;
            this.timer = timer;
            this.timer.reset();
        }

        public Vec3d getPos() {
            return this.pos;
        }

        public Timer getTimer() {
            return this.timer;
        }
    }
}
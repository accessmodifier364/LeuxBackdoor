package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventMove;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketEntityAction;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class Speed extends Module {
	public Speed() {
		super(Category.movement);
		this.name        = "Speed";
		this.description = "its like running, but faster";
	}
    
    Setting multiplier = create("Multiplier", "Multiplier", 3, 0, 10);
    Setting extraYBoost = create("Extra Y Boost", "ExtraYBoost", -1, -5, 30);
    Setting accelerationTimer = create("Acceleration Timer", "AccelerationTimer", false);
    Setting timerSpeed = create("Timer Speed", "TimerSpeed", 1.0, 0.0, 2.0);
    Setting speedDetect = create("Speed Detect", "SpeedDetect", true);
    Setting jumpDetect = create("Leaping Detect", "LeapingDetect", true);

    private double prevDist;
    private static int[] a;
    private double motionSpeed;
    private int currentState;

    @EventHandler
    private final Listener<EventMove> player_move = new Listener<>(event -> {
        if (event.get_era() != EventCancellable.Era.EVENT_PRE) {
            return;
        }
        if (!isNull(mc.player)) {
            switch (currentState) {
                case 0: {
                    currentState += a[1];
                    prevDist = 0.0;
                    break;
                }
                case 2: {
                    double b = 0.40123128 + (extraYBoost.get_value(1) / (double)1000);
                    if ((!c(d(mc.player.moveForward)) || e(d(mc.player.moveStrafing))) && e(mc.player.onGround ? 1 : 0)) {
                        if (e(mc.player.isPotionActive(MobEffects.JUMP_BOOST) ? 1 : 0) && e(jumpDetect.get_value(true) ? 1 : 0)) {
                            b += (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + a[1]) * 0.1f;
                        }
                        event.set_y(mc.player.motionY = b);
                        motionSpeed *= 2.149;
                        break;
                    }
                    break;
                }
                case 3: {
                    motionSpeed = prevDist - 0.76 * (prevDist - getBaseMotionSpeed());
                    break;
                }
                default: {
                    if ((!f(mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size()) || e(mc.player.collidedVertically ? 1 : 0)) && ohsex(currentState)) {
                        if (c(d(mc.player.moveForward)) && c(d(mc.player.moveStrafing))) {
                            currentState = a[0];
                        }
                        else {
                            currentState = a[1];
                        }
                    }
                    motionSpeed = prevDist - prevDist / 159.0;
                    break;
                }
            }
            motionSpeed = Math.max(motionSpeed, getBaseMotionSpeed());
            double g = mc.player.movementInput.moveForward;
            double h = mc.player.movementInput.moveStrafe;
            final double i = mc.player.rotationYaw;
            if (c(j(g)) && c(j(h))) {
                event.set_x(0.0);
                event.set_z(0.0);
            }
            if (e(j(g)) && e(j(h))) {
                g *= Math.sin(0.7853981633974483);
                h *= Math.cos(0.7853981633974483);
            }
            event.set_x((g * motionSpeed * -Math.sin(Math.toRadians(i)) + h * motionSpeed * Math.cos(Math.toRadians(i))) * (0.99 + multiplier.get_value(1) / (double)600));
            event.set_z((g * motionSpeed * Math.cos(Math.toRadians(i)) - h * motionSpeed * -Math.sin(Math.toRadians(i))) * (0.99 + multiplier.get_value(1) / (double)600));
            currentState += a[1];
        }
        event.cancel();
    });

    @Override
    public void update() {
        if (isNull(mc.player)) {
            return;
        }
        prevDist = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
        if (e(accelerationTimer.get_value(true) ? 1 : 0)) {
            mc.timer.tickLength = (float) (50.0f / timerSpeed.get_value(1.0));
        }
        else if (e(k(mc.timer.tickLength))) {
            mc.timer.tickLength = 50.0f;
        }
        if (c(mc.player.isSprinting() ? 1 : 0) && e(1)) {
            mc.player.setSprinting(a[1] != 0);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
        }
    }

    public void disable() {
        mc.timer.tickLength = 50.0f;
    }

    private static void l() {
        String[] m;
        (m = new String[a[8]])[a[0]] = n("n9pHF6SFvkOs6iUr+fnXgA==", "GmCTC");
        m[a[1]] = n("4noHmwJ5F40+cu8qBPcyzA==", "CVFaT");
        m[a[2]] = n("R+hGwU+dCgQQcUdIkD9ZYaUO+QBhMxiN", "RjGgZ");
        m[a[3]] = n("Dk9SQuIPQSn5I8lWMj8Z+w==", "dWNML");
        m[a[5]] = n("rPWGh7vSeiSJWWJOJQfq5wdZ8fI6Y9G+", "QkkkG");
        m[a[6]] = n("6BSD78RsHX6yVgm/4JINjBgTGCxZfgXF", "rXpxu");
        m[a[7]] = o();
    }

    private double getBaseMotionSpeed() {
        double p = 0.272;
        if (e(mc.player.isPotionActive(MobEffects.SPEED) ? 1 : 0) && e(speedDetect.get_value(true) ? 1 : 0)) {
            final int q = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            p *= 1.0 + 0.2 * q;
        }
        return p;
    }

    private static boolean c(final int r) {
        return r == 0;
    }

    private static String n(final String s, final String t) {
        try {
            final SecretKeySpec u = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(t.getBytes(StandardCharsets.UTF_8)), "Blowfish");
            final Cipher v = Cipher.getInstance("Blowfish");
            v.init(a[2], u);
            return new String(v.doFinal(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
        catch (Exception w) {
            w.printStackTrace();
            return null;
        }
    }

    private static String o() {
        try {
            final SecretKeySpec x = new SecretKeySpec(Arrays.copyOf(MessageDigest.getInstance("MD5").digest("RTxXY".getBytes(StandardCharsets.UTF_8)), a[9]), "DES");
            final Cipher y = Cipher.getInstance("DES");
            y.init(a[2], x);
            return new String(y.doFinal(Base64.getDecoder().decode("ENR8rJxJYtA86kRMf8iVlQ==".getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        }
        catch (Exception z) {
            z.printStackTrace();
            return null;
        }
    }

    static {
        lIIlIIIII();
        l();
    }

    private static int j(final double var0) {
        final double var3;
        return ((var3 = var0 - 0.0) == 0.0) ? 0 : ((var3 < 0.0) ? -1 : 1);
    }

    private static boolean isNull(final Object cope) {
        return cope == null;
    }

    private static boolean e(final int cum) {
        return cum != 0;
    }


    private static void lIIlIIIII() {
        (a = new int[10])[0] = 0;
        a[1] = " ".length();
        a[2] = "  ".length();
        a[3] = "   ".length();
        a[4] = 10;
        a[5] = 4;
        a[6] = 5;
        a[7] = 6;
        a[8] = 7;
        a[9] = 8;
    }

    private static int k(final float var0) {
        final float var2;
        return ((var2 = var0 - (float) 50.0) == 0.0f) ? 0 : ((var2 < 0.0f) ? -1 : 1);
    }

    private static boolean f(final int sex) {
        return sex <= 0;
    }

    private static int d(final float var0) {
        final float var2;
        return ((var2 = var0 - (float) 0.0) == 0.0f) ? 0 : ((var2 < 0.0f) ? -1 : 1);
    }

    private static boolean ohsex(final int ihatepedos) {
        return ihatepedos > 0;
    }
}
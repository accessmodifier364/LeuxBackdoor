package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventMotionUpdate;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.PosManager;
import me.accessmodifier364.leuxbackdoor.client.util.RotationUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class BowAim extends Module { //This module is completely made by me cuz enelojooo
    public BowAim() {
		super(Category.combat);
		this.name        = "BowAim";
		this.description = "Predicts enemies movement and aims at them when using a Bow";
    }

    Setting lockView = create("Lock View", "LockView", false);
	public static ArrayList<Entity> attackList = new ArrayList<>();
	public static ArrayList<Entity> targets = new ArrayList<>();
	public static int currentTarget;

	public boolean isValidTarget(Entity entity) {
		boolean valid = false;
		if (mc.player.isRiding()) {
			return false;
		}
		if (entity.isInvisible()) {
			valid = true;
		}
		if (FriendUtil.isFriend(entity.getName()) && entity instanceof EntityPlayer || !mc.player.canEntityBeSeen(entity)) {
			return false;
		}
		if (entity instanceof EntityPlayer) {
			valid = mc.player.getDistance(entity) <= 50.0f && entity != mc.player && entity.isEntityAlive() && !FriendUtil.isFriend(entity.getName());
		}
		return valid;
	}

	@EventHandler
	private final Listener<EventMotionUpdate> on_movement = new Listener<>(event -> {
		if (!lockView.get_value(true)) {
			PosManager.updatePosition();
			RotationUtil.updateRotations();
		}
	});

	@Override
		public void update() {
		Entity e;
		for (Object o : mc.world.loadedEntityList) {
			e = (Entity)o;
			if (e instanceof EntityPlayer && !targets.contains(e)) {
				targets.add(e);
			}
			if (!targets.contains(e) || !(e instanceof EntityPlayer)) continue;
			targets.remove(e);
		}
		if (currentTarget >= attackList.size()) {
			currentTarget = 0;
		}
		for (Object o : mc.world.loadedEntityList) {
			e = (Entity)o;
			if (isValidTarget(e) && !attackList.contains(e)) {
				attackList.add(e);
			}
			if (isValidTarget(e) || !attackList.contains(e)) continue;
			attackList.remove(e);
		}
		sortTargets();
		if (mc.player != null && attackList.size() != 0 && attackList.get(currentTarget) != null && isValidTarget(attackList.get(currentTarget)) && mc.player.isHandActive() && mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
			int bowCurrentCharge = mc.player.getItemInUseMaxCount();
			float bowVelocity = (float)bowCurrentCharge / 20.0f;
			bowVelocity = (bowVelocity * bowVelocity + bowVelocity * 2.0f) / 3.0f;
			bowVelocity = MathHelper.clamp(bowVelocity, 0.0f, 1.0f);
			double v = bowVelocity * 3.0f;
			double g = 0.05000000074505806;
			if ((double)bowVelocity < 0.1) {
				return;
			}
			if (bowVelocity > 1.0f) {
				bowVelocity = 1.0f;
			}
			double xDistance = attackList.get(currentTarget).posX - mc.player.posX + (attackList.get(currentTarget).posX - attackList.get(currentTarget).lastTickPosX) * (double)(bowVelocity * 10.0f);
			double zDistance = attackList.get(currentTarget).posZ - mc.player.posZ + (attackList.get(currentTarget).posZ - attackList.get(currentTarget).lastTickPosZ) * (double)(bowVelocity * 10.0f);
			float trajectoryTheta90 = (float)(Math.atan2(zDistance, xDistance) * 180.0 / 3.141592653589793) - 90.0f;
			float bowTrajectory = (float)((double)((float)(- Math.toDegrees(getLaunchAngle((EntityLivingBase)attackList.get(currentTarget), v, g)))) - 3.8);
			if (trajectoryTheta90 <= 360.0f && bowTrajectory <= 360.0f) {
				if (lockView.get_value(true)) {
					mc.player.rotationYaw = trajectoryTheta90;
					mc.player.rotationPitch = bowTrajectory;
				} else {
					RotationUtil.setPlayerRotations(trajectoryTheta90, bowTrajectory);
				}
			}
		}
	}

	public void sortTargets() {
		attackList.sort((ent1, ent2) -> {
			double d2;
			double d1 = mc.player.getDistance(ent1);
			return d1 < (d2 = mc.player.getDistance(ent2)) ? -1 : (d1 == d2 ? 0 : 1);
		});
	}

	@Override
	public void disable() {
		targets.clear();
		attackList.clear();
		currentTarget = 0;
	}

	private float getLaunchAngle(EntityLivingBase targetEntity, double v, double g) {
		double yDif = targetEntity.posY + (double)(targetEntity.getEyeHeight() / 2.0f) - (mc.player.posY + (double)mc.player.getEyeHeight());
		double xDif = targetEntity.posX - mc.player.posX;
		double zDif = targetEntity.posZ - mc.player.posZ;
		double xCoord = Math.sqrt(xDif * xDif + zDif * zDif);
		return theta(v + 2.0, g, xCoord, yDif);
	}

	private float theta(double v, double g, double x, double y) {
		double yv = 2.0 * y * (v * v);
		double gx = g * (x * x);
		double g2 = g * (gx + yv);
		double insqrt = v * v * v * v - g2;
		double sqrt = Math.sqrt(insqrt);
		double numerator = v * v + sqrt;
		double numerator2 = v * v - sqrt;
		double atan1 = Math.atan2(numerator, g * x);
		double atan2 = Math.atan2(numerator2, g * x);
		return (float)Math.min(atan1, atan2);
	}

}
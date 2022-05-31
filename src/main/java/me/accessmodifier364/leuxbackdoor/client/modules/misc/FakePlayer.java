package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import com.mojang.authlib.GameProfile;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.MoverType;
import net.minecraft.world.GameType;

import java.util.Random;
import java.util.UUID;

public class FakePlayer extends Module {
    public FakePlayer() {
        super(Category.misc);
		this.name        = "FakePlayer";
		this.description = "hahahaaha what a noob its in beta ahahahahaha";
    }

    Setting travel = create("Travel", "Travel", true);
    Setting receiveDamage = create("ReceiveDamage", "ReceiveDamage", true);

    private EntityOtherPlayerMP otherPlayer;

    @Override
    public void enable() {
        if (mc.world == null || mc.player == null) {
            toggle();
            return;
        }
        if (otherPlayer == null) {
            otherPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.randomUUID(), "Gray"));
            otherPlayer.copyLocationAndAnglesFrom(mc.player);
            otherPlayer.inventory.copyInventory(mc.player.inventory);
            if (receiveDamage.get_value(true)) {
                otherPlayer.setGameType(GameType.SURVIVAL);
                otherPlayer.setHealth(20);
            }
        }
        mc.world.spawnEntity(otherPlayer);
    }

    @Override
    public void update() {
        if (otherPlayer != null) {
            Random random = new Random();
            otherPlayer.moveForward = mc.player.moveForward + (random.nextInt(5) / 10F);
            otherPlayer.moveStrafing = mc.player.moveStrafing + (random.nextInt(5) / 10F);
            if (travel.get_value(true)) travel(otherPlayer.moveStrafing, otherPlayer.moveVertical, otherPlayer.moveForward);
        }
    }

    @Override
    public void disable() {
        if (otherPlayer != null) {
            mc.world.removeEntity(otherPlayer);
            otherPlayer = null;
        }
    }

    public void travel(float strafe, float vertical, float forward) {
        double d0 = otherPlayer.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = (float) EnchantmentHelper.getDepthStriderModifier(otherPlayer);

        if (f3 > 3.0F) {
            f3 = 3.0F;
        }

        if (!otherPlayer.onGround) {
            f3 *= 0.5F;
        }

        if (f3 > 0.0F) {
            f1 += (0.54600006F - f1) * f3 / 3.0F;
            f2 += (otherPlayer.getAIMoveSpeed() - f2) * f3 / 4.0F;
        }

        otherPlayer.moveRelative(strafe, vertical, forward, f2);
        otherPlayer.move(MoverType.SELF, otherPlayer.motionX, otherPlayer.motionY, otherPlayer.motionZ);
        otherPlayer.motionX *= f1;
        otherPlayer.motionY *= 0.800000011920929D;
        otherPlayer.motionZ *= f1;

        if (!otherPlayer.hasNoGravity()) {
            otherPlayer.motionY -= 0.02D;
        }

        if (otherPlayer.collidedHorizontally && otherPlayer.isOffsetPositionInLiquid(otherPlayer.motionX, otherPlayer.motionY + 0.6000000238418579D - otherPlayer.posY + d0, otherPlayer.motionZ)) {
            otherPlayer.motionY = 0.30000001192092896D;
        }
    }

}
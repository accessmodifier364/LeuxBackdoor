package me.accessmodifier364.leuxbackdoor.client.modules.movement;


import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventEntity;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;


public class Velocity extends Module {
	public Velocity() {
		super(Category.movement);
		this.name        = "Velocity";
		this.description = "No kockback";
	}

	Setting nopush = create("NoPush", "NoPush", true);

	@EventHandler
	private final Listener<PlayerSPPushOutOfBlocksEvent> listener_push = new Listener<>(event -> {
		if (nopush.get_value(true)) {
			event.setCanceled(true);
		}
	});

	@EventHandler
	private final Listener<EventPacket.ReceivePacket> damage = new Listener<>(event -> {
		if (event.get_era() == EventCancellable.Era.EVENT_PRE) {
			if (event.get_packet() instanceof SPacketEntityVelocity) {
				SPacketEntityVelocity knockback = (SPacketEntityVelocity) event.get_packet();
				if (knockback.getEntityID() == mc.player.getEntityId()) {
					event.cancel();
					knockback.motionX *= 0.0f;
					knockback.motionY *= 0.0f;
					knockback.motionZ *= 0.0f;
				}
			} else if (event.get_packet() instanceof SPacketExplosion) {
				event.cancel();

				SPacketExplosion knockback = (SPacketExplosion) event.get_packet();

				knockback.motionX *= 0.0f;
				knockback.motionY *= 0.0f;
				knockback.motionZ *= 0.0f;
			}
		}
	});

	@EventHandler
	private final Listener<EventEntity.EventColision> explosion = new Listener<>(event -> {
		if (event.get_entity() == mc.player) {
			event.cancel();
		}
	});
}
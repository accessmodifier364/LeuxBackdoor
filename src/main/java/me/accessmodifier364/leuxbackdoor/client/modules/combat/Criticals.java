package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {

	public Criticals() {
		super(Category.combat);
		this.name        = "Criticals";
		this.description = "You can hit with criticals when attack.";
	}
	
	Setting mode = create("Mode", "Mode", "Packet", combobox("Packet", "Jump"));

	@EventHandler
	private final Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
		if (event.get_packet() instanceof CPacketUseEntity) {
				if ((mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && (mc.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold) || ModLoader.get_module_manager().get_module_with_tag("AutoCrystal").is_disabled() && ModLoader.get_module_manager().get_module_with_tag("AutoFrameDupe").is_disabled()) {
					if (mc.player.getHeldItemMainhand().isEmpty()) {
						return;
					}
					CPacketUseEntity event_entity = ((CPacketUseEntity) event.get_packet());
					if (event_entity.getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
						if (mode.in("Packet")) {
							mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
							mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
						} else if (mode.in("Jump")) {
							mc.player.jump();
						}
					}
				}
		}
	});

	@Override
	public String array_detail() {
		return mode.get_current_value();
	}
}
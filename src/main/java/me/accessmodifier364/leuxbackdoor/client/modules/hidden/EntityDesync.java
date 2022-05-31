package me.accessmodifier364.leuxbackdoor.client.modules.hidden;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventNetworkPacketEvent;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EntityDesync extends Module {
    public EntityDesync() {
        super(Category.hidden);
        this.name        = "EntityDesync";
        this.description = "for WWE dupe";
    }

    private Entity Riding = null;

    @Override
    public void enable() {
        if (mc.player == null) {
            Riding = null;
            toggle();
            return;
        }

        if (!mc.player.isRiding()) {
            MessageUtil.send_client_message(ChatFormatting.RED + "[Entity Desync] You are not riding an entity.");
            Riding = null;
            toggle();
            return;
        }

        Riding = mc.player.getRidingEntity();

        mc.player.dismountRidingEntity();
        mc.world.removeEntity(Riding);
    }

    @Override
    public void disable() {
        if (Riding != null) {
            Riding.isDead = false;
            if (!mc.player.isRiding()) {
                mc.world.spawnEntity(Riding);
                mc.player.startRiding(Riding, true);
            }
            Riding = null;
            MessageUtil.send_client_message(ChatFormatting.RED + "[Entity Desync] Forced a remount.");
        }
    }

    @Override
    public void update () {
        /// We must be riding to send these packets.
        if (Riding == null)
            return;

        if (mc.player.isRiding())
            return;

        mc.player.onGround = true;

        Riding.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);

        mc.player.connection.sendPacket(new CPacketVehicleMove(Riding));
    }

    @EventHandler
    private final Listener<EventNetworkPacketEvent> PacketEvent = new Listener<>(p_Event -> {
        if (p_Event.getPacket() instanceof SPacketSetPassengers) {
            if (Riding == null)
                return;
            SPacketSetPassengers l_Packet = (SPacketSetPassengers) p_Event.getPacket();
            Entity en = mc.world.getEntityByID(l_Packet.getEntityId());
            if (en == Riding) {
                for (int i : l_Packet.getPassengerIds()) {
                    Entity ent = mc.world.getEntityByID(i);
                    if (ent == mc.player)
                        return;
                }
                MessageUtil.send_client_message("ChatFormatting.RED + [Entity Desync] You dismounted. RIP");
                toggle();
            }
        }
        else if (p_Event.getPacket() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities l_Packet = (SPacketDestroyEntities) p_Event.getPacket();
            for (int l_EntityId : l_Packet.getEntityIDs()) {
                if (l_EntityId == Riding.getEntityId()) {
                    MessageUtil.send_client_message("ChatFormatting.RED + [Entity Desync] Entity is now null SPacketDestroyEntities");
                    //  toggle();
                    return;
                }
            }
        }
    });

    @EventHandler
    private final Listener<EntityJoinWorldEvent> OnWorldEvent = new Listener<>(p_Event -> {
        if (p_Event.getEntity() == mc.player) {
            MessageUtil.send_client_message("[Entity Desync] Joined world event!");
        }
    });
}

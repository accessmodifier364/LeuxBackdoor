package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.EzMessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AutoGG extends Module {
    public AutoGG() { //Writted by ObsidianBreaker :3, april 04, 2021
        super(Category.misc);
        this.name = "AutoGG";
        this.description = "xd";
    }

    Setting ez = create("Ez", "Ez", true);
    Setting pop = create("Pop", "Pop", false);
    Setting excuse = create("Excuse", "Excuse", false);

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap();
    private static final ConcurrentHashMap targeted_players = new ConcurrentHashMap();
    int diedTime = 0;
    int delay_count = 0;

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {
        if (pop.get_value(true)) {
        SPacketEntityStatus packet;
        if (event.get_packet() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus) event.get_packet()).getOpCode() == 35) {
            Entity entity = packet.getEntity(mc.world);
            int count = 1;
            if (totem_pop_counter.containsKey(entity.getName())) {
                count = totem_pop_counter.get(entity.getName());
                totem_pop_counter.put(entity.getName(), ++count);
            } else {
                totem_pop_counter.put(entity.getName(), count);
            }
            if (entity == mc.player) {
                return;
            }
            mc.player.sendChatMessage( "ez pop " + entity.getName() + " " + count + " totems");
        }
        }
    });

    @EventHandler
    private Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        if (ez.get_value(true)) {
            if (mc.player == null) return;
            if (event.get_packet() instanceof CPacketUseEntity) {
                CPacketUseEntity cPacketUseEntity = (CPacketUseEntity) event.get_packet();
                if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                    Entity target_entity = cPacketUseEntity.getEntityFromWorld(mc.world);
                    if (target_entity instanceof EntityPlayer) {
                        add_target(target_entity.getName());
                    }
                }
            }
        }});

    @EventHandler
    private Listener<LivingDeathEvent> living_death_listener = new Listener<>(event -> {
        if (ez.get_value(true)) {
        if (mc.player == null) return;
        EntityLivingBase e = event.getEntityLiving();
        if (e == null) return;
        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;
            if (player.getHealth() <= 0) {
                if (targeted_players.containsKey(player.getName())) {
                    announce(player.getName());
                }
            }
        }
    }});

    @Override
    public void update() {
        if (pop.get_value(true)) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (!totem_pop_counter.containsKey(player.getName()) || !player.isDead && !(player.getHealth() <= 0.0f)) continue;
            int count = totem_pop_counter.get(player.getName());
            totem_pop_counter.remove(player.getName());
            if (player == mc.player) continue;
            mc.player.sendChatMessage(player.getName() + " died after popping " + count + " totems");
        }}

        if (ez.get_value(true)) {
            for (Entity entity : mc.world.getLoadedEntityList()) {
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    if (player.getHealth() <= 0) {
                        if (targeted_players.containsKey(player.getName())) {
                            announce(player.getName());
                        }
                    }
                }
            }

            targeted_players.forEach((name, timeout) -> {
                if ((int)timeout <= 0) {
                    targeted_players.remove(name);
                } else {
                    targeted_players.put(name, (int)timeout - 1);
                }

            });

            delay_count++;
        }

        if (excuse.get_value(true)) {
            if (this.diedTime > 0) {
                --this.diedTime;
            }
            if (mc.player.isDead) {
                this.diedTime = 500;
            }
            if (!mc.player.isDead && this.diedTime > 0) {
                final Random rand = new Random();
                final int randomNum = rand.nextInt(10) + 1;
                if (randomNum == 1) {
                    mc.player.sendChatMessage("U win bc u r a fucking ping player");
                }
                if (randomNum == 2) {
                    mc.player.sendChatMessage("LMAO luckiest player ever");
                }
                if (randomNum == 3) {
                    mc.player.sendChatMessage("Bruh, i was testing settings");
                }
                if (randomNum == 5) {
                    mc.player.sendChatMessage("Fucking pingspikes");
                }
                if (randomNum == 6) {
                    mc.player.sendChatMessage("Imagine saying ez");
                }
                if (randomNum == 7) {
                    mc.player.sendChatMessage("I have highping, stop saying ez");
                }
                if (randomNum == 8) {
                    mc.player.sendChatMessage("I was afk bruhh");
                }
                if (randomNum == 9) {
                    mc.player.sendChatMessage("Dont say ez, its cringe");
                }
                if (randomNum == 10) {
                    mc.player.sendChatMessage("Im so lagged :(");
                }
                this.diedTime = 0;
            }
        }
    }

    public void announce(String name) {
        if (delay_count < 150) {
            return;
        }
        delay_count = 0;
        targeted_players.remove(name);
        String message = EzMessageUtil.get_message().replace("[", "").replace("]", "");
        mc.player.connection.sendPacket(new CPacketChatMessage(message));
    }

    public static void add_target(String name) {
        if (!Objects.equals(name, mc.player.getName())) {
            targeted_players.put(name, 20);
        }
    }
}

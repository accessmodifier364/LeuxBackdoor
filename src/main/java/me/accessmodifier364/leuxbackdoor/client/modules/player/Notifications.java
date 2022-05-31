package me.accessmodifier364.leuxbackdoor.client.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Notifications extends Module { //All this module writted by ObsidianBreaker, February 5, 2021
    public Notifications() {
        super(Category.player);
        this.str = Collections.newSetFromMap(new WeakHashMap<>());
        this.name = "Notifications";
        this.description = "got yo ass drinking sum";
    }

    Setting coordexploit = create("CoordExploit", "CoordExploit", false);
    Setting visualrange = create("VisualRange", "VisualRange", false);
    Setting strength = create("Strength", "Strength", false);
    Setting breakwarner =  create("BreakWarner", "BreakWarner", true);
    Setting distanceToDetect = create("Max Break Distance", "WarnerMaxDistance", 2, 1, 5);
    Setting notificationTicks = create("Chat Delay", "WarnerChatDelay", 18, 14, 25);


    private final Set<EntityPlayer> str;
    private List<String> people;
    public static final Minecraft mc = Minecraft.getMinecraft();
    private final HashMap<Entity, Vec3d> knownPlayers = new HashMap<>();
    private final HashMap<String, Vec3d> tpdPlayers = new HashMap<>();

    private int numTicks = 0;
    private int numForgetTicks = 0;
    private int ticks;

    @EventHandler
    public Listener<EventPacket.ReceivePacket> packetReceiveListener = new Listener<>(event -> {
        EntityPlayerSP player = mc.player;
        WorldClient world = mc.world;
        if (Objects.isNull(player) || Objects.isNull(world)) {
            return;
        }
        if (event.get_packet() instanceof SPacketBlockBreakAnim && this.pastDistance(player, ((SPacketBlockBreakAnim) event.get_packet()).getPosition(), this.distanceToDetect.get_value(1))) {
            if (ticks >= notificationTicks.get_value(1)) {
                this.sendChat();
                ticks = 0;
            }
            ++ticks;
        }
    });

    @Override
    public void enable() {
        people = new ArrayList<>();
    }

    @Override
    public void update() {
        if (strength.get_value(true)){
            for (EntityPlayer player : mc.world.playerEntities) {
                if (player.equals(mc.player))
                    continue;
                if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
                    MessageUtil.send_client_message(ChatFormatting.RESET + player.getDisplayNameString() + ChatFormatting.RED + " Has Strength");
                    this.str.add(player);
                }
                if (!this.str.contains(player) || player.isPotionActive(MobEffects.STRENGTH))
                    continue;
                MessageUtil.send_client_message(ChatFormatting.RESET + player.getDisplayNameString() + ChatFormatting.GREEN + " Has Ran Out Of Strength");
                this.str.remove(player);
            }}

        if (coordexploit.get_value(true)) {
            if (this.numTicks >= 50) {
                this.numTicks = 0;
                for (final Entity entity : mc.world.loadedEntityList) {
                    if (entity instanceof EntityPlayer && !entity.getName().equals(mc.player.getName())) {
                        final Vec3d playerPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
                        if (this.knownPlayers.containsKey(entity)) {
                            if (Math.abs(this.knownPlayers.get(entity).distanceTo(playerPos)) > 50.0
                                    && Math.abs(mc.player.getPositionVector().distanceTo(playerPos)) > 100.0
                                    && (!this.tpdPlayers.containsKey(entity.getName())
                                    || this.tpdPlayers.get(entity.getName()) != playerPos)) {

                                MessageUtil.send_client_message("Player " + entity.getName() + " has tp'd to " + vectorToString(playerPos, false));
                                saveFile(vectorToString(playerPos, false), entity.getName());
                                this.knownPlayers.remove(entity);
                                this.tpdPlayers.put(entity.getName(), playerPos);
                            }
                        }
                        this.knownPlayers.put(entity, playerPos);
                    }
                }
            }
            if (this.numForgetTicks >= 9000000) {
                this.tpdPlayers.clear();
            }
            ++this.numTicks;
            ++this.numForgetTicks;
        }

        if (visualrange.get_value(true)){
            if (mc.world == null | mc.player == null) return;

            List<String> peoplenew = new ArrayList<>();
            List<EntityPlayer> playerEntities = mc.world.playerEntities;

            for (Entity e : playerEntities) {
                if (e.getName().equals(mc.player.getName())) continue;
                peoplenew.add(e.getName());
            }

            if (peoplenew.size() > 0) {
                for (String name : peoplenew) {
                    if (!people.contains(name)) {
                        if (FriendUtil.isFriend(name)) {
                            MessageUtil.send_client_message("I see an epic dude called " + ChatFormatting.RESET + ChatFormatting.GREEN + name + ChatFormatting.RESET + " :D");
                        } else {
                            MessageUtil.send_client_message("I see a dude called " + ChatFormatting.RESET + ChatFormatting.RED + name);
                        }
                        people.add(name);
                    }
                }
            }
        }
    }

    public static String vectorToString(final Vec3d vector, final boolean includeY) {
        final StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append((int)Math.floor(vector.x));
        builder.append(", ");
        if (includeY) {
            builder.append((int)Math.floor(vector.y));
            builder.append(", ");
        }
        builder.append((int)Math.floor(vector.z));
        builder.append(")");
        return builder.toString();
    }

    public void saveFile(String pos, String name) {
        try {
            File file = new File("./LeuxBackdoor/coordexploit.json");
            file.getParentFile().mkdirs();
            PrintWriter writer = new PrintWriter(new FileWriter(file, true));
            writer.println("name: " + name + " coords: " + pos);
            writer.close();
        } catch (Exception ignored) {}
    }

    private boolean pastDistance(EntityPlayer player, BlockPos pos, double dist) {
        return player.getDistanceSqToCenter(pos) <= Math.pow(dist, 2.0);
    }

    public void sendChat() {
        if (breakwarner.get_value(true)) {
            MessageUtil.send_client_message("" + ChatFormatting.RED + "BREAK WARNING!!!");
        }
    }

    public String getPlayer() {
        List entities = mc.world.playerEntities.stream().filter(entityPlayer -> !FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList());
        EntityPlayer e;
        Iterator var2 = entities.iterator();
        do {
            if (!var2.hasNext()) {
                return "";
            }
            e = (EntityPlayer)var2.next();
        } while(e.isDead || e.getHealth() <= 0.0F || e.getName().equals(mc.player.getName()) || !(e.getHeldItemMainhand().getItem() instanceof ItemTool));
        return e.getName();
    }
}

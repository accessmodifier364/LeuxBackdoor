package me.accessmodifier364.leuxbackdoor.client.guiscreen.hud;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.pinnables.Pinnable;
import me.accessmodifier364.leuxbackdoor.client.modules.combat.AutoCrystal;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listenable;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Objects;

public class EnemyInfo extends Pinnable implements Listenable {
    public EnemyInfo() {
        super("Enemy Info", "EnemyInfo", 1, 0, 0);

        this.set_height(80);
        this.set_width(150);

        EventClientBus.EVENT_BUS.subscribe(this);
    }

    @Override
    public void render() {
        update_pops();

        if (mc.world == null || mc.player == null) {
            return;
        }

        EntityPlayer target = mc.player;

        float lowest_distance = 999F;

        for (EntityPlayer e : mc.world.playerEntities) {
            if (e.getDistance(mc.player) < lowest_distance && !e.getName().equals(mc.player.getName()) && e.getDistance(mc.player) != 0) {
                target = e;
                lowest_distance = e.getDistance(mc.player);
            }
        }

        if (AutoCrystal.get_target() != null && !AutoCrystal.get_target().isDead) {
            target = AutoCrystal.get_target();
        }

        create_rect(0, 0, this.get_width(), this.get_height(), 0, 0, 0, 69);

        float target_hp = target.getHealth() + target.getAbsorptionAmount();
        String ping_str = "Ping: ";
        try {
            final int response_time = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getResponseTime();
            ping_str += response_time + "ms";
        } catch (Exception ignored) {}
        float distance_to_target = target.getDistance(mc.player);

        int hp_r;
        int hp_g;
        int hp_b;

        if (target_hp > 25.0f) {
            hp_r = 3;
            hp_g = 252;
            hp_b = 19;
        }
        else if (target_hp > 20.0f) {
            hp_r = 34;
            hp_g = 112;
            hp_b = 39;
        }
        else if (target_hp > 15.0f) {
            hp_r = 237;
            hp_g = 224;
            hp_b = 40;
        }
        else if (target_hp > 10.0f) {
            hp_r = 245;
            hp_g = 140;
            hp_b = 12;
        }
        else if (target_hp > 5.0f) {
            hp_r = 255;
            hp_g = 65;
            hp_b = 51;
        }
        else {
            hp_r = 255;
            hp_g = 25;
            hp_b = 25;
        }

        String pop_str = "";

        try {
            pop_str += (totem_pop_counter.get(target.getName()) == null ? "\u00A7" + "70" : "\u00A7" + "c " + totem_pop_counter.get(target.getName()));
        } catch (Exception ignore) {}

        int str_height = this.get("00hpRRRta", "height") + 3;

        try {
            create_line(target.getName() + " HP: " + (int) target_hp, 3, 3, hp_r, hp_g, hp_b, 255);
            create_line(ping_str, 3, str_height);
            create_line("Distance: " + (int) distance_to_target, 3, str_height * 2);
            create_line("Totems Popped: " + pop_str, 3, str_height * 3);

            int i1 = 3;
            for (int i = target.inventory.armorInventory.size(); i > 0; i--) {
                final ItemStack stack2 = target.inventory.armorInventory.get(i - 1);
                final ItemStack armourStack = stack2.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                renderItemStack(armourStack, this.get_x() + i1, this.get_y() + str_height * 4);
                i1 += 16;
            }

            create_rect(0, this.get_height(), (int) (target_hp / 36 * this.get_width()), this.get_height() -5, hp_r, hp_g, hp_b, 255);

            GL11.glColor3f(1f, 1f, 1f);
            GuiInventory.drawEntityOnScreen(this.get_x() + this.get_width() -20, this.get_y()
                    + this.get_height() - 10, 30, -target.rotationYaw, -target.rotationPitch, target);
        } catch (Exception ignored){}
    }

    private void renderItemStack(final ItemStack stack, final int x, final int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y);
        mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<>();

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketEntityStatus) {

            SPacketEntityStatus packet = (SPacketEntityStatus) event.get_packet();

            if (packet.getOpCode() == 35) {

                Entity entity = packet.getEntity(mc.world);

                int count = 1;

                if (totem_pop_counter.containsKey(entity.getName())) {
                    count = totem_pop_counter.get(entity.getName());
                    totem_pop_counter.put(entity.getName(), ++count);
                } else {
                    totem_pop_counter.put(entity.getName(), count);
                }

            }

        }
    });


    private void update_pops () {
        for (EntityPlayer player : mc.world.playerEntities) {

            if (!totem_pop_counter.containsKey(player.getName())) continue;

            if (player.isDead || player.getHealth() <= 0) {

                totem_pop_counter.remove(player.getName());
            }
        }
    }
}

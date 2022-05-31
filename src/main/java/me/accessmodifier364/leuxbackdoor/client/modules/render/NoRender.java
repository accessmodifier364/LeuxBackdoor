package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderArmorOverlay;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderBossHealth;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventSetupFog;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class NoRender extends Module {
    public NoRender() {
        super(Category.render);
        this.name = "NoRender";
        this.description = "xd";
    }

    Setting minecarts = create("Minecarts", "Minecarts", false);
    Setting armor = create("Armor", "Armor", true);
    Setting fire = create("Fire", "Fire", true);
    Setting hurtcam = create("HurtCam", "NoHurtCam", true);
    Setting pumpkin = create("Pumpkin", "Pumpkin", true);
    Setting falling_blocks = create("Falling Blocks", "FallingBlocks", true);
    Setting weather = create("Weather", "Weather", true);
    Setting blind = create("Blind", "Blind", true);
    Setting nausea = create("Nausea", "Nausea", true);
    Setting fog = create("Fog", "Fog", true);
    Setting advancements = create("Advancements", "Advancements", true);
    Setting portaloverlay = create("Portal Overlay", "PortalOverlay", true);
    Setting potion = create("Potion Icons", "PotionIcons", true);
    Setting bossbar = create("BossBar", "BossBar", true); //INTRODUCIR EVENT
    Setting totemanimation = create("TotemAnimation", "TotemAnimation", false);
    Setting noclip = create("Camera Clip", "CameraClip", false);
    Setting settingFloorDroppedItem = create("Floor Dropped Item", "FloorDroppedItem", false);
    Setting settingCustomWorldTime = create("Custom World Time", "CustomWorldTime", false);
    Setting settingWorldTime = create("World Time", "WorldTime", 1000, 0, 23000);

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        if (!fog.get_value(true)) {
            event.setDensity(0.0f);
            event.setCanceled(true);
        }
    }

    @Override
    protected void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void update() {
        if (minecarts.get_value(true)) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityMinecart) {
                    mc.world.removeEntity(entity);
                }
            }
        }

        if (blind.get_value(true) && mc.player.isPotionActive(MobEffects.BLINDNESS)) mc.player.removePotionEffect(MobEffects.BLINDNESS);
        if (nausea.get_value(true) && mc.player.isPotionActive(MobEffects.NAUSEA)) mc.player.removePotionEffect(MobEffects.NAUSEA);
        if (falling_blocks.get_value(true)) {
            mc.world.loadedEntityList.stream().filter(e -> e instanceof net.minecraft.entity.item.EntityFallingBlock)
                    .forEach(e -> mc.world.removeEntity(e));
        }
        
        if (weather.get_value(true)) {
            if (mc.world == null) return;
            if (mc.world.isRaining()) mc.world.setRainStrength(0.0f);
            if (mc.world.isThundering()) mc.world.setThunderStrength(0.0f);
        }

        if (settingFloorDroppedItem.get_value(true)) {
            for (Entity entities : mc.world.loadedEntityList) {
                if (entities instanceof EntityItem) {
                    EntityItem entityItem = (EntityItem) entities;
                    entityItem.setDead();
                }
            }
        }

        if (settingCustomWorldTime.get_value(true)) {
            mc.world.setWorldTime(settingWorldTime.get_value(1));
            if (!mc.world.isDaytime()){
                mc.player.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
            }
        }
    }

    @Override
    protected void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @EventHandler
    public Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener = new Listener<>(event -> {
        if (fire.get_value(true) && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.setCanceled(true);
        }

        if (pumpkin.get_value(true) && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK) {
            event.setCanceled(true);
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> onServerPacket = new Listener<>(event -> {
        if (mc.world == null) return;

        if (event.get_era() != EventCancellable.Era.EVENT_PRE)
            return;

        if (event.get_packet() instanceof SPacketEntityStatus && totemanimation.get_value(true)) {
            SPacketEntityStatus l_Packet = (SPacketEntityStatus)event.get_packet();
            if (l_Packet.getOpCode() == 35) {
                event.cancel();
            }
        }

        if (event.get_packet() instanceof SPacketTimeUpdate && settingCustomWorldTime.get_value(true)) {
            event.cancel();
        }

    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<EventRenderBossHealth> OnRenderBossHealth = new Listener<>(p_Event -> {
        if (mc.world == null) {
            return;
        }

        if (bossbar.get_value(true)) {
            p_Event.cancel();
        }
    });

    @EventHandler
    Listener<EventRenderArmorOverlay> eventarmor = new Listener<>(event -> {
        if (armor.get_value(true) && event.entity instanceof EntityPlayer) {
            event.cancel();
        }
        if (ModLoader.get_module_manager().get_module_with_tag("ESP").is_active()) {
            armor.set_value(true);
        }
    });

    @EventHandler
    private final Listener<EventSetupFog> setup_fog = new Listener<> (event -> {
        if (!fog.get_value(true)) {
            event.cancel();
            mc.entityRenderer.setupFogColor(false);
            GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.colorMaterial(1028, 4608);
        }
    });
}
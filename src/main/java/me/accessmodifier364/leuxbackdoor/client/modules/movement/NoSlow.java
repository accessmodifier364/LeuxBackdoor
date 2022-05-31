package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.Wrapper;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlow extends Module {
    public NoSlow() {
        super(Category.movement);
		this.name        = "NoSlow";
		this.description = "Just no slows";
    }

    Setting bypass = create("ByPass", "ByPass", false);
    private boolean sneaking;

    @EventHandler
    private final Listener<InputUpdateEvent> eventListener = new Listener<>(event -> {
        if (!bypass.get_value(true)) {
            if (mc.player.isHandActive() && !mc.player.isRiding()) {
                event.getMovementInput().moveStrafe *= 5.0f;
                event.getMovementInput().moveForward *= 5.0f;
            }
        }
    });

    @Override
    public void update() {
        if (bypass.get_value(true)) {
            if (mc.world != null) {
                Item item = Wrapper.getPlayer().getActiveItemStack().getItem();
                if (sneaking && ((!Wrapper.getPlayer().isHandActive() && item instanceof ItemFood || item instanceof ItemBow || item instanceof ItemPotion) || (!(item instanceof ItemFood) || !(item instanceof ItemBow) || !(item instanceof ItemPotion)))) {
                    Wrapper.getPlayer().connection.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.STOP_SNEAKING));
                    sneaking = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        if (bypass.get_value(true)) {
            if (!sneaking) {
                Wrapper.getPlayer().connection.sendPacket(new CPacketEntityAction(Wrapper.getPlayer(), CPacketEntityAction.Action.START_SNEAKING));
                sneaking = true;
            }
        }
    }
}


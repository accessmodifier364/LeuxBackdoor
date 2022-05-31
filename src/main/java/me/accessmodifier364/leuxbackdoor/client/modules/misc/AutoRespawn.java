package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventGUIScreen;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;

public class AutoRespawn extends Module {
  public AutoRespawn() {
    super(Category.misc);
    this.name = "AutoRespawn";
    this.description = "Respawn Fast lol";
  }

  Setting coords = create("DeathCoords","DeathCoords",true);

  @EventHandler
  Listener<EventGUIScreen> listener = new Listener<>(event -> {
    if (event.get_guiscreen() instanceof net.minecraft.client.gui.GuiGameOver) {
      if (coords.get_value(true)) MessageUtil.send_client_message(String.format("You died at x%d y%d z%d", (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ));
      if (mc.player != null)
        mc.player.respawnPlayer();
      mc.displayGuiScreen(null);
    mc.player.noClip = true;
    }
  });

  public void enable() {
    EventClientBus.EVENT_BUS.subscribe(this);
  }
  
  public void disable() {
    EventClientBus.EVENT_BUS.unsubscribe(this);
  }
}
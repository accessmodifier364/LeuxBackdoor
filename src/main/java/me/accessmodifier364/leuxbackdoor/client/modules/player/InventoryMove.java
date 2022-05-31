package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventGUIScreen;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
	public InventoryMove() {
		super(Category.player);
		this.name = "InventoryMove";
		this.description = "move in guis";
	}
	private static final KeyBinding[] KEYS = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};

	@EventHandler
	private final Listener<EventGUIScreen> state_gui = new Listener<>(event -> {
		if (mc.player == null && mc.world == null) {
			return;
		}
		if (event.get_guiscreen() instanceof GuiChat || event.get_guiscreen() == null) {
			return;
		}
		this.walk();
	});


	@Override
	public void update() {
		if (mc.player == null && mc.world == null) {
			return;
		}
		if (mc.currentScreen instanceof GuiChat || mc.currentScreen == null) {
			return;
		}
		this.walk();
	}

	public void walk() {
		for (KeyBinding key_binding : KEYS) {
			if (Keyboard.isKeyDown(key_binding.getKeyCode())) {
				if (key_binding.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
					key_binding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
				}
				KeyBinding.setKeyBindState(key_binding.getKeyCode(), true);
				continue;
			}
			KeyBinding.setKeyBindState(key_binding.getKeyCode(), false);
		}
	}
}
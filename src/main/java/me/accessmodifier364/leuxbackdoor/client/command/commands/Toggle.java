package me.accessmodifier364.leuxbackdoor.client.command.commands;

import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.manager.CommandManager;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class Toggle extends Command {
	public Toggle() {
		super("t", "turn on and off stuffs");
	}

	public boolean get_message(String[] message) {
		String module = "null";

		if (message.length > 1) {
			module = message[1];
		}

		if (message.length > 2) {
			MessageUtil.send_client_error_message(current_prefix() + "t <ModuleNameNoSpace>");

			return true;
		}

		if (module.equals("null")) {
			MessageUtil.send_client_error_message(CommandManager.get_prefix() + "t <ModuleNameNoSpace>");

			return true;
		}

		Module module_requested = ModLoader.get_module_manager().get_module_with_tag(module);

		if (module_requested != null) {
			module_requested.toggle();

			MessageUtil.send_client_message("[" + module_requested.get_tag() + "] - Toggled to " + module_requested.is_active() + ".");
		} else {
			MessageUtil.send_client_error_message("Module does not exist.");
		}

		return true;
	}
}
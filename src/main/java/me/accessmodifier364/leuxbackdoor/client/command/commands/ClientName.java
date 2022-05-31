package me.accessmodifier364.leuxbackdoor.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.client.util.RenameUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class ClientName extends Command {

    public ClientName() {
        super("clientname", "Set the client name");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("client name needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder client_name = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                client_name.append(word);
            }
            RenameUtil.set_client_name(client_name.toString());
            MessageUtil.send_client_message("client name changed to " + ChatFormatting.AQUA + ChatFormatting.BOLD + client_name.toString());
            ModLoader.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}

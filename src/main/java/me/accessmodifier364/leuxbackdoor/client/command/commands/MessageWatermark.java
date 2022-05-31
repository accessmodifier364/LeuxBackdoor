package me.accessmodifier364.leuxbackdoor.client.command.commands;

import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.client.util.RenameUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class MessageWatermark extends Command {

    public MessageWatermark() {
        super("messagewatermark", "Set the message watermark name");
    }

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_error_message("watermark needed");
            return true;
        }

        if (message.length >= 2) {
            StringBuilder messageWatermark = new StringBuilder();
            boolean flag = true;
            for (String word : message) {
                if (flag) {
                    flag = false;
                    continue;
                }
                messageWatermark.append(word);
            }
            String processedWatermark = RenameUtil.asciiToUtf8(messageWatermark.toString());
            RenameUtil.set_message_watermark(messageWatermark.toString());
            MessageUtil.send_client_message("Message watermark changed to " + processedWatermark);
            ModLoader.get_config_manager().save_settings();
            return true;
        }

        return false;

    }

}

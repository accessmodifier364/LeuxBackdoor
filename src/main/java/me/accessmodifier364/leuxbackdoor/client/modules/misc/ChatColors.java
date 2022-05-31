package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatColors extends Module { //Writted by ObsidianBreaker
    
    public ChatColors() {
        super(Category.misc);
        this.name = "ChatColors";
        this.description = "brrr rainbow xd";
    }

    Setting color = create("Color", "ChatColor", "Green", combobox("Green", "Yellow", "Blue", "None"));
    Setting ignore = create("Ignore", "ChatColorIgnore", true);

    @EventHandler
    private Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
        if (!(event.get_packet() instanceof CPacketChatMessage)) {
            return;
        }
        boolean accept_color;
        accept_color = true;
        String colorstring = "";
        String message = ((CPacketChatMessage) event.get_packet()).getMessage();
        boolean ignore_prefix = ignore.get_value(true);
        if (message.startsWith("/")  && ignore_prefix) accept_color = false;
        if (message.startsWith("\\") && ignore_prefix) accept_color = false;
        if (message.startsWith("!")  && ignore_prefix) accept_color = false;
        if (message.startsWith(":")  && ignore_prefix) accept_color = false;
        if (message.startsWith(";")  && ignore_prefix) accept_color = false;
        if (message.startsWith(".")  && ignore_prefix) accept_color = false;
        if (message.startsWith(",")  && ignore_prefix) accept_color = false;
        if (message.startsWith("@")  && ignore_prefix) accept_color = false;
        if (message.startsWith("&")  && ignore_prefix) accept_color = false;
        if (message.startsWith("*")  && ignore_prefix) accept_color = false;
        if (message.startsWith("$")  && ignore_prefix) accept_color = false;
        if (message.startsWith("#")  && ignore_prefix) accept_color = false;
        if (message.startsWith("(")  && ignore_prefix) accept_color = false;
        if (message.startsWith(")")  && ignore_prefix) accept_color = false;
        if (color.in("Green")) colorstring = ">";
        if (color.in("Yellow")) colorstring = "#";
        if (color.in("Blue")) colorstring = "``";
        if (color.in("None")) colorstring = "";
        if (accept_color) {
            message = colorstring + message;
        }
        ((CPacketChatMessage) event.get_packet()).message = message;
    });
}
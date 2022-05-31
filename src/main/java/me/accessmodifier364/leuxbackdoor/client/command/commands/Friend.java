package me.accessmodifier364.leuxbackdoor.client.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;

public class Friend extends Command {

    public Friend() {
        super("friend", "To add friends");
    }

    public static ChatFormatting red = ChatFormatting.RED;
    public static ChatFormatting green = ChatFormatting.GREEN;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting reset = ChatFormatting.RESET;

    public boolean get_message(String[] message) {

        if (message.length == 1) {
            MessageUtil.send_client_message("Add - add friend");
            MessageUtil.send_client_message("Del - delete friend");
            MessageUtil.send_client_message("List - list friends");

            return true;
        }

        if (message.length == 2) {
            if (message[1].equalsIgnoreCase("list")) {
                if (FriendUtil.friends.isEmpty()) {
                    MessageUtil.send_client_message("You appear to have " + red + bold + "no" + reset + " friends :(");
                } else {
                    for (FriendUtil.Friend friend : FriendUtil.friends) {
                        MessageUtil.send_client_message("" + green + bold + friend.getUsername());
                    }
                }
            } else {
                if (FriendUtil.isFriend(message[1])) {
                    MessageUtil.send_client_message("Player " + green + bold + message[1] + reset + " is your friend :D");
                } else {
                    MessageUtil.send_client_error_message("Player " + red + bold + message[1] + reset + " is not your friend :(");
                }
            }
            return true;
        }

        if (message.length >= 3) {
            if (message[1].equalsIgnoreCase("add")) {
                if (FriendUtil.isFriend(message[2])) {
                    MessageUtil.send_client_message("Player " + green + bold + message[2] + reset + " is already your friend :D");
                } else {
                    FriendUtil.Friend f = FriendUtil.get_friend_object(message[2]);
                    if (f == null) {
                        MessageUtil.send_client_error_message("Cannot find " + red + bold + "UUID" + reset + " for that player :(");
                        return true;
                    }
                    FriendUtil.friends.add(f);
                    MessageUtil.send_client_message("Player " + green + bold + message[2] + reset + " is now your friend :D");
                    if (ModLoader.get_setting_manager().get_setting_with_tag("Settings", "MSGWhenFriend").get_value(true)) {
                        mc.player.sendChatMessage("/msg " + message[2] + " i added u to my friends list <3");
                    }
                }
                return true;
            } else if (message[1].equalsIgnoreCase("del") || message[1].equalsIgnoreCase("remove") || message[1].equalsIgnoreCase("delete")) {
                if (!FriendUtil.isFriend(message[2])) {
                    MessageUtil.send_client_message("Player " + red + bold + message[2] + reset + " is already not your friend :/");
                } else {
                    FriendUtil.Friend f = FriendUtil.friends.stream().filter(friend -> friend.getUsername().equalsIgnoreCase(message[2])).findFirst().get();
                    FriendUtil.friends.remove(f);
                    MessageUtil.send_client_message("Player " + red + bold + message[2] + reset + " is now not your friend :(");
                    if (ModLoader.get_setting_manager().get_setting_with_tag("Settings", "MSGWhenFriend").get_value(true)) {
                        mc.player.sendChatMessage("/msg " + message[2] + " i deleted u from my friends list >:(");
                    }
                }
                return true;
            }
        }
        return true;
    }

}
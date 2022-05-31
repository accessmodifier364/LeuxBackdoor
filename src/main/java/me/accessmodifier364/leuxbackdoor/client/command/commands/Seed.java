package me.accessmodifier364.leuxbackdoor.client.command.commands;


import me.accessmodifier364.leuxbackdoor.client.command.Command;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;


public class Seed extends Command {
    public Seed() {
        super("seed", "why u want raid bases");
    }

    public boolean get_message(String[] message) {
        if (mc.player != null && mc.world != null) {
            MessageUtil.send_client_message("World seed: " + mc.world.getSeed());
        }
        return true;
    }
}
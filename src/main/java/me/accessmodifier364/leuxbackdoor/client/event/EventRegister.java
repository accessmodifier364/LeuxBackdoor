package me.accessmodifier364.leuxbackdoor.client.event;

import me.accessmodifier364.leuxbackdoor.client.manager.CommandManager;
import me.accessmodifier364.leuxbackdoor.client.manager.EventManager;
import net.minecraftforge.common.MinecraftForge;


public class EventRegister {
    public static void register_command_manager(CommandManager manager) {
        MinecraftForge.EVENT_BUS.register(manager);
    }

    public static void register_module_manager(EventManager manager) {
        MinecraftForge.EVENT_BUS.register(manager);
    }
}
package me.accessmodifier364.leuxbackdoor.client.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.client.Minecraft;

public class DiscordUtil {
    private static final Minecraft mc;
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static String details;
    private static String state;
    private static int index;

    public static void start() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        DiscordUtil.rpc.Discord_Initialize("834568476747759687", handlers, true, "");
        DiscordUtil.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordUtil.presence.details = "";
        DiscordUtil.presence.state = "Main Menu";
        DiscordUtil.presence.largeImageKey = "buggy";
        DiscordUtil.presence.largeImageText = ModLoader.VERSION;
        DiscordUtil.rpc.Discord_UpdatePresence(DiscordUtil.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DiscordUtil.rpc.Discord_RunCallbacks();
                    if (mc.player != null) {
                        DiscordUtil.details = mc.player.getName();
                    } else {
                        DiscordUtil.details = "";
                    }
                    DiscordUtil.state = "";
                    if (DiscordUtil.mc.isIntegratedServerRunning()) {
                        DiscordUtil.state = "Playing on Singleplayer";
                    } else if (DiscordUtil.mc.getCurrentServerData() != null) {
                        if (!DiscordUtil.mc.getCurrentServerData().serverIP.equals("")) {
                            if (ModLoader.get_setting_manager().get_setting_with_tag("RPC", "ShowIP").get_value(true)) {
                                DiscordUtil.state = DiscordUtil.mc.getCurrentServerData().serverIP;
                            } else {
                                DiscordUtil.state = "Playing on Multiplayer";
                            }
                        }
                    } else {
                        DiscordUtil.state = "Main Menu";
                    }
                    if (!DiscordUtil.details.equals(DiscordUtil.presence.details)) {
                        DiscordUtil.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    DiscordUtil.presence.details = DiscordUtil.details;
                    DiscordUtil.presence.state = DiscordUtil.state;
                    if (index > 10) {
                        index = 1;
                    } else {
                        DiscordUtil.presence.largeImageKey = "animated" + index;
                        ++index;
                    }
                    DiscordUtil.rpc.Discord_UpdatePresence(DiscordUtil.presence);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }

    static {
        mc = Minecraft.getMinecraft();
        rpc = DiscordRPC.INSTANCE;
        DiscordUtil.presence = new DiscordRichPresence();
    }

    public static void stop() {
        rpc.Discord_Shutdown();
    }
}
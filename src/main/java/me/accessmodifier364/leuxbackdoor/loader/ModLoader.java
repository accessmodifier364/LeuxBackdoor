package me.accessmodifier364.leuxbackdoor.loader;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.event.EventHandler;
import me.accessmodifier364.leuxbackdoor.client.event.EventRegister;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.GUI;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.HUD;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.NewGui;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.newgui.font.CustomFontRenderer;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.render.font.CFontRenderer;
import me.accessmodifier364.leuxbackdoor.client.manager.*;
import me.accessmodifier364.leuxbackdoor.client.util.DiscordUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.TurokOld;
import me.zero.alpine.fork.bus.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Mod(modid = "leux", version = ModLoader.VERSION)
public class ModLoader {

    public static final String VERSION = "0.9";
    public static final String SIGN = " ";
    public static final int KEY_GUI = Keyboard.KEY_RSHIFT;
    public static final int KEY_DELETE = Keyboard.KEY_DELETE;
    public static final int KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;
    public static final EventBus EVENT_BUS = new me.zero.alpine.fork.bus.EventManager();
    public static String CLIENT_NAME = "LeuxBackdoor";
    public static CFontRenderer fontRenderer;
    public static Logger register_log;
    public static GUI click_gui;
    public static NewGui new_gui;
    public static HUD click_hud;
    public static TurokOld turok;
    public static CustomFontRenderer latoFont;
    public static ChatFormatting g = ChatFormatting.GRAY;
    public static ChatFormatting r = ChatFormatting.RESET;

    @Mod.Instance
    private static ModLoader MASTER;
    private static SettingManager setting_manager;
    private static ConfigManager config_manager;
    private static ModuleManager module_manager;
    private static HUDManager hud_manager;

    public static void send_minecraft_log(String log) {
        register_log.info(log);
    }

    public static String get_client_name() {
        return CLIENT_NAME;
    }

    public static void set_client_name(String name) {
        CLIENT_NAME = name;
    }

    public static String get_version() {
        return VERSION;
    }

    public static String get_actual_user() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    public static ConfigManager get_config_manager() {
        return config_manager;
    }

    public static ModuleManager get_hack_manager() {
        return module_manager;
    }

    public static SettingManager get_setting_manager() {
        return setting_manager;
    }

    public static HUDManager get_hud_manager() {
        return hud_manager;
    }

    public static ModuleManager get_module_manager() {
        return module_manager;
    }

    public static EventHandler get_event_handler() {
        return EventHandler.INSTANCE;
    }

    public static String smoth(String base) {
        return me.accessmodifier364.leuxbackdoor.client.util.turok.task.Font.smoth(base);
    }

    @Mod.EventHandler
    public void Starting(FMLInitializationEvent event) {
        init_log(CLIENT_NAME);
        send_minecraft_log("Starting " + CLIENT_NAME + " v" + VERSION);
        ModLoader.fontRenderer = new CFontRenderer(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18), true, false);

        EventHandler.INSTANCE = new EventHandler();

        setting_manager = new SettingManager();
        config_manager = new ConfigManager();
        module_manager = new ModuleManager();
        hud_manager = new HUDManager();
        EventManager event_manager = new EventManager();
        CommandManager command_manager = new CommandManager();

        Display.setTitle(CLIENT_NAME + " v" + VERSION);

        try {
            InputStream bigIcon = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("custom/icon_32.png")).getInputStream();
            InputStream smallIcon = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("custom/icon_16.png")).getInputStream();
            Display.setIcon(new ByteBuffer[]{loadIcon(bigIcon), loadIcon(smallIcon)});
        } catch (IOException e) {
            e.printStackTrace();
        }

        latoFont = new CustomFontRenderer(new Font("Lato", Font.ITALIC, 18), true, true);

        click_gui = new GUI();
        click_hud = new HUD();
        new_gui = new NewGui();

        turok = new TurokOld("Turok");

        // Register event modules and manager.
        EventRegister.register_command_manager(command_manager);
        EventRegister.register_module_manager(event_manager);

        config_manager.load_settings();

        if (module_manager.get_module_with_tag("GUI").is_active()) {
            module_manager.get_module_with_tag("GUI").set_active(false);
        }

        if (module_manager.get_module_with_tag("HUD").is_active()) {
            module_manager.get_module_with_tag("HUD").set_active(false);
        }

        if (module_manager.get_module_with_tag("GuiModule").is_active()) {
            module_manager.get_module_with_tag("GuiModule").set_active(false);
        }

    }

    @Mod.EventHandler
    public void Loaded(FMLPostInitializationEvent event) {
        if (ModLoader.get_hack_manager().get_module_with_tag("RPC").is_active()) {
            try {
                DiscordUtil.start();
            } catch (Exception ignored) {
            }
        }
    }

    public void init_log(String name) {
        register_log = LogManager.getLogger(name);
    }

    private ByteBuffer loadIcon(final InputStream iconFile) throws IOException {
        final BufferedImage icon = ImageIO.read(iconFile);

        final int[] rgb = icon.getRGB(0, 0, icon.getWidth(), icon.getHeight(), null, 0, icon.getWidth());

        final ByteBuffer buffer = ByteBuffer.allocate(4 * rgb.length);
        for (int color : rgb) {
            buffer.putInt(color << 8 | ((color >> 24) & 0xFF));
        }
        buffer.flip();
        return buffer;
    }

}
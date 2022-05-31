package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.Random;

public class ChatSuffix extends Module {
	public ChatSuffix() {
		super(Category.misc);
		this.name        = "ChatSuffix";
		this.description = "show off how cool u are";
	}

	Setting ignore = create("Ignore", "ChatSuffixIgnore", true);
	Setting type   = create("Type", "ChatSuffixType", "Leux", combobox("Leux", "Obsidian", "2B2T", "Random", "ClientName"));
	Setting separator = create("Separator", "ChatSuffixSeparator", "None", combobox("\u00bb", "\u00ab", "\u23d0", "\u269d", "None"));

	boolean accept_suffix;
	boolean suffix_2b2t = false;
	boolean suffix_leux = false;
	boolean suffix_random = false;
	boolean suffix_obsidian = false;
	boolean suffix_clientname = false;
	String text_separator = "";

    String[] random_client_name = {
		"leux",
			"luscius",
			"obsidian",
		"backdoor"
	};

	String[] random_client_finish = {
		" plus",
		" luscius",
		"+",
		" bbcversion",
		" brrr",
		" antiniggers",
		" popper",
		" backdoor",
		" obsidian",
			" miner",
		" cold",
			" tater",
		" user"
	};

	@EventHandler
	private final Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {
		if (!(event.get_packet() instanceof CPacketChatMessage)) {
			return;
		}
		accept_suffix = true;
		boolean ignore_prefix = ignore.get_value(true);
		String message = ((CPacketChatMessage) event.get_packet()).getMessage();
		if (message.startsWith("/")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("\\") && ignore_prefix) accept_suffix = false;
		if (message.startsWith("!")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(":")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(";")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(".")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(",")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("@")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("&")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("*")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("$")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("#")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith("(")  && ignore_prefix) accept_suffix = false;
		if (message.startsWith(")")  && ignore_prefix) accept_suffix = false;
		if (separator.in("\u00bb")) text_separator = "\u00bb";
		if (separator.in("\u00ab")) text_separator = "\u00ab";
		if (separator.in("\u23d0")) text_separator = "\u23d0";
		if (separator.in("\u269d")) text_separator = "\u269d";
		if (separator.in("None")) text_separator = "";
		if (type.in("Leux")) {
			suffix_leux = true;
			suffix_obsidian = false;
			suffix_2b2t = false;
			suffix_random  = false;
			suffix_clientname = false;
		}
		if (type.in("Obsidian")) {
			suffix_leux = false;
			suffix_obsidian = true;
			suffix_2b2t = false;
			suffix_random  = false;
            suffix_clientname = false;
		}
		if (type.in("2B2T")) {
			suffix_leux = false;
			suffix_obsidian = false;
			suffix_2b2t = true;
			suffix_random  = false;
            suffix_clientname = false;
		}
		if (type.in("Random")) {
			suffix_leux = false;
			suffix_obsidian = false;
			suffix_2b2t = false;
			suffix_random  = true;
            suffix_clientname = false;
		}
		if (type.in("ClientName")) {
            suffix_leux = false;
            suffix_obsidian = false;
            suffix_2b2t = false;
            suffix_random  = false;
            suffix_clientname = true;
        }

		if (accept_suffix) {
			if (suffix_leux) {
				message += ModLoader.SIGN + text_separator + ModLoader.SIGN + "L\u03a3uxB\u03b1ckdoor";
			}

			if (suffix_obsidian) {
				message += ModLoader.SIGN + text_separator + ModLoader.SIGN + convert_base("obsidian+");
			}

			if (suffix_2b2t) {
				message += ModLoader.SIGN + text_separator + ModLoader.SIGN + "LeuxBackdoor";
			}

			if (suffix_clientname) {
                message += ModLoader.SIGN + text_separator + ModLoader.SIGN + convert_base(ModLoader.get_client_name().toLowerCase());
            }

			if (suffix_random) {
                String suffix_with_randoms = convert_base(random_string(random_client_name)) +
                        convert_base(random_string(random_client_finish));
                message += ModLoader.SIGN + text_separator + ModLoader.SIGN + suffix_with_randoms;
			}
			if (message.length() >= 256) {
				message.substring(0, 256);
			}
		}
		((CPacketChatMessage) event.get_packet()).message = message;
	});
	public String random_string(String[] list) {
		return list[new Random().nextInt(list.length)];
	}
	public String convert_base(String base) {
		return ModLoader.smoth(base);
	}

	@Override
	public String array_detail() {
		return this.type.get_current_value();
	}
}
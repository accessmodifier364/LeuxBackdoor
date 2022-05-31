package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class AntiSpam extends Module {
    public AntiSpam() {
        super(Category.player);
        this.name = "AntiSpam";
        this.description = "xddd";
    }

    Setting greenText = create("Green Text", "GreenText", false);
    Setting discordLinks = create("Smart Mode", "DiscordLinks", false);
    Setting webLinks = create("Web Links", "WebLinks", false);
    Setting announcers = create("Announcers", "Announcers", false);
    Setting spammers = create("Spammers", "Spammers", true);
    Setting insulters = create("Insulters", "Insulters", false);
    Setting greeters = create("Greeters", "Greeters", false);
    Setting tradeChat = create("Trade Chat", "TradeChat", false);
    Setting numberSuffix = create("Number Suffix", "NumberSuffix", false);
    Setting duplicates = create("Duplicates", "Duplicates", false);
    Setting duplicatesTimeout = create("Duplicates Timeout", "DuplicatesTimeout", 10, 1, 600);
    Setting filterOwn = create("Filter Own", "FilterOwn", false);
    Setting debug = create("Debug Messages", "DebugMessages", false);

    private ConcurrentHashMap<String, Long> messageHistory;
    
    @EventHandler
    private final Listener<EventPacket.ReceivePacket> listener = new Listener<>(event -> {
        if (mc.player == null || this.is_disabled()) {
            return;
        }
        if (!(event.get_packet() instanceof SPacketChat)) {
            return;
        }
        SPacketChat sPacketChat = (SPacketChat) event.get_packet();
        if (this.detectSpam(sPacketChat.getChatComponent().getUnformattedText())) {
            event.cancel();
        }
    });

    @Override
    public void enable() {
        this.messageHistory = new ConcurrentHashMap();
    }

    @Override
    public void disable() {
        this.messageHistory = null;
    }

    private boolean detectSpam(String message) {
        if (!this.filterOwn.get_value(true) && this.findPatterns(FilterPatterns.OWN_MESSAGE, message)) {
            return false;
        }
        if (this.greenText.get_value(true) && this.findPatterns(FilterPatterns.GREEN_TEXT, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Green Text: " + message);
            }
            return true;
        }
        if (this.discordLinks.get_value(true) && this.findPatterns(FilterPatterns.DISCORD, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Discord Link: " + message);
            }
            return true;
        }
        if (this.webLinks.get_value(true) && this.findPatterns(FilterPatterns.WEB_LINK, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Web Link: " + message);
            }
            return true;
        }
        if (this.tradeChat.get_value(true) && this.findPatterns(FilterPatterns.TRADE_CHAT, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Trade Chat: " + message);
            }
            return true;
        }
        if (this.numberSuffix.get_value(true) && this.findPatterns(FilterPatterns.NUMBER_SUFFIX, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Number Suffix: " + message);
            }
            return true;
        }
        if (this.announcers.get_value(true) && this.findPatterns(FilterPatterns.ANNOUNCER, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Announcer: " + message);
            }
            return true;
        }
        if (this.spammers.get_value(true) && this.findPatterns(FilterPatterns.SPAMMER, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Spammers: " + message);
            }
            return true;
        }
        if (this.insulters.get_value(true) && this.findPatterns(FilterPatterns.INSULTER, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Insulter: " + message);
            }
            return true;
        }
        if (this.greeters.get_value(true) && this.findPatterns(FilterPatterns.GREETER, message)) {
            if (this.debug.get_value(true)) {
                MessageUtil.send_client_message("[AntiSpam] Greeter: " + message);
            }
            return true;
        }
        if (this.duplicates.get_value(true)) {
            if (this.messageHistory == null) {
                this.messageHistory = new ConcurrentHashMap();
            }
            boolean isDuplicate = false;
            if (this.messageHistory.containsKey(message) && (System.currentTimeMillis() - this.messageHistory.get(message)) / 1000L < (long)this.duplicatesTimeout.get_value(1)) {
                isDuplicate = true;
            }
            this.messageHistory.put(message, System.currentTimeMillis());
            if (isDuplicate) {
                if (this.debug.get_value(true)) {
                    MessageUtil.send_client_message("[AntiSpam] Duplicate: " + message);
                }
                return true;
            }
        }
        return false;
    }

    private boolean findPatterns(String[] patterns, String string) {
        for (String pattern : patterns) {
            if (!Pattern.compile(pattern).matcher(string).find()) continue;
            return true;
        }
        return false;
    }

    private static class FilterPatterns {
        private static final String[] ANNOUNCER = new String[]{"I just walked .+ feet!", "I just placed a .+!", "I just attacked .+ with a .+!", "I just dropped a .+!", "I just opened chat!", "I just opened my console!", "I just opened my GUI!", "I just went into full screen mode!", "I just paused my game!", "I just opened my inventory!", "I just looked at the player list!", "I just took a screen shot!", "I just swaped hands!", "I just ducked!", "I just changed perspectives!", "I just jumped!", "I just ate a .+!", "I just crafted .+ .+!", "I just picked up a .+!", "I just smelted .+ .+!", "I just respawned!", "I just attacked .+ with my hands", "I just broke a .+!", "I recently walked .+ blocks", "I just droped a .+ called, .+!", "I just placed a block called, .+!", "Im currently breaking a block called, .+!", "I just broke a block called, .+!", "I just opened chat!", "I just opened chat and typed a slash!", "I just paused my game!", "I just opened my inventory!", "I just looked at the player list!", "I just changed perspectives, now im in .+!", "I just crouched!", "I just jumped!", "I just attacked a entity called, .+ with a .+", "Im currently eatting a peice of food called, .+!", "Im currently using a item called, .+!", "I just toggled full screen mode!", "I just took a screen shot!", "I just swaped hands and now theres a .+ in my main hand and a .+ in my off hand!", "I just used pick block on a block called, .+!", "Ra just completed his blazing ark", "Its a new day yes it is", "I just placed .+ thanks to (http:\\/\\/)?DotGod\\.CC!", "I just flew .+ meters like a butterfly thanks to (http:\\/\\/)?DotGod\\.CC!"};
        private static final String[] SPAMMER = new String[]{"Sigma Rule #", "Fact #", "WWE Client's spammer", "Lol get gud", "Future client is bad", "WWE > Future", "WWE > Impact", "Default Message", "IKnowImEZ is a god", "THEREALWWEFAN231 is a god", "WWE Client made by IKnowImEZ/THEREALWWEFAN231", "WWE Client was the first public client to have Path Finder/New Chunks", "WWE Client was the first public client to have color signs", "WWE Client was the first client to have Teleport Finder", "WWE Client was the first client to have Tunneller & Tunneller Back Fill"};
        private static final String[] INSULTER = new String[]{".+ Download WWE utility mod, Its free!", ".+ 4b4t is da best mintscreft serber", ".+ dont abouse", ".+ you cuck", ".+ https://www.youtube.com/channel/UCJGCNPEjvsCn0FKw3zso0TA", ".+ is my step dad", ".+ again daddy!", "dont worry .+ it happens to every one", ".+ dont buy future it's crap, compared to WWE!", "What are you, fucking gay, .+?", "Did you know? .+ hates you, .+", "You are literally 10, .+", ".+ finally lost their virginity, sadly they lost it to .+... yeah, that's unfortunate.", ".+, don't be upset, it's not like anyone cares about you, fag.", ".+, see that rubbish bin over there? Get your ass in it, or I'll get .+ to whoop your ass.", ".+, may I borrow that dirt block? that guy named .+ needs it...", "Yo, .+, btfo you virgin", "Hey .+ want to play some High School RP with me and .+?", ".+ is an Archon player. Why is he on here? Fucking factions player.", "Did you know? .+ just joined The Vortex Coalition!", ".+ has successfully conducted the cactus dupe and duped a itemhand!", ".+, are you even human? You act like my dog, holy shit.", ".+, you were never loved by your family.", "Come on .+, you hurt .+'s feelings. You meany.", "Stop trying to meme .+, you can't do that. kek", ".+, .+ is gay. Don't go near him.", "Whoa .+ didn't mean to offend you, .+.", ".+ im not pvping .+, im WWE'ing .+.", "Did you know? .+ just joined The Vortex Coalition!", ".+, are you even human? You act like my dog, holy shit."};
        private static final String[] GREETER = new String[]{"Bye, Bye .+", "Farwell, .+"};
        private static final String[] DISCORD = new String[]{"discord.gg"};
        private static final String[] NUMBER_SUFFIX = new String[]{".+-?\\d{3,100}$"};
        private static final String[] GREEN_TEXT = new String[]{"^<.+> >"};
        private static final String[] TRADE_CHAT = new String[]{"buy", "sell"};
        private static final String[] WEB_LINK = new String[]{"http:\\/\\/", "https:\\/\\/"};
        private static final String[] OWN_MESSAGE = new String[]{"^<" + mc.player.getName() + "> ", "^To .+: "};

        private FilterPatterns() {
        }
    }
}
package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;

import java.util.regex.Pattern;

public class ChatEncryption extends Module {
    public ChatEncryption() {
        super(Category.player);
        this.name = "EncryptChat";
        this.description = "madafakin ching chong taka taka";
    }

    Setting showOriginal = create("Show Original", "ShowOriginal", true);
    Setting encrypt = create("Encrypt", "Encrypt", true);
    Setting decrypt = create("Decrypt", "Decrypt", true);

    private static final String[] ENCRYPTED = new String[] {
            "\u2620", "\u2621", "\u2622",
            "\u2623", "\u2624", "\u2625",
            "\u2626", "\u2627", "\u2628",
            "\u2629", "\u262a", "\u262b",
            "\u262c", "\u262d", "\u262e",
            "\u262f", "\u2670", "\u2671",
            "\u2672", "\u2673", "\u2674",
            "\u2675", "\u2676", "\u2677",
            "\u2678", "\u2679", "\u267a"
    };

    /*
\u2620 = a
\u2621 = c
\u2622 = c
\u2623 = d
\u2624 = e
\u2625 = f
\u2626 = g
\u2627 = h
\u2628 = i
\u2629 = j
\u262a = k
\u262b = l
\u262c = m
\u262d = n
\u262e = ñ
\u262f = o
\u2670 = p
\u2671 = q
\u2672 = r
\u2673 = s
\u2674 = t
\u2675 = u
\u2676 = v
\u2677 = w
\u2678 = x
\u2679 = y
\u267a = z
     */

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> listener = new Listener<>(event -> {
        if (mc.player != null && mc.world != null) {
            if (event.get_packet() instanceof SPacketChat) {
                SPacketChat sPacketChat = (SPacketChat) event.get_packet();
                if (detectEncryption(sPacketChat.getChatComponent().getUnformattedText()) && !showOriginal.get_value(true)) {
                    event.cancel();
                }
            }
        }
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> listener2 = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketChatMessage) {
            if (encrypt.get_value(true)) {
                String message = ((CPacketChatMessage) event.get_packet()).getMessage();
                message = encryption(message, false);
                ((CPacketChatMessage) event.get_packet()).message = message;
            }
        }
    });

    private boolean detectEncryption(String message) {
        if (findPatterns(message) && decrypt.get_value(true)) {
                MessageUtil.send_client_message("Decrypted message: " + encryption(message, true));
            return true;
        }
        return false;
    }


    private boolean findPatterns(String string) {
        for (String pattern : ENCRYPTED) {
            if (!Pattern.compile(pattern).matcher(string).find()) continue;
            return true;
        }
        return false;
    }

    public static String encryption(String base, boolean decrypt) {
        String new_base = base;
        if (decrypt) {
            new_base = new_base.replace("\u2620", "a");
            new_base = new_base.replace("\u2621", "b");
            new_base = new_base.replace("\u2622", "c");
            new_base = new_base.replace("\u2623", "d");
            new_base = new_base.replace("\u2624", "e");
            new_base = new_base.replace("\u2625", "f");
            new_base = new_base.replace("\u2626", "g");
            new_base = new_base.replace("\u2627", "h");
            new_base = new_base.replace("\u2628", "i");
            new_base = new_base.replace("\u2629", "j");
            new_base = new_base.replace("\u262a", "k");
            new_base = new_base.replace("\u262b", "l");
            new_base = new_base.replace("\u262c", "m");
            new_base = new_base.replace("\u262d", "n");
            new_base = new_base.replace("\u262e", "ñ");
            new_base = new_base.replace("\u262f", "o");
            new_base = new_base.replace("\u2670", "p");
            new_base = new_base.replace("\u2671", "q");
            new_base = new_base.replace("\u2672", "r");
            new_base = new_base.replace("\u2673", "s");
            new_base = new_base.replace("\u2674", "t");
            new_base = new_base.replace("\u2675", "u");
            new_base = new_base.replace("\u2676", "v");
            new_base = new_base.replace("\u2677", "w");
            new_base = new_base.replace("\u2678", "x");
            new_base = new_base.replace("\u2679", "y");
            new_base = new_base.replace("\u267a", "z");
        } else {
            new_base = new_base.replace("a", "\u2620");
            new_base = new_base.replace("b", "\u2621");
            new_base = new_base.replace("c", "\u2622");
            new_base = new_base.replace("d", "\u2623");
            new_base = new_base.replace("e", "\u2624");
            new_base = new_base.replace("f", "\u2625");
            new_base = new_base.replace("g", "\u2626");
            new_base = new_base.replace("h", "\u2627");
            new_base = new_base.replace("i", "\u2628");
            new_base = new_base.replace("j", "\u2629");
            new_base = new_base.replace("k", "\u262a");
            new_base = new_base.replace("l", "\u262b");
            new_base = new_base.replace("m", "\u262c");
            new_base = new_base.replace("n", "\u262d");
            new_base = new_base.replace("ñ", "\u262e");
            new_base = new_base.replace("o", "\u262f");
            new_base = new_base.replace("p", "\u2670");
            new_base = new_base.replace("q", "\u2671");
            new_base = new_base.replace("r", "\u2672");
            new_base = new_base.replace("s", "\u2673");
            new_base = new_base.replace("t", "\u2674");
            new_base = new_base.replace("u", "\u2675");
            new_base = new_base.replace("v", "\u2676");
            new_base = new_base.replace("w", "\u2677");
            new_base = new_base.replace("x", "\u2678");
            new_base = new_base.replace("y", "\u2679");
            new_base = new_base.replace("z", "\u267a");
        }
        return new_base;
    }

}
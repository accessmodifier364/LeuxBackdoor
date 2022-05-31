package me.accessmodifier364.leuxbackdoor.client.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    public final static Minecraft mc = Minecraft.getMinecraft();

    public static ChatFormatting b = ChatFormatting.BLUE;
    public static ChatFormatting c = ChatFormatting.RED;
    public static ChatFormatting r = ChatFormatting.RESET;
    public static ChatFormatting g = ChatFormatting.GRAY;
    public static ChatFormatting a = ChatFormatting.GREEN;

    public static void toggle_message(Module module) {

        if (module.is_active()) {
            client_message_simple(processedMessageWatermark() + module.get_tag() + " toggled " + a + "on" + g + ".");
        } else {
            client_message_simple(processedMessageWatermark() + module.get_tag() + " toggled " + c + "off" + g + ".");
        }
    }

    public static void client_message_simple(String message) {
        if (mc.player != null) {
            final ITextComponent itc = new TextComponentString(message).setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Client by ObsidianBreaker :3"))));
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(itc, 5936);
        }
    }

    public static void client_message(String message) {
        if (mc.player != null) {
            mc.player.sendMessage(new ChatMessage(message));
        }
    }

    public static void send_client_message_simple(String message) {
        client_message(processedMessageWatermark() + message);
    }

    public static void send_client_message(String message) {

        client_message(processedMessageWatermark() + message);
    }

    public static void send_client_error_message(String message) {

        client_message(processedMessageWatermark() + message);
    }

    private static String processedMessageWatermark() {
        String finalWatermark = RenameUtil.asciiToUtf8(RenameUtil.get_message_watermark());
        return finalWatermark + "\u00A77 ";
    }

    public static class ChatMessage extends TextComponentBase {
        String message_input;

        public ChatMessage(String message) {
            Pattern p = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher m = p.matcher(message);
            StringBuffer sb = new StringBuffer();

            while (m.find()) {
                String replacement = "\u00A7" + m.group().substring(1);
                m.appendReplacement(sb, replacement);
            }

            m.appendTail(sb);
            this.message_input = sb.toString();
        }

        public String getUnformattedComponentText() {
            return this.message_input;
        }

        @Override
        public ITextComponent createCopy() {
            return new ChatMessage(this.message_input);
        }
    }

}
package me.accessmodifier364.leuxbackdoor.client.util;

public class RenameUtil {

    private static String message;
    private static String messageWatermark;

    public static void set_client_name(String message) {
        if (message != null) {
            RenameUtil.message = message;
        } else {
            RenameUtil.message = "LeuxBackdoor";
        }
    }

    public static String get_client_name() {
        if (message != null) {
            return message;
        }
        return "LeuxBackdoor";
    }

    public static void set_message_watermark(String watermark) {
        if (watermark != null) {
            RenameUtil.messageWatermark = watermark;
        } else {
            RenameUtil.messageWatermark = "LeuxBackdoor";
        }
    }

    public static String get_message_watermark() {
        if (messageWatermark != null) {
            return messageWatermark;
        }
        return "\u00A79[LeuxBackdoor]";
    }

    public static String get_rainbow_watermark() {
        StringBuilder stringBuilder = new StringBuilder("[LeuxBackdoor]");
        stringBuilder.insert(0, "\u00a7+");
        stringBuilder.append("\u00a7r");
        return stringBuilder.toString();
    }

    public static String asciiToUtf8(String convert) {
        if (convert != null) {
            return convert.replace("&", "\u00A7");
        }
        return "\u00A79[LeuxBackdoor]";
    }

}

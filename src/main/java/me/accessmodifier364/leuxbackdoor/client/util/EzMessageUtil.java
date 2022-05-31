package me.accessmodifier364.leuxbackdoor.client.util;

public class EzMessageUtil {

    private static String message;

    public static void set_message(String message) {
        EzMessageUtil.message = message;
    }

    public static String get_message() {
        return message;
    }

}

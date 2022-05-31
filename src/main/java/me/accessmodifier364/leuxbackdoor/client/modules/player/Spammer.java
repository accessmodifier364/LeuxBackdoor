package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Spammer extends Module {
    public Spammer() {
        super(Category.player);
        this.name        = "Spammer";
        this.description = "xd";
    }

    Setting random = create("Random", "Random", true);
    Setting greentext = create("Greentext", "Greentext", true);
    Setting randomsuffix = create("Anti Spam", "AntiSpam", true);
    Setting delay = create("Send Delay", "SendDelay", 4, 1, 15);

    private static final Random rand = new Random();
    private static final String fileName = "LeuxBackdoor" + File.separator + "spammer.txt";
    private static final List<String> spamMessages = new ArrayList<>();
    private static final Random rnd = new Random();
    private static Timer timer;

    @Override
        public void enable() {
            this.readSpamFile();
            timer = new Timer();
            if (mc.player == null) {
                this.disable();
                return;
            }
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                Spammer.this.runCycle();
            }
        };
            timer.schedule(task, 0L, this.delay.get_value(1) * 1000L);
        }

        @Override
        public void disable() {
            timer.cancel();
            timer.purge();
            spamMessages.clear();
        }

    boolean readded = false;

        private void runCycle() {
            if (mc.player == null) {
                return;
            }
            if (!readded) {
                this.readSpamFile();
                readded = true;
            }
            if (spamMessages.size() > 0) {
                String messageOut;
                if (this.random.get_value(true)) {
                    int index = rnd.nextInt(spamMessages.size());
                    messageOut = spamMessages.get(index);
                    spamMessages.remove(index);
                } else {
                    messageOut = spamMessages.get(0);
                    spamMessages.remove(0);
                }
                spamMessages.add(messageOut);
                if (this.greentext.get_value(true)) {
                    messageOut = "> " + messageOut;
                }
                int reserved = 0;
                ArrayList<String> messageAppendix = new ArrayList<>();
                if (ModLoader.get_module_manager().get_module_with_tag("ChatSuffix").is_active()) {
                    reserved += " \u2744".length();
                }
                if (this.randomsuffix.get_value(true)) {
                    messageAppendix.add(generateRandomHexSuffix(2));
                }
                if (messageAppendix.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" ");
                    for (String msg : messageAppendix) {
                        sb.append(msg);
                    }
                    messageOut = cropMaxLengthMessage(messageOut, sb.toString().length() + reserved);
                    messageOut = messageOut + sb.toString();
                }
                mc.player.connection.sendPacket(new CPacketChatMessage(messageOut.replaceAll("\u00a7", "")));
            }
        }

    public static String cropMaxLengthMessage(String s, int i) {
        if (s.length() > 255 - i) {
            s = s.substring(0, 255 - i);
        }
        return s;
    }
        
    public static String generateRandomHexSuffix(int n) {
        return "[" +
                Integer.toHexString((rand.nextInt() + 11) * rand.nextInt()).substring(0, n) +
                ']';
    }

    private void readSpamFile() {
            if (Files.exists(Paths.get(fileName))) {
                List<String> fileInput = readTextFileAllLines(fileName);
                Iterator<String> i = fileInput.iterator();
                spamMessages.clear();
                while (i.hasNext()) {
                    String s = i.next();
                    if (s.replaceAll("\\s", "").isEmpty()) continue;
                    spamMessages.add(s);
                }
                if (spamMessages.size() == 0) {
                    MessageUtil.send_client_message("spammer.txt is empty");
                    set_disable();
                }
            } else {
                try {
                    createSpammerFile();
                } catch (Exception e) {
                    MessageUtil.send_client_message("Error creating spammer.txt");
                }
                MessageUtil.send_client_message("spammer.txt dont exist");
            }
    }

    private void createSpammerFile() throws IOException {
        FileWriter writer = new FileWriter(fileName);
        try {
            writer.write("Default spammer message");
        } catch (Exception ignored) {
        }
        writer.close();
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            appendTextFile("", file);
            return Collections.emptyList();
        }
    }

    public static void appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        }
        catch (IOException ignored) {
        }
    }
}

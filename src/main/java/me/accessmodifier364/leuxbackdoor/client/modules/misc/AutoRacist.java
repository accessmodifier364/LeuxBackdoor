package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AutoRacist extends Module {

    public AutoRacist() {
        super(Category.misc);

        this.name = "AutoRacist";
        this.description = "i love black squares (circles on the other hand...)";
    }

    Setting delay = create("Delay", "AutoRacistDelay", 6, 0, 100);
    Setting anti_nword = create("AntiNword", "AutoRacismAntiNword", false);
    Setting chanter = create("Chanter", "AutoRacismChanter", true);

    List<String> chants = new ArrayList<>();

    Random r = new Random();
    int tick_delay;

    @Override
    protected void enable() {
        tick_delay = 0;

        chants.add("<player> nigger");
        chants.add("Luscius is god");
        chants.add("#LEUXMODE");
        chants.add("<player>, i have ur stash coords");
        chants.add("justice 4 ObsidianBreaker");
        chants.add("WarriorCrystal loves spaghetti");
        chants.add("stop being gay and join Leux");
        chants.add("imagine not being from Leux");
        chants.add("<player> join Leux and stop being a nn");
        chants.add("<player>, ur password has leaked");
        chants.add(":rolf:");
        chants.add("<player>, Sazked wants sex with you");
    }

    String[] random_correction = {
            "Yuo jstu got nea nae'd by LuxBakdor",
            "LeuxBackdoor just stopped me from saying something racially incorrect!",
            "<Insert nword word here>",
            "Im an edgy teenager and saying the nword makes me feel empowered on the internet.",
            "My mom calls me a late bloomer",
            "I really do think im funny",
            "Niger is a great county, I do say so myself",
            "Mommy and daddy are wrestling in the bedroom again so im going to play minecraft until their done",
            "How do you open the impact GUI?",
            "What time does FitMC do his basehunting livestreams?"
    };


    CharSequence nigger = "nigger";
    CharSequence nigga = "nigga";

    @Override
    public void update() {

        if(chanter.get_value(true)) {

            tick_delay++;

            if (tick_delay < delay.get_value(1) * 10) return;

            String s = chants.get(r.nextInt(chants.size()));
            String name = get_random_name();

            if (name.equals(mc.player.getName())) return;

            mc.player.sendChatMessage(s.replace("<player>", name));
            tick_delay = 0;

            }
        }

    public String get_random_name() {

            List<EntityPlayer> players = mc.world.playerEntities;
            return players.get(r.nextInt(players.size())).getName();
        }


    public String random_string(String[] list) {
        return list[r.nextInt(list.length)];
    }

    // Anti n-word

    @EventHandler
    private Listener<EventPacket.SendPacket> listener = new Listener<>(event -> {

        if (!(event.get_packet() instanceof CPacketChatMessage)) {
            return;
        }

        if(anti_nword.get_value(true)) {

            String message = ((CPacketChatMessage) event.get_packet()).getMessage().toLowerCase();

            if (message.contains(nigger) || message.contains(nigga)) {

                String x = Integer.toString((int) (mc.player.posX));
                String z = Integer.toString((int) (mc.player.posZ));

                String coords = x + " " + z;

                message = (random_string(random_correction));
                mc.player.connection.sendPacket(new CPacketChatMessage("I am not nigger"));

            }

            ((CPacketChatMessage) event.get_packet()).message = message;
        }
    });


}

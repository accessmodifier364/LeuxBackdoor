package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.Timer;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.Random;


public class AntiAFK extends Module {//Rewrite by ObsidianBreaker
    public AntiAFK()
    {
        super(Category.player);
        this.name = "AntiAFK";
        this.description = "xd";
    }

    Setting delay = create("Delay", "Delay", 20, 20, 240);
    Setting announce = create("Announce", "Announce", true);
    Setting jump = create("Jump", "Jump", true);
    Setting swing = create("Hand", "Hand", true);
    Setting move = create("Move", "Move", true);
    Setting interact = create("Interact", "Interact", true);

    int afk_delay;
    static String lastcode;
    private final Timer timer = new Timer();

    @EventHandler
    private final Listener<ClientChatReceivedEvent> listener = new Listener<>(event ->{
        if(event.getMessage().getUnformattedText().contains(lastcode))
            event.setCanceled(true);
    });

    public void enable(){
        afk_delay = 0;
        EventClientBus.EVENT_BUS.subscribe(this);
    }

    @Override
    public void update() {
        afk_delay++;
        if (move.get_value(true)) {
            move();
        }
        if (afk_delay < delay.get_value(1) * 10) return;
        lastcode = getRandomHexString();
        if (announce.get_value(true)) mc.player.sendChatMessage("AntiAFK " + lastcode);
        if (jump.get_value(true)) mc.player.jump();
        if (swing.get_value(true)) mc.player.swingArm(EnumHand.MAIN_HAND);
        if (interact.get_value(true)) ;
        afk_delay = 0;
    }

    public void disable(){
        EventClientBus.EVENT_BUS.unsubscribe(this);
        mc.gameSettings.keyBindForward.pressed = false;
        mc.gameSettings.keyBindBack.pressed = false;
        mc.gameSettings.keyBindLeft.pressed = false;
        mc.gameSettings.keyBindRight.pressed = false;
    }

    private String getRandomHexString(){
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 8){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.substring(0, 8);
    }

    private void move() {
        if (timer.passed(3000) && timer.getTime(1L) < 6000) {
            mc.gameSettings.keyBindForward.pressed = true;
        }

        if (timer.passed(6000) && timer.getTime(1L) < 9000) {
            mc.gameSettings.keyBindForward.pressed = false;
            mc.gameSettings.keyBindBack.pressed = true;
        }

        if (timer.passed(9000) && timer.getTime(1L) < 12000) {
            mc.gameSettings.keyBindBack.pressed = false;
            mc.gameSettings.keyBindLeft.pressed = true;
        }

        if (timer.passed(12000) && timer.getTime(1L) < 15000) {
            mc.gameSettings.keyBindLeft.pressed = false;
            mc.gameSettings.keyBindRight.pressed = true;
        }

        if (timer.passed(15000)) {
            mc.gameSettings.keyBindRight.pressed = false;
            timer.reset();
        }

    }
}
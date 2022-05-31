package me.accessmodifier364.leuxbackdoor.client.event.events;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import net.minecraft.client.gui.GuiScreen;

public class EventGUIScreen extends EventCancellable {
    private GuiScreen guiscreen;

    public EventGUIScreen(GuiScreen screen) {
        this.guiscreen = screen;
    }

    public GuiScreen get_guiscreen() {
        return this.guiscreen;
    }

    public void set_screen(GuiScreen screen) {
        this.guiscreen = this.guiscreen;
    }

    public static class Closed
            extends EventGUIScreen {
        public Closed(GuiScreen screen) {
            super(screen);
        }
    }

    public static class Displayed
            extends EventGUIScreen {
        public Displayed(GuiScreen screen) {
            super(screen);
        }
    }
}

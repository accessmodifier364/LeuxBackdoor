package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class ClearChat extends Module {
    public ClearChat() {
        super(Category.render);
        this.name = "ClearChatbox";
        this.description = "Removes the default minecraft chat outline.";
    }
}
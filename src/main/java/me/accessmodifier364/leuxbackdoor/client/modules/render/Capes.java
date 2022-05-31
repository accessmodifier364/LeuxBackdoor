package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;

public class Capes extends Module {
    public Capes() {
        super(Category.render);
        this.name = "Capes";
        this.description = "see epic capes behind epic dudes";
    }
    Setting cape = create("Cape", "Cape", "Red", combobox("Dark", "Red", "Fem"));

    @Override
    public String array_detail() {
        return cape.get_current_value();
    }
}

package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class StorageESP extends Module {
    Setting shu = create("Shulkers", "StorageShulker", true);
    Setting shu_ = create("Shulker Color", "StorageESPShulker", "Client", combobox("Client", "RAINBOW"));
    Setting enc = create("EChests", "StorageEChests", true);
    Setting enc_ = create("EChest Color", "StorageESPEnchest", "Client", combobox("Client"));
    Setting che = create("Chests", "StorageChests", true);
    Setting che_ = create("Chest Color", "StorageESPChest", "Client", combobox("Client"));
    Setting oth = create("Misc", "StorageESPMisc", false);
    Setting oth_ = create("Misc Color", "StorageESPOthers", "Client", combobox("Client"));
    Setting ot_a = create("Outline A", "StorageESPOutlineA", 128, 0, 255);
    Setting a = create("Solid A", "StorageESPSolidA", 32, 0, 255);
    private int color_alpha;

    public StorageESP() {
        super(Category.render);
        this.name = "StorageESP";
        this.description = "Is able to see storages in world";
    }

    public void render(EventRender event) {

        this.color_alpha = this.a.get_value(1);
        for (TileEntity tiles : mc.world.loadedTileEntityList) {
            if (shu.get_value(true)) {
                if (tiles instanceof TileEntityShulkerBox) {
                    TileEntityShulkerBox shulker = (TileEntityShulkerBox) tiles;
                    int hex = 0xFF000000 | shulker.getColor().getColorValue();
                    if (this.shu_.in("RAINBOW")) {
                        draw(tiles, (hex & 0xFF0000) >> 16, (hex & 0xFF00) >> 8, hex & 0xFF);
                    } else {
                        draw(tiles, 204, 0, 0);
                    }

                }
            }

            if (enc.get_value(true)) {
                if (tiles instanceof net.minecraft.tileentity.TileEntityEnderChest)

                    draw(tiles, 204, 0, 255);

            }

            if (che.get_value(true)) {
                if (tiles instanceof net.minecraft.tileentity.TileEntityChest)

                    draw(tiles, 153, 102, 0);

            }

            if (oth.get_value(true)) {
                if (tiles instanceof net.minecraft.tileentity.TileEntityDispenser || tiles instanceof net.minecraft.tileentity.TileEntityHopper || tiles instanceof net.minecraft.tileentity.TileEntityFurnace || tiles instanceof net.minecraft.tileentity.TileEntityBrewingStand) {
                    draw(tiles, 190, 190, 190);
                }
            }

        }
    }

    public void draw(TileEntity tile_entity, int r, int g, int b) {
        RenderHelp.prepare("quads");
        RenderHelp.draw_cube(tile_entity.getPos(), r, g, b, this.a.get_value(1), "all");
        RenderHelp.release();
        RenderHelp.prepare("lines");
        RenderHelp.draw_cube_line(tile_entity.getPos(), r, g, b, this.ot_a.get_value(1), "all");
        RenderHelp.release();
    }
}

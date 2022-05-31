package me.accessmodifier364.leuxbackdoor.client.modules.render;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.CrystalUtil;
import me.accessmodifier364.leuxbackdoor.client.util.EntityUtil;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.turok.draw.RenderHelp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class FuckedDetector extends Module {
    public FuckedDetector() {
        super(Category.render);
        this.fucked_players = new HashSet<>();
        this.name = "FuckedDetector";
        this.description = "see if people are hecked";
    }

    Setting draw_own = create("Draw Own", "FuckedDrawOwn", false);
    Setting draw_friends = create("Draw Friends", "FuckedDrawFriends", false);
    Setting render_mode = create("Render Mode", "FuckedRenderMode", "Pretty", combobox("Pretty", "Solid", "Outline"));
    Setting r = create("R", "FuckedR", 255, 0, 255);
    Setting g = create("G", "FuckedG", 255, 0, 255);
    Setting b = create("B", "FuckedB", 255, 0, 255);
    Setting a = create("A", "FuckedA", 100, 0, 255);

    private boolean solid;
    private boolean outline;
    public Set<BlockPos> fucked_players;

    protected void enable() {
        this.fucked_players.clear();
    }

    public void update() {
        if (mc.world == null)
            return;
        set_fucked_players();
    }

    public void set_fucked_players() {
        this.fucked_players.clear();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (!EntityUtil.isLiving(player) || player.getHealth() <= 0.0F)
                continue;
            if (is_fucked(player)) {
                if ((FriendUtil.isFriend(player.getName()) && !this.draw_friends.get_value(true)) || (
                        player == mc.player && !this.draw_own.get_value(true)))
                    continue;
                this.fucked_players.add(new BlockPos(player.posX, player.posY, player.posZ));
            }
        }
    }

    public boolean is_fucked(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.posX, player.posY - 1.0D, player.posZ);
        if (CrystalUtil.canPlaceCrystal(pos.south()) || (CrystalUtil.canPlaceCrystal(pos.south().south()) && mc.world.getBlockState(pos.add(0, 1, 1)).getBlock() == Blocks.AIR))
            return true;
        if (CrystalUtil.canPlaceCrystal(pos.east()) || (CrystalUtil.canPlaceCrystal(pos.east().east()) && mc.world.getBlockState(pos.add(1, 1, 0)).getBlock() == Blocks.AIR))
            return true;
        if (CrystalUtil.canPlaceCrystal(pos.west()) || (CrystalUtil.canPlaceCrystal(pos.west().west()) && mc.world.getBlockState(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR))
            return true;
        return CrystalUtil.canPlaceCrystal(pos.north()) || (CrystalUtil.canPlaceCrystal(pos.north().north()) && mc.world.getBlockState(pos.add(0, 1, -1)).getBlock() == Blocks.AIR);
    }

    public void render(EventRender event) {
        if (this.render_mode.in("Pretty")) {
            this.outline = true;
            this.solid = true;
        }
        if (this.render_mode.in("Solid")) {
            this.outline = false;
            this.solid = true;
        }
        if (this.render_mode.in("Outline")) {
            this.outline = true;
            this.solid = false;
        }
        for (BlockPos render_block : this.fucked_players) {
            if (render_block == null)
                return;
            if (this.solid) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), render_block
                        .getX(), render_block.getY(), render_block.getZ(), 1.0F, 1.0F, 1.0F, this.r

                        .get_value(1), this.g.get_value(1), this.b.get_value(1), this.a.get_value(1), "all");
                RenderHelp.release();
            }
            if (this.outline) {
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), render_block
                        .getX(), render_block.getY(), render_block.getZ(), 1.0F, 1.0F, 1.0F, this.r

                        .get_value(1), this.g.get_value(1), this.b.get_value(1), this.a.get_value(1), "all");
                RenderHelp.release();
            }
        }
    }
}

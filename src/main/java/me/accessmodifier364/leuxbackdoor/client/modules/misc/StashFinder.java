package me.accessmodifier364.leuxbackdoor.client.modules.misc;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StashFinder extends Module {
    public StashFinder() {
        super(Category.misc);
        this.name = "StashFinder";
        this.description = "ez raid";
        chestPositions = new ArrayList<>();
        stashMap = new HashMap<>();
    }

    Setting chests = create("Chests", "StashFinderChests", 6, 2, 20);
    Setting file = create("Log to File", "StashFinderFile", true);
    Setting shulkers = create("Log Shulkers", "StashFinderShulkers", false);
    Setting sound = create("Play Sound", "StashFinderSound", true);

    private final ArrayList<BlockPos> chestPositions;
    private final Map<Long, Integer> stashMap;

    public void enable() {
        chestPositions.clear();
        stashMap.clear();
    }

    public void update() {
        for (TileEntity tileEntity : mc.world.loadedTileEntityList) {
            BlockPos pos = tileEntity.getPos();
            if (tileEntity instanceof net.minecraft.tileentity.TileEntityChest || tileEntity instanceof net.minecraft.tileentity.TileEntityShulkerBox) {
                boolean alreadyAdded = false;
                for (BlockPos p : chestPositions) {
                    if (p.equals(pos)) {
                        alreadyAdded = true;
                        break;
                    }
                }
                if (alreadyAdded)
                    continue;
                chestPositions.add(pos);
                int chunkX = pos.getX() / 16;
                int chunkZ = pos.getZ() / 16;
                long chunk = ChunkPos.asLong(chunkX, chunkZ);
                if (!stashMap.containsKey(chunk))
                    stashMap.put(chunk, 0);
                int DENSITY = chests.get_value(1);
                int count = stashMap.get(chunk) + 1;
                if (shulkers.get_value(true) && tileEntity instanceof TileEntityShulkerBox && count < DENSITY) {
                    count = DENSITY;
                }
                stashMap.put(chunk, count);
                if (count == DENSITY) {
                    if (tileEntity instanceof TileEntityShulkerBox) {
                        MessageUtil.send_client_message("1 or more Shulker Boxs found at " + pos.toString());
                    } else {
                        MessageUtil.send_client_message(count + " or more Chests found at " + pos.toString());
                    }
                    if (sound.get_value(true))
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F));
                    if (file.get_value(true))
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("LeuxBackdoor\\stashfinder.json", true));
                            String line = "";
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            line = line + "[" + formatter.format(calendar.getTime()) + "|";
                            if (mc.getCurrentServerData() != null)
                                line = line + (mc.getCurrentServerData()).serverIP + "|";
                            switch (mc.player.dimension) {
                                case 0:
                                    line = line + "Overworld";
                                    break;
                                case 1:
                                    line = line + "End";
                                    break;
                                case -1:
                                    line = line + "Nether";
                                    break;
                            }
                            line = line + "] ";
                            if (tileEntity instanceof net.minecraft.tileentity.TileEntityShulkerBox && shulkers.get_value(true)) {
                                line = line + "Shulker Box found at " + pos.toString();
                            } else if (chests.get_value(true)) {
                                line = line + count + " or more Chests found at " + pos.toString();
                            }
                            writer.write(line);
                            writer.newLine();
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }
}
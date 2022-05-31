package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.hud.Speedometer;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.Timer;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.*;

public class Surround extends Module {
    public static boolean isPlacing = false;
    private final me.accessmodifier364.leuxbackdoor.client.util.Timer timer = new me.accessmodifier364.leuxbackdoor.client.util.Timer();
    private final me.accessmodifier364.leuxbackdoor.client.util.Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<>();
    private final Map<BlockPos, Integer> retries = new HashMap<>();
    Setting delay = create("Delay/Place", "Alpha", 50, 0, 250);
    Setting blocksPerTick = create("Block/Place", "Block/Place", 8, 1, 20);
    Setting rotate = create("Rotate", "Rotate", false);
    Setting raytrace = create("Raytrace", "Raytrace", false);
    Setting switchMode = create("Switch", "Switch", "Normal", combobox("Normal", "None", "Silent"));
    Setting center = create("Center", "Center", false);
    Setting helpingBlocks = create("HelpingBlocks", "HelpingBlocks", true);
    Setting intelligent = create("Intelligent", "Intelligent", false);
    Setting antiPedo = create("NoPedo", "NoPedo", false);
    Setting extender = create("Extend", "Extend", 1, 1, 4);
    Setting extendMove = create("MoveExtend", "MoveExtend", false);
    Setting movementMode = create("Movement", "Movement", "Static", combobox("Static", "None", "Limit", "Off"));
    Setting speed = create("Speed", "Speed", 10.0, 0.0, 30.0);
    Setting floor = create("Floor", "Floor", false);
    Setting echests = create("Echests", "Echests", false);
    Setting noGhost = create("Packet", "Packet", false);
    Setting info = create("Info", "Info", false);
    Setting retryer = create("Retries", "Retries", 4, 1, 15);
    Setting endPortals = create("EndPortals", "EndPortals", false);
    Setting render = create("Render", "Render", true);
    Setting box = create("Box", "Box", false);
    Setting outline = create("Outline", "Outline", true);
    Setting red = create("Red", "Red", 255, 0, 255);
    Setting green = create("Green", "Green", 255, 0, 255);
    Setting blue = create("Blue", "Blue", 255, 0, 255);
    Setting alpha = create("Alpha", "Alpha", 180, 0, 255);
    Setting boxAlpha = create("BoxAlpha", "BoxAlpha", 125, 0, 255);
    Setting lineWidth = create("LineWidth", "LineWidth", 1.0f, 0.1f, 5.0f);
    Setting customOutline = create("CustomLine", "CustomLine", false);
    Setting cRed = create("OL-Red", "OLRed", 255, 0, 255);
    Setting cGreen = create("OL-Green", "OLGreen", 255, 0, 255);
    Setting cBlue = create("OL-Blue", "OLBlue", 255, 0, 255);
    Setting cAlpha = create("OL-Alpha", "OLAlpha", 255, 0, 255);
    int extra = 0;
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;
    private List<BlockPos> placeVectors = new ArrayList<>();
    public Surround() {
        super(Category.combat);
        this.name = "Surround";
        this.description = "surround urself with obi and such";
    }

    @Override
    public void enable() {
        if (mc.player == null || mc.world == null) {
            this.disable();
        }

        try {
            extra = 0;
            this.lastHotbarSlot = mc.player.inventory.currentItem;
            this.startPos = EntityUtil.getRoundedBlockPos(mc.player);
            if (this.center.get_value(true) && ModLoader.get_module_manager().get_module_with_tag("Freecam").is_disabled()) {
                if (mc.world.getBlockState(new BlockPos(mc.player.getPositionVector())).getBlock() == Blocks.WEB) {
                    setPositionPacket(mc.player.posX, this.startPos.getY(), mc.player.posZ, true, true);
                } else {
                    setPositionPacket((double) this.startPos.getX() + 0.5, this.startPos.getY(), (double) this.startPos.getZ() + 0.5, true, true);
                }
            }
            this.retries.clear();
            this.retryTimer.reset();
        } catch (Throwable ignored) {
        }
    }


    public void setPositionPacket(double x, double y, double z, boolean onGround, boolean setPos) {
        try {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, onGround));
            if (setPos) {
                mc.player.setPosition(x, y, z);
            }
        } catch (Throwable ignored) {
        }
    }


    @Override
    public void update() {
        try {
            this.doFeetPlace();
            if (this.isSafe == 2) {
                this.placeVectors = new ArrayList<>();
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void disable() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        try {
            extra = 0;
            isPlacing = false;
            this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
            this.switchItem(true);
        } catch (Throwable ignored) {
        }
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }

        extra = 0;

        if (mc.world.getBlockState(mc.player.getPosition()).getBlock().equals(Blocks.ENDER_CHEST) && mc.world.getBlockState(mc.player.getPosition().add(0, 0.2, 0)).getBlock().equals(Blocks.AIR)) {
            extra = 1;
        }

        if (!EntityUtil.isSafe(mc.player, 0, this.floor.get_value(true), true)) {
            this.isSafe = 0;
            this.placeBlocks(mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(mc.player, 0, this.floor.get_value(true), true), this.helpingBlocks.get_value(true), false, false);
        } else if (!EntityUtil.isSafe(mc.player, -1, false, true)) {
            this.isSafe = 1;
            if (this.antiPedo.get_value(true)) {
                this.placeBlocks(mc.player.getPositionVector(), EntityUtil.getUnsafeBlockArray(mc.player, -1, false, true), false, false, true);
            }
        } else {
            this.isSafe = 2;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < this.extender.get_value(1)) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            for (Vec3d extendingBlock : this.extendingBlocks) {
                array[i] = extendingBlock;
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, this.floor.get_value(true), true), this.helpingBlocks.get_value(true), false, true);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= this.extender.get_value(1)) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray(mc.player, 0, this.floor.get_value(true), true)) {
                if (!vec3d.equals(pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        int helpings = 0;
        boolean gotHelp;
        block6:
        for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            if (isHelping && !this.intelligent.get_value(true) && ++helpings > 1) {
                return false;
            }
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);

            if (position != null) {
                try {
                    switch (BlockUtil.isPositionPlaceable(position, this.raytrace.get_value(true))) {
                        case -1: {
                            continue block6;
                        }
                        case 1: {

                            if ((this.switchMode.in("Silent") || this.retries.get(position) < this.retryer.get_value(1))) {
                                this.placeBlock(position);
                                this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                                this.retryTimer.reset();
                                continue block6;
                            }
                            if (!this.extendMove.get_value(true) && Speedometer.get_speed() != 0.0 || isExtending || this.extenders >= this.extender.get_value(1))
                                continue block6;
                            this.placeBlocks(mc.player.getPositionVector().add(vec3d), EntityUtil.getUnsafeBlockArrayFromVec3d(mc.player.getPositionVector().add(vec3d), 0, this.floor.get_value(true), true), hasHelpingBlocks, false, true);
                            this.extendingBlocks.add(vec3d);
                            ++this.extenders;
                            continue block6;
                        }
                        case 2: {
                            if (!hasHelpingBlocks) continue block6;
                            gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                        }
                        case 3: {
                            if (gotHelp) {
                                this.placeBlock(position);
                            }
                            if (!isHelping) continue block6;
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (mc.player == null || mc.world == null) {
            return true;
        }
        if (this.endPortals.get_value(true)) {
            this.offHand = InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockEndPortalFrame.class);
            if (!this.offHand) {
                this.offHand = InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
            }
        } else {
            this.offHand = InventoryUtil.isBlock(mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        }
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        if (this.endPortals.get_value(true)) {
            this.obbySlot = InventoryUtil.findHotbarBlockz(BlockEndPortalFrame.class);
            if (this.obbySlot == -1) {
                this.obbySlot = InventoryUtil.findHotbarBlockz(BlockObsidian.class);
            }
        } else {
            this.obbySlot = InventoryUtil.findHotbarBlockz(BlockObsidian.class);
        }
        int echestSlot = InventoryUtil.findHotbarBlockz(BlockEnderChest.class);
        if (this.is_disabled()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        this.switchItem(true);
        if (!(this.obbySlot != -1 || this.offHand || this.echests.get_value(true) && echestSlot != -1)) {
            if (this.info.get_value(true)) {
                MessageUtil.send_client_message("\u00a7c" + "You are out of Obsidian.");
            }
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (mc.player.inventory.currentItem != this.lastHotbarSlot && mc.player.inventory.currentItem != this.obbySlot && mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = mc.player.inventory.currentItem;
        }
        switch (this.movementMode.get_current_value()) {
            case "None": {
                break;
            }
            case "Static": {
                try {
                    if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(mc.player))) {
                        this.disable();
                        return true;
                    }
                } catch (Exception ignored) {

                }
            }
            case "Limit": {
                if (!(Speedometer.get_speed() > this.speed.get_value(1.0))) break;
                return true;
            }
            case "Off": {
                if (!(Speedometer.get_speed() > this.speed.get_value(1.0))) break;
                this.disable();
                return true;
            }
        }
        return ModLoader.get_module_manager().get_module_with_tag("Freecam").is_active() || !this.timer.passedMs(this.delay.get_value(1)) || this.switchMode.in("None") && mc.player.inventory.currentItem != InventoryUtil.findHotbarBlockz(BlockObsidian.class);
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerTick.get_value(1) && this.switchItem(false)) {
            isPlacing = true;
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.get_value(true), this.noGhost.get_value(true), this.isSneaking);
            this.didPlace = true;
            ++this.placements;
        }
    }

    private boolean switchItem(boolean back) {
        if (this.offHand) {
            return true;
        }
        boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.get_current_value(), this.obbySlot == -1 ? BlockEnderChest.class : (this.endPortals.get_value(true) && InventoryUtil.findHotbarBlockz(BlockEndPortalFrame.class) != -1 ? BlockEndPortalFrame.class : BlockObsidian.class));
        this.switchedItem = value[0];
        return value[1];
    }

    private List<BlockPos> fuckYou3arthqu4keYourCodeIsGarbage() {
        if (this.floor.get_value(true)) {
            return Arrays.asList(new BlockPos(mc.player.getPositionVector()).add(0, -1 + extra, 0), new BlockPos(mc.player.getPositionVector()).add(1, extra, 0), new BlockPos(mc.player.getPositionVector()).add(-1, extra, 0), new BlockPos(mc.player.getPositionVector()).add(0, extra, -1), new BlockPos(mc.player.getPositionVector()).add(0, extra, 1));
        }
        return Arrays.asList(new BlockPos(mc.player.getPositionVector()).add(1, extra, 0), new BlockPos(mc.player.getPositionVector()).add(-1, extra, 0), new BlockPos(mc.player.getPositionVector()).add(0, extra, -1), new BlockPos(mc.player.getPositionVector()).add(0, extra, 1));
    }

    @Override
    public void render(EventRender event) {
        if (this.render.get_value(true) && (this.isSafe == 0 || this.isSafe == 1)) {
            try {
                this.placeVectors = this.fuckYou3arthqu4keYourCodeIsGarbage();
                for (BlockPos pos : this.placeVectors) {
                    if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;
                    RenderUtil.drawBoxESP(pos, new Color(this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), this.alpha.get_value(1)), this.customOutline.get_value(true), new Color(this.cRed.get_value(1), this.cGreen.get_value(1), this.cBlue.get_value(1), this.cAlpha.get_value(1)), (float) this.lineWidth.get_value(1.0), this.outline.get_value(true), this.box.get_value(true), this.boxAlpha.get_value(1), false);
                }
            } catch (Throwable ignored) {
            }
        }
    }

}
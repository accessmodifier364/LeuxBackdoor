package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPlayerTravel;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.Timer;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.*;

public class AutoTrap extends Module {
    public static boolean isPlacing = false;
    private final me.accessmodifier364.leuxbackdoor.client.util.Timer timer = new me.accessmodifier364.leuxbackdoor.client.util.Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<>();
    private final me.accessmodifier364.leuxbackdoor.client.util.Timer retryTimer = new Timer();
    private final Map<BlockPos, IBlockState> toAir = new HashMap<>();
    public EntityPlayer target;
    Setting delay = create("Delay/Place", "Delay/Place", 3, 0, 250);
    Setting blocksPerPlace = create("Block/Place", "Block/Place", 8, 1, 30);
    Setting targetRange = create("TargetRange", "TargetRange", 7.5, 0.0, 20.0);
    Setting range = create("PlaceRange", "PlaceRange", 5.9, 0.0, 10.0);
    Setting targetMode = create("Target", "Target", "Closest", combobox("Closest", "Untrapped"));
    Setting toggle = create("Toggle", "Toggle", false);
    Setting rotate = create("Rotate", "Rotate", true);
    Setting raytrace = create("Raytrace", "Raytrace", false);
    Setting switchMode = create("Switch", "Switch", "Normal", combobox("Normal", "Silent", "None"));
    Setting pattern = create("Pattern", "Pattern", "Static", combobox("Static", "Smart", "Open"));
    Setting extend = create("Extend", "Extend", 4, 1, 4);
    Setting antiScaffold = create("AntiScaffold", "AntiScaffold", true);
    Setting antiStep = create("AntiStep", "AntiStep", false);
    Setting face = create("Face", "Face", true);
    Setting legs = create("Legs", "Legs", false);
    Setting platform = create("Platform", "Platform", false);
    Setting antiDrop = create("AntiDrop", "AntiDrop", false);
    Setting antiSelf = create("AntiSelf", "AntiSelf", false);
    Setting eventMode = create("Updates", "Updates", 3, 1, 3);
    Setting freecam = create("Freecam", "Freecam", false);
    Setting info = create("Info", "Info", false);
    Setting entityCheck = create("NoBlock", "NoBlock", true);
    Setting noScaffoldExtend = create("NoScaffoldExtend", "NoScaffoldExtend", false);
    Setting disable = create("TSelfMove", "TSelfMove", false);
    Setting packet = create("Packet", "Packet", false);
    Setting airPacket = create("AirPacket", "AirPacket", false);
    Setting retryer = create("Retries", "Retries", 4, 1, 15);
    Setting endPortals = create("EndPortals", "EndPortals", false);
    Setting render = create("Render", "Render", true);
    Setting box = create("Box", "Box", false);
    Setting outline = create("Outline", "Outline", true);
    Setting red = create("Red", "TrapRed", 0, 0, 255);
    Setting green = create("Green", "TrapGreen", 100, 0, 255);
    Setting blue = create("Blue", "TrapBlue", 100, 0, 255);
    Setting alpha = create("Alpha", "TrapAlpha", 30, 0, 255);
    Setting boxAlpha = create("BoxAlpha", "BoxAlpha", 30, 0, 255);
    Setting lineWidth = create("LineWidth", "LineWidth", 1.0f, 0.1f, 5.0f);
    Setting customOutline = create("CustomLine", "CustomLine", false);
    Setting cRed = create("OL-Red", "OL-Red", 255, 0, 255);
    Setting cGreen = create("OL-Green", "OL-Green", 255, 0, 255);
    Setting cBlue = create("OL-Blue", "OL-Blue", 255, 0, 255);
    Setting cAlpha = create("OL-Alpha", "OL-Alpha", 255, 0, 255);
    private boolean didPlace = false;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    private int timeout_ticker = 0;
    private boolean smartRotate = false;
    private BlockPos startPos = null;
    private List<Vec3d> currentPlaceList = new ArrayList<>();
    @EventHandler
    private final Listener<EventPlayerTravel> onTravel = new Listener<>(event -> {
        if (event.get_era() == EventCancellable.Era.EVENT_PRE && this.eventMode.get_value(1) == 2) {
            this.smartRotate = this.rotate.get_value(true) && this.blocksPerPlace.get_value(1) == 1;
            this.doTrap();
        }
    });

    public AutoTrap() {
        super(Category.combat);
        this.name = "AutoTrap";
        this.description = "poo";
    }

    @Override
    public void enable() {
        if (mc.player == null || mc.world == null) return;
        timeout_ticker = 0;
        this.toAir.clear();
        this.startPos = EntityUtil.getRoundedBlockPos(mc.player);
        this.lastHotbarSlot = mc.player.inventory.currentItem;
        this.retries.clear();
    }

    @Override
    public void update() {
        if (timeout_ticker > 25 && !toggle.get_value(true)) {
            timeout_ticker = 0;
            set_disable();
            return;
        } else {
            if (this.eventMode.get_value(1) == 3) {
                this.smartRotate = false;
                this.doTrap();
            }

            if (this.eventMode.get_value(1) == 1) {
                this.smartRotate = false;
                this.doTrap();
            }
        }
        ++timeout_ticker;
    }

    @Override
    public void disable() {
        if (mc.player == null || mc.world == null) return;
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.switchItem(true);
    }

    @Override
    public void render(EventRender event) {
        if (this.render.get_value(true) && this.currentPlaceList != null) {
            for (Vec3d vec : this.currentPlaceList) {
                BlockPos pos = new BlockPos(vec);
                if (!(mc.world.getBlockState(pos).getBlock() instanceof BlockAir)) continue;

                RenderUtil.drawBoxESP(pos, new Color(this.red.get_value(1), this.green.get_value(1), this.blue.get_value(1), this.alpha.get_value(1)), this.customOutline.get_value(true), new Color(this.cRed.get_value(1), this.cGreen.get_value(1), this.cBlue.get_value(1), this.cAlpha.get_value(1)), this.lineWidth.get_value(1), this.outline.get_value(true), this.box.get_value(true), this.boxAlpha.get_value(1), false);

            }
        }
    }

    private void doTrap() {
        if (this.check()) {
            return;
        }

        if (pattern.in("Static")) {
            this.doStaticTrap();
        } else if (pattern.in("Open")) {
            this.doSmartTrap();
        }

        if (this.packet.get_value(true) && this.airPacket.get_value(true)) {
            for (Map.Entry<BlockPos, IBlockState> entry : this.toAir.entrySet()) {
                mc.world.setBlockState(entry.getKey(), entry.getValue());
            }
            this.toAir.clear();
        }
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void doSmartTrap() {
        List<Vec3d> placeTargets = EntityUtil.getUntrappedBlocksExtended(this.extend.get_value(1), this.target, this.antiScaffold.get_value(true), this.antiStep.get_value(true), this.legs.get_value(true), this.platform.get_value(true), this.antiDrop.get_value(true), this.raytrace.get_value(true), this.noScaffoldExtend.get_value(true), this.face.get_value(true));
        this.placeList(placeTargets);
        this.currentPlaceList = placeTargets;
    }

    private void doStaticTrap() {
        List<Vec3d> placeTargets = EntityUtil.targets(this.target.getPositionVector(), this.antiScaffold.get_value(true), this.antiStep.get_value(true), this.legs.get_value(true), this.platform.get_value(true), this.antiDrop.get_value(true), this.raytrace.get_value(true), this.face.get_value(true));
        this.placeList(placeTargets);
        this.currentPlaceList = placeTargets;
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.get_value(true));
            if (this.entityCheck.get_value(true) && placeability == 1 && (this.switchMode.in("Silent") || (this.retries.get(position) == null || this.retries.get(position) < this.retryer.get_value(1)))) {
                this.placeBlock(position);
                this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                this.retryTimer.reset();
                continue;
            }
            if (placeability != 3 || this.antiSelf.get_value(true) && BlockInteractHelper.areVec3dsAligned(mc.player.getPositionVector(), vec3d3))
                continue;
            this.placeBlock(position);
        }
    }

    private boolean check() {
        isPlacing = false;
        this.didPlace = false;
        this.placements = 0;
        int obbySlot;
        if (this.endPortals.get_value(true)) {
            obbySlot = InventoryUtil.findHotbarBlockz(BlockEndPortalFrame.class);
            if (obbySlot == -1) {
                obbySlot = InventoryUtil.findHotbarBlockz(BlockObsidian.class);
            }
        } else {
            obbySlot = InventoryUtil.findHotbarBlockz(BlockObsidian.class);
        }
        if (this.is_disabled()) {
            return true;
        }
        if (this.disable.get_value(true) && this.startPos != null && !this.startPos.equals(EntityUtil.getRoundedBlockPos(mc.player))) {
            this.disable();
            return true;
        }
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1) {
            if (!this.switchMode.in("None")) {
                if (this.info.get_value(true)) {
                    MessageUtil.send_client_message("You are out of Obsidian.");
                }
                this.disable();
            }
            return true;
        }
        if (mc.player.inventory.currentItem != this.lastHotbarSlot && mc.player.inventory.currentItem != obbySlot) {
            this.lastHotbarSlot = mc.player.inventory.currentItem;
        }
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.target = this.getTarget(this.targetRange.get_value(1f), this.targetMode.in("Untrapped"));
        return this.target == null || ModLoader.get_module_manager().get_module_with_tag("Freecam").is_active() && this.freecam.get_value(true) || !this.timer.passedMs(this.delay.get_value(1)) || this.switchMode.in("None") && mc.player.inventory.currentItem != InventoryUtil.findHotbarBlockz(BlockObsidian.class);
    }

    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range) || this.pattern.in("Static") && trapped && EntityUtil.isTrapped(player, this.antiScaffold.get_value(true), this.antiStep.get_value(true), this.legs.get_value(true), this.platform.get_value(true), this.antiDrop.get_value(true), this.face.get_value(true)) || this.pattern.in("Static") && trapped && EntityUtil.isTrappedExtended(this.extend.get_value(1), player, this.antiScaffold.get_value(true), this.antiStep.get_value(true), this.legs.get_value(true), this.platform.get_value(true), this.antiDrop.get_value(true), this.raytrace.get_value(true), this.noScaffoldExtend.get_value(true), this.face.get_value(true)) || EntityUtil.getRoundedBlockPos(mc.player).equals(EntityUtil.getRoundedBlockPos(player)) && this.antiSelf.get_value(true))
                continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (!(mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerPlace.get_value(1) && mc.player.getDistanceSq(pos) <= MathUtil.square(this.range.get_value(1f)) && this.switchItem(false)) {
            isPlacing = true;
            if (this.airPacket.get_value(true) && this.packet.get_value(true)) {
                this.toAir.put(pos, mc.world.getBlockState(pos));
            }
            this.isSneaking = this.smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, this.airPacket.get_value(true) && this.packet.get_value(true), this.isSneaking) : BlockUtil.PplaceBlock(pos, EnumHand.MAIN_HAND, this.rotate.get_value(true), this.airPacket.get_value(true) && this.packet.get_value(true), this.isSneaking);
            this.didPlace = true;
            ++this.placements;
        }
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.get_current_value(), this.endPortals.get_value(true) && InventoryUtil.findHotbarBlockz(BlockEndPortalFrame.class) != -1 ? BlockEndPortalFrame.class : BlockObsidian.class);
        this.switchedItem = value[0];
        return value[1];
    }
}

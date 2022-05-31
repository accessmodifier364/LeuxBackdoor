package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.EventCancellable;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventMotionUpdate;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventRender;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.modules.misc.AutoGG;
import me.accessmodifier364.leuxbackdoor.client.util.Timer;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.IntStream;

public class AutoCrystal extends Module {
    public AutoCrystal() {
        super(Category.combat);
        this.name = "AutoCrystal";
        this.description = "kills people (if ur good)";
        INSTANCE = this;
    }

    public static AutoCrystal INSTANCE;

    //Ranges
    Setting placeDelay = create("Place Delay", "PlaceDelay", 1, 0, 10);
    Setting placeRange = create("Place Range", "PlaceRange", 5.1, 0.0, 6.0);
    Setting placeRangeWall = create("Place Range Wall", "PlaceRangeWall", 3.5, 0.0, 6.0);
    Setting minPlace = create("MinPlace", "MinPlace", 10, 0, 36);
    Setting maxSelfPlace = create("MaxSelfPlace", "MaxSelfPlace", 6, 0, 36);
    Setting breakDelay = create("Break Delay", "BreakDelay", 1, 0, 10);
    Setting breakRange = create("Break Range", "BreakRange", 5.1, 0.0, 6.0);
    Setting breakRangeWall = create("Break Range Wall", "BreakRangeWall", 3.5, 0.0, 6.0);
    Setting minBreak = create("MinBreak", "MinBreak", 6, 0, 36);
    Setting maxSelfBreak = create("MaxSelfBreak", "MaxSelfBreak", 6, 0, 36);
    Setting targetRange = create("Target Range", "TargetRange", 12.0, 0.0, 20.0);
    Setting ignoreSelfDamage = create("Ignore Self", "IgnoreSelf", false);
    Setting antiSuicide = create("Anti Suicide", "AntiSuicide", true);
    Setting rotateMode = create("Rotate", "Rotate", "Off", combobox("Off", "Break", "Place", "Both"));
    Setting raytrace = create("Raytrace", "Raytrace", false);
    Setting fastMode = create("Fast", "Fast", "Ignore", combobox("Off", "Ignore", "Ghost", "Sound"));
    Setting autoSwitch = create("Switch", "Switch", "None", combobox("Allways", "NoGap", "None", "Silent"));
    Setting silentSwitchHand = create("Hand Activation", "HandActivation", true);
    Setting antiWeakness = create("Anti Weakness", "AntiWeakness", true);
    Setting maxCrystals = create("MaxCrystal", "MaxCrystal", 1, 1, 4);
    Setting ignoreTerrain = create("Terrain Trace", "TerrainTrace", true);
    Setting crystalLogic = create("Placements", "Placements", "Damage", combobox("Damage", "Nearby", "Safe"));
    Setting thirteen = create("1.13", "1.13", false);
    Setting attackPacket = create("Attack Packet", "AttackPacket", true);
    Setting packetSafe = create("Packet Safe", "PacketSafe", true);
    Setting arrayListMode = create("ArrayListMode", "ArrayListMode", "Latency", combobox("Latency", "Player", "CPS"));

    //Misc
    Setting threaded = create("Threaded", "Threaded", false);
    Setting antiStuck = create("Anti Stuck", "AntiStuck", false);
    Setting maxAntiStuckDamage = create("Stuck Self Damage", "StuckSelfDamage", 8, 0, 36);

    //Predicts
    Setting predictCrystal = create("Predict Crystal", "PredictCrystal", true);
    Setting predictBlock = create("Predict Block", "PredictBlock", false);
    Setting predictTeleport = create("PTeleport", "PTeleport", "Sound", combobox("Sound", "Packet", "None"));
    Setting entityPredict = create("Entity Predict", "EntityPredict", true);
    Setting predictedTicks = create("Predict Ticks", "PredictTicks", 2, 0, 5);

    //PlaceObsidian
    Setting palceObiFeet = create("PlaceObsidian", "PlaceObsidian", false);
    Setting ObiYCheck = create("YCheck", "YCheck", false);
    Setting rotateObiFeet = create("RotatePlace", "RotatePlace", false);
    Setting timeoutTicksObiFeet = create("Timeout", "Timeout", 3, 0, 5);

    //Faceplace
    Setting noMP = create("NoMultiPlace", "NoMultiPlace", true);
    Setting facePlaceHP = create("FaceplaceHP", "FaceplaceHP", 8, 0, 36);
    Setting facePlaceDelay = create("FaceplaceDelay", "FaceplaceDelay", 1, 0, 10);
    Setting fuckArmourHP = create("Armour%", "Armour%", 15, 0, 100);

    //render
    Setting when = create("When", "When", "Place", combobox("Place", "Break", "Both", "Never"));
    Setting mode = create("Mode", "Mode", "Pretty", combobox("Pretty", "Solid", "Outline"));

    Setting fade = create("Fade", "Fade", true);
    Setting fadeTime = create("FadeTime", "FadeTime", 200, 0, 1000);
    Setting flat = create("Flat", "Flat", false);

    Setting height = create("FlatHeight", "FlatHeight", -0.2, -2.0, 2.0);
    Setting width = create("Width", "Width", 1, 1, 10);

    Setting rainbow = create("Rainbow", "Rainbow", true);
    Setting saturation = create("Saturation", "Saturation", 50, 0, 100);
    Setting brightness = create("Brightness", "Brightness", 100, 0, 100);
    Setting speed = create("Speed", "Speed", 40, 1, 100);

    Setting red = create("Red", "Red", 100, 0, 255);
    Setting green = create("Green", "Green", 100, 0, 255);
    Setting blue = create("Blue", "Blue", 100, 0, 255);
    Setting alpha = create("Alpha", "Alpha", 100, 0, 255);

    Setting red2 = create("Red2", "Red2", 100, 0, 255);
    Setting green2 = create("Green2", "Green2", 100, 0, 255);
    Setting blue2 = create("Blue2", "Blue2", 100, 0, 255);
    Setting alpha2 = create("Alpha2", "Alpha2", 100, 0, 255);

    Setting renderDamage = create("RenderDamage", "RenderDamage", true);
    Setting swing = create("Swing", "Swing", "Offhand", combobox("Mainhand", "Offhand", "None"));
    Setting placeSwing = create("Place Swing", "PlaceSwing", true);

    private final List<EntityEnderCrystal> attemptedCrystals = new ArrayList<>();
    private final ArrayList<RenderPos> renderMap = new ArrayList<>();
    private final ArrayList<BlockPos> currentTargets = new ArrayList<>();
    private final me.accessmodifier364.leuxbackdoor.client.util.Timer crystalsPlacedTimer = new Timer();

    private EntityEnderCrystal stuckCrystal;
    private static EntityPlayer ca_target = null;

    private boolean alreadyAttacking;
    private boolean placeTimeoutFlag;
    private boolean hasPacketBroke;
    private boolean didAnything;
    private boolean facePlacing;

    private long start;
    private long crystalLatency;

    private int placeTimeout;
    private int breakTimeout;
    private int breakDelayCounter;
    private int placeDelayCounter;
    private int facePlaceDelayCounter;
    private int obiFeetCounter;
    private int crystalsPlaced;

    public EntityPlayer ezTarget;
    public ArrayList<BlockPos> staticPos;
    public EntityEnderCrystal staticEnderCrystal;

    @Override
    public void render(EventRender event) {
        if (fullNullCheck()) return;
        if (renderMap.isEmpty()) return;
        boolean outline = false;
        boolean solid = false;
        switch (mode.get_current_value()) {
            case "Pretty":
                outline = true;
                solid = true;
                break;
            case "Solid":
                outline = false;
                solid = true;
                break;
            case "Outline":
                outline = true;
                solid = false;
                break;
        }
        List<RenderPos> toRemove = new ArrayList<>();
        for (RenderPos renderPos : renderMap) {
            int fillAlpha = alpha.get_value(1);
            int boxAlpha = alpha2.get_value(1);
            if (currentTargets.contains(renderPos.pos)) {
                renderPos.fadeTimer = 0;
            } else if (!fade.get_value(true)) {
                toRemove.add(renderPos);
            } else {
                renderPos.fadeTimer++;
                fillAlpha = (int) (fillAlpha - (fillAlpha * (renderPos.fadeTimer / fadeTime.get_value(1))));
                boxAlpha = (int) (boxAlpha - (boxAlpha * (renderPos.fadeTimer / fadeTime.get_value(1))));
            }
            if (renderPos.fadeTimer > fadeTime.get_value(1))
                toRemove.add(renderPos);
            if (toRemove.contains(renderPos)) continue;
            RenderUtil3.drawBoxESP((flat.get_value(true)) ? new BlockPos(renderPos.pos.getX(), renderPos.pos.getY() + 1, renderPos.pos.getZ()) : renderPos.pos, new Colour(red.get_value(1), green.get_value(1), blue.get_value(1), Math.max(fillAlpha, 0)), new Colour(red2.get_value(1), green2.get_value(1), blue2.get_value(1), Math.max(boxAlpha, 0)), width.get_value(1), outline, solid, true, (flat.get_value(true)) ? height.get_value(1.0) : 0f, false, false, false, false, 0);
            if (renderDamage.get_value(true))
                RenderUtil3.drawText(renderPos.pos, String.valueOf(MathUtil.roundAvoid(renderPos.damage, 1)), true);
        }
        renderMap.removeAll(toRemove);
    }

    @Override
    public void enable() {
        if (fullNullCheck()) return;
        placeTimeout = this.placeDelay.get_value(1);
        breakTimeout = this.breakDelay.get_value(1);
        placeTimeoutFlag = false;
        ezTarget = null;
        facePlacing = false;
        attemptedCrystals.clear();
        hasPacketBroke = false;
        alreadyAttacking = false;
        obiFeetCounter = 0;
        crystalLatency = 0;
        start = 0;
        staticEnderCrystal = null;
        staticPos = null;
        crystalsPlaced = 0;
        crystalsPlacedTimer.reset();
    }

    public float getCPS() {
        return crystalsPlaced / (crystalsPlacedTimer.getPassedTimeMs() / 1000f);
    }

    @Override
    public String array_detail() {
        switch (arrayListMode.get_current_value()) {
            case "Latency":
                return crystalLatency + "ms";
            case "CPS":
                return "" + MathUtil.round(getCPS(), 2);
            case "Player":
                return this.ezTarget != null ? this.ezTarget.getName() : null;
            default:
                return "";
        }
    }

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        CPacketUseEntity packet;
        if (event.get_era() == EventCancellable.Era.EVENT_PRE
                && event.get_packet() instanceof CPacketUseEntity
                && (packet = (CPacketUseEntity) event.get_packet()).getAction() == CPacketUseEntity.Action.ATTACK
                && packet.getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
            if (this.fastMode.in("Ghost")) {
                Objects.requireNonNull(packet.getEntityFromWorld(mc.world)).setDead();
                mc.world.removeEntityFromWorld(packet.entityId);
            }
        }
    });


    @EventHandler
    private final Listener<EventMotionUpdate> on_movement = new Listener<>(event -> {
        if (event.stage == 0 && !this.rotateMode.in("Off")) {
            this.doCrystalAura();
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {

        // crystal predict check
        SPacketSpawnObject spawnObjectPacket;
        if (event.get_packet() instanceof SPacketSpawnObject
                && (spawnObjectPacket = (SPacketSpawnObject) event.get_packet()).getType() == 51
                && this.predictCrystal.get_value(true)) {
            // for each player on the server
            for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
                // if the crystal is valid for the given player
                if (this.isCrystalGood(new EntityEnderCrystal(mc.world, spawnObjectPacket.getX(), spawnObjectPacket.getY(), spawnObjectPacket.getZ()), target) != 0) {
                    // set up the break packet
                    CPacketUseEntity predict = new CPacketUseEntity();
                    predict.entityId = spawnObjectPacket.getEntityID();
                    predict.action = CPacketUseEntity.Action.ATTACK;
                    mc.player.connection.sendPacket(predict);
                    // swing arm
                    if (!swing.in("None")) {
                        BlockUtil.swingArm(swing);
                    }
                    // sets up the packet safe
                    if (packetSafe.get_value(true)) {
                        hasPacketBroke = true;
                        didAnything = true;
                    }
                    // only do it once
                    break;
                }
            }
        }

        // sets the 'player pos' of a teleporting player to where they're going to tp to
        if (event.get_packet() instanceof SPacketEntityTeleport) {
            SPacketEntityTeleport tpPacket = (SPacketEntityTeleport) event.get_packet();
            Entity e = mc.world.getEntityByID(tpPacket.getEntityId());
            if (e == mc.player) return;
            if (e instanceof EntityPlayer && predictTeleport.in("Packet")) {
                e.setEntityBoundingBox(e.getEntityBoundingBox().offset(tpPacket.getX(), tpPacket.getY(), tpPacket.getZ()));
            }
        }

        // same as above but works on the sound effect rather than the tp packet
        if (event.get_packet() instanceof SPacketSoundEffect) {
            SPacketSoundEffect soundPacket = (SPacketSoundEffect) event.get_packet();
            if (soundPacket.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT && predictTeleport.in("Sound")) {
                mc.world.loadedEntityList.spliterator().forEachRemaining(player -> {
                    if (player instanceof EntityPlayer && player != mc.player) {
                        if (player.getDistance(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) <= targetRange.get_value(1.0)) {
                            player.setEntityBoundingBox(player.getEntityBoundingBox().offset(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()));
                        }
                    }
                });
            }
            // unsure how this would ever lead to a crash but i dont wanna touch it atm
            try {
                if (soundPacket.getCategory() == SoundCategory.BLOCKS
                        && soundPacket.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity crystal : new ArrayList<>(mc.world.loadedEntityList)) {
                        if (crystal instanceof EntityEnderCrystal)
                            if (crystal.getDistance(soundPacket.getX(), soundPacket.getY(), soundPacket.getZ()) <= breakRange.get_value(1.0)) {
                                crystalLatency = System.currentTimeMillis() - start;
                                if (fastMode.in("Sound")) {
                                    crystal.setDead();
                                }
                            }
                    }
                }
            } catch (NullPointerException ignored) {
            }
        }

        // attempt at a place predict, currently doesn't place
        if (event.get_packet() instanceof SPacketExplosion) {
            SPacketExplosion explosionPacket = (SPacketExplosion) event.get_packet();
            BlockPos pos = new BlockPos(Math.floor(explosionPacket.getX()), Math.floor(explosionPacket.getY()), Math.floor(explosionPacket.getZ())).down();
            if (this.predictBlock.get_value(true)) {
                for (EntityPlayer player : new ArrayList<>(mc.world.playerEntities)) {
                    if (this.isBlockGood(pos, player) > 0) {
                        BlockUtil.placeCrystalOnBlock3(pos, EnumHand.MAIN_HAND, true);
                    }
                }
            }
        }
    });

    @Override
    public void update() {
        if (!fullNullCheck()) {

            if (rainbow.get_value(true)) {
                Color rainbowColor = new Color(RenderUtil.getRainbow(speed.get_value(1) * 100, 0, (float) saturation.get_value(1) / 100.0f, (float) brightness.get_value(1) / 100.0f));
                red.set_value(rainbowColor.getRed());
                green.set_value(rainbowColor.getGreen());
                blue.set_value(rainbowColor.getBlue());

                red2.set_value(rainbowColor.getRed());
                green2.set_value(rainbowColor.getGreen());
                blue2.set_value(rainbowColor.getBlue());
            }

            if (this.rotateMode.in("Off")) {
                this.doCrystalAura();
            }

            if (mc.player.isDead || mc.player.getHealth() <= 0) ca_target = null;
        }
    }

    public void doCrystalAura() {
        if (nullCheck()) {
            this.disable();
            return;
        }
        didAnything = false;

        if (placeDelayCounter > placeTimeout && (facePlaceDelayCounter >= facePlaceDelay.get_value(1) || !facePlacing)) {
            start = System.currentTimeMillis();
            this.placeCrystal();
        }
        if (breakDelayCounter > breakTimeout && (!hasPacketBroke || !packetSafe.get_value(true))) {
            if (antiStuck.get_value(true) && stuckCrystal != null) {
                this.breakCrystal(stuckCrystal);
                stuckCrystal = null;
            } else {
                this.breakCrystal(null);
            }
        }

        if (!didAnything) {
            hasPacketBroke = false;
        }

        breakDelayCounter++;
        placeDelayCounter++;
        facePlaceDelayCounter++;
        obiFeetCounter++;
    }

    private void clearMap(BlockPos checkBlock) {
        List<RenderPos> toRemove = new ArrayList<>();
        if (checkBlock == null || renderMap.isEmpty()) return;
        for (RenderPos pos : renderMap) {
            if (pos.pos.getX() == checkBlock.getX() && pos.pos.getY() == checkBlock.getY() && pos.pos.getZ() == checkBlock.getZ())
                toRemove.add(pos);
        }
        renderMap.removeAll(toRemove);
    }

    private void placeCrystal() {
        ArrayList<BlockPos> placePositions;
        placePositions = this.getBestBlocks();
        currentTargets.clear();

        if (placePositions == null) {
            return;
        }

        currentTargets.addAll(placePositions);

        if (placePositions.size() > 0) {
            boolean offhandCheck = false;
            int slot = InventoryUtil.findHotbarBlockz(ItemEndCrystal.class);
            int old = mc.player.inventory.currentItem;
            EnumHand hand = null;
            int stackSize = getCrystalCount(false);
            alreadyAttacking = false;
            if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && (autoSwitch.in("Allways") || autoSwitch.in("NoGap"))) {
                    if (autoSwitch.in("NoGap")) {
                        if (mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {
                            return;
                        }
                    }
                    if (this.findCrystalsHotbar() == -1) return;
                    mc.player.inventory.currentItem = this.findCrystalsHotbar();

                    //mc.playerController.syncCurrentPlayItem();

                }
            } else {
                offhandCheck = true;
            }
            if (autoSwitch.in("Silent")) {
                if (slot != -1) {
                    if (mc.player.isHandActive() && silentSwitchHand.get_value(true)) {
                        hand = mc.player.getActiveHand();
                    }
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                }
            }
            placeDelayCounter = 0;
            facePlaceDelayCounter = 0;
            didAnything = true;
            for (BlockPos targetBlock : placePositions) {
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal || mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal || autoSwitch.in("Silent")) {
                    if (setYawPitch(targetBlock)) {
                        EntityEnderCrystal cCheck = CrystalUtil.isCrystalStuck(targetBlock.up());
                        if (cCheck != null && antiStuck.get_value(true)) {
                            stuckCrystal = cCheck;
                        }
                        BlockUtil.placeCrystalOnBlock3(targetBlock, offhandCheck ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, placeSwing.get_value(true));
                        crystalsPlaced++;
                    }
                }
            }
            int newSize = getCrystalCount(offhandCheck);
            if (autoSwitch.in("Silent")) {
                if (slot != -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(old));
                    if (silentSwitchHand.get_value(true) && hand != null) {
                        mc.player.setActiveHand(hand);
                    }
                }
            }

            if (newSize == stackSize) {
                didAnything = false;
            }
        }
    }

    private int getCrystalCount(boolean offhand) {
        if (offhand) {
            return mc.player.getHeldItemOffhand().stackSize;
        } else {
            return mc.player.getHeldItemMainhand().stackSize;
        }
    }

    private void breakCrystal(EntityEnderCrystal overwriteCrystal) {
        EntityEnderCrystal crystal;
        if (threaded.get_value(true)) {
            Threads threads = new Threads();
            threads.start();
            crystal = staticEnderCrystal;
        } else {
            crystal = this.getBestCrystal();
        }
        if (overwriteCrystal != null) {
            if (CrystalUtil.calculateDamage(overwriteCrystal, mc.player, ignoreTerrain.get_value(true)) < maxAntiStuckDamage.get_value(1)) {
                crystal = overwriteCrystal;
            }
        }
        if (crystal == null) return;
        if (antiWeakness.get_value(true) && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            boolean shouldWeakness = true;
            if (mc.player.isPotionActive(MobEffects.STRENGTH)) {
                if (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                    shouldWeakness = false;
                }
            }
            if (shouldWeakness) {
                if (!alreadyAttacking) {
                    this.alreadyAttacking = true;
                }
                int newSlot = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.player.inventory.getStackInSlot(i);
                    if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
                        newSlot = i;
                        mc.playerController.updateController();
                        break;
                    }
                }
                if (newSlot != -1) {
                    mc.player.inventory.currentItem = newSlot;
                }
            }
        }
        didAnything = true;
        if (setYawPitch(crystal)) {
            EntityUtil.attackEntity(crystal, this.attackPacket.get_value(true));
            if (!this.swing.in("None")) {
                BlockUtil.swingArm(swing);
            }
            breakDelayCounter = 0;
        }
    }

    public final EntityEnderCrystal getBestCrystal() {
        double bestDamage = 0;
        EntityEnderCrystal bestCrystal = null;
        for (Entity e : mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderCrystal)) continue;
            EntityEnderCrystal crystal = (EntityEnderCrystal) e;
            for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
                if (target == mc.player) continue;

                if (mc.player.getDistanceSq(target) > MathUtil.square((float) targetRange.get_value(1.0))) continue;

                if (entityPredict.get_value(true) && rotateMode.in("Off") && target != mc.player) {
                    float f = target.width / 2.0F, f1 = target.height;
                    target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double) f, target.posY, target.posZ - (double) f, target.posX + (double) f, target.posY + (double) f1, target.posZ + (double) f));
                    Entity y = CrystalUtil.getPredictedPosition(target, predictedTicks.get_value(1));
                    target.setEntityBoundingBox(y.getEntityBoundingBox());
                }

                double targetDamage = this.isCrystalGood(crystal, target);
                if (targetDamage <= 0) continue;
                if (targetDamage > bestDamage) {
                    bestDamage = targetDamage;
                    this.ezTarget = target;
                    bestCrystal = crystal;
                }
            }
        }

        if (this.ezTarget != null) {
            AutoGG.add_target(this.ezTarget.getName());
        }

        if (bestCrystal != null && (when.in("Both") || when.in("Break"))) {
            BlockPos renderPos = bestCrystal.getPosition().down();
            clearMap(renderPos);
            renderMap.add(new RenderPos(renderPos, bestDamage));
        }
        return bestCrystal;
    }

    public final ArrayList<BlockPos> getBestBlocks() {
        ArrayList<RenderPos> posArrayList = new ArrayList<>();
        if (getBestCrystal() != null && fastMode.in("Off")) {
            placeTimeoutFlag = true;
            return null;
        }

        if (placeTimeoutFlag) {
            placeTimeoutFlag = false;
            return null;
        }

        for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
            if (target == mc.player) continue;
            if (mc.player.getDistanceSq(target) > MathUtil.square((float) targetRange.get_value(1.0))) continue;
            if (entityPredict.get_value(true) && target != mc.player) {
                float f = target.width / 2.0F, f1 = target.height;
                target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double) f, target.posY, target.posZ - (double) f, target.posX + (double) f, target.posY + (double) f1, target.posZ + (double) f));
                Entity y = CrystalUtil.getPredictedPosition(target, predictedTicks.get_value(1));
                target.setEntityBoundingBox(y.getEntityBoundingBox());
            }
            ca_target = target;
            for (BlockPos blockPos : CrystalUtil.possiblePlacePositions((float) this.placeRange.get_value(1.0), this.thirteen.get_value(true), true)) {
                double targetDamage = isBlockGood(blockPos, target);
                if (targetDamage <= 0) continue;
                posArrayList.add(new RenderPos(blockPos, targetDamage));
            }
        }

        //sorting all place positions
        posArrayList.sort(new DamageComparator());

        //making sure all positions are placeble and wont block each other
        if (maxCrystals.get_value(1) > 1) {
            List<BlockPos> blockedPosList = new ArrayList<>();
            List<RenderPos> toRemove = new ArrayList<>();
            for (RenderPos test : posArrayList) {
                boolean blocked = false;
                for (BlockPos blockPos : blockedPosList) {
                    if (blockPos.getX() == test.pos.getX() && blockPos.getY() == test.pos.getY() && blockPos.getZ() == test.pos.getZ()) {
                        blocked = true;
                    }
                }
                if (!blocked) {
                    blockedPosList.addAll(getBlockedPositions(test.pos));
                } else toRemove.add(test);
            }
            posArrayList.removeAll(toRemove);
        }

        if (this.ezTarget != null) {
            AutoGG.add_target(this.ezTarget.getName());
        }
        //taking the best out of the list
        int maxCrystals = this.maxCrystals.get_value(1);
        if (facePlacing && noMP.get_value(true)) {
            maxCrystals = 1;
        }
        ArrayList<BlockPos> finalArrayList = new ArrayList<>();
        IntStream.range(0, Math.min(maxCrystals, posArrayList.size())).forEachOrdered(n -> {
            RenderPos pos = posArrayList.get(n);
            if (when.in("Both") || when.in("Place")) {
                clearMap(pos.pos);
                if (pos.pos != null) renderMap.add(pos);
            }
            finalArrayList.add(pos.pos);
        });
        return finalArrayList;
    }

    private ArrayList<BlockPos> getBlockedPositions(BlockPos pos) {
        ArrayList<BlockPos> list = new ArrayList<>();
        list.add(pos.add(1, -1, 1));
        list.add(pos.add(1, -1, -1));
        list.add(pos.add(-1, -1, 1));
        list.add(pos.add(-1, -1, -1));
        list.add(pos.add(-1, -1, 0));
        list.add(pos.add(1, -1, 0));
        list.add(pos.add(0, -1, -1));
        list.add(pos.add(0, -1, 1));
        list.add(pos.add(1, 0, 1));
        list.add(pos.add(1, 0, -1));
        list.add(pos.add(-1, 0, 1));
        list.add(pos.add(-1, 0, -1));
        list.add(pos.add(-1, 0, 0));
        list.add(pos.add(1, 0, 0));
        list.add(pos.add(0, 0, -1));
        list.add(pos.add(0, 0, 1));
        list.add(pos.add(1, 1, 1));
        list.add(pos.add(1, 1, -1));
        list.add(pos.add(-1, 1, 1));
        list.add(pos.add(-1, 1, -1));
        list.add(pos.add(-1, 1, 0));
        list.add(pos.add(1, 1, 0));
        list.add(pos.add(0, 1, -1));
        list.add(pos.add(0, 1, 1));
        return list;
    }

    private double isCrystalGood(EntityEnderCrystal crystal, EntityPlayer target) {

        if (this.isPlayerValid(target)) {
            if (mc.player.canEntityBeSeen(crystal)) {
                if (mc.player.getDistanceSq(crystal) > MathUtil.square((float) this.breakRange.get_value(1.0))) {
                    return 0;
                }
            } else {
                if (mc.player.getDistanceSq(crystal) > MathUtil.square((float) this.breakRangeWall.get_value(1.0))) {
                    return 0;
                }
            }
            if (crystal.isDead) return 0;
            if (attemptedCrystals.contains(crystal)) return 0;
            double targetDamage = CrystalUtil.calculateDamage(crystal, target, ignoreTerrain.get_value(true));
            // set min damage to 2 if we want to kill the dude fast
            facePlacing = false;
            double miniumDamage = this.minBreak.get_value(1);

            if ((EntityUtil.getHealth(target) <= facePlaceHP.get_value(1)) || CrystalUtil.getArmourFucker(target, fuckArmourHP.get_value(1))) {
                miniumDamage = EntityUtil.isInHole(target) ? 1 : 2;
                facePlacing = true;
            }

            if (targetDamage < miniumDamage && EntityUtil.getHealth(target) - targetDamage > 0) return 0;
            double selfDamage = 0;
            if (!ignoreSelfDamage.get_value(true)) {
                selfDamage = CrystalUtil.calculateDamage(crystal, mc.player, ignoreTerrain.get_value(true));
            }
            if (selfDamage > maxSelfBreak.get_value(1)) return 0;
            if (EntityUtil.getHealth(mc.player) - selfDamage <= 0 && this.antiSuicide.get_value(true)) return 0;
            switch (crystalLogic.get_current_value()) {
                case "Safe":
                    return targetDamage - selfDamage;
                case "Damage":
                    return targetDamage;
                case "Nearby":
                    double distance = mc.player.getDistanceSq(crystal);
                    return targetDamage - distance;
            }
        }
        return 0;
    }

    private double isBlockGood(BlockPos blockPos, EntityPlayer target) {
        if (this.isPlayerValid(target)) {
            // if raytracing and cannot see block
            if (!CrystalUtil.canSeePos(blockPos) && raytrace.get_value(true)) return 0;
            // if cannot see pos use wall range, else use normal
            if (!CrystalUtil.canSeePos(blockPos)) {
                if (mc.player.getDistanceSq(blockPos) > MathUtil.square((float) this.placeRangeWall.get_value(1.0))) {
                    return 0;
                }
            } else {
                if (mc.player.getDistanceSq(blockPos) > MathUtil.square((float) this.placeRange.get_value(1.0))) {
                    return 0;
                }
            }

            double targetDamage = CrystalUtil.calculateDamage(blockPos, target, ignoreTerrain.get_value(true));

            facePlacing = false;
            double miniumDamage = this.minPlace.get_value(1);
            if (EntityUtil.getHealth(target) <= facePlaceHP.get_value(1) || CrystalUtil.getArmourFucker(target, fuckArmourHP.get_value(1))) {
                miniumDamage = EntityUtil.isInHole(target) ? 1 : 2;
                facePlacing = true;
            }

            if (targetDamage < miniumDamage && EntityUtil.getHealth(target) - targetDamage > 0) return 0;
            double selfDamage = 0;
            if (!ignoreSelfDamage.get_value(true)) {
                selfDamage = CrystalUtil.calculateDamage(blockPos, mc.player, ignoreTerrain.get_value(true));
            }
            if (selfDamage > maxSelfPlace.get_value(1)) return 0;
            if (EntityUtil.getHealth(mc.player) - selfDamage <= 0 && this.antiSuicide.get_value(true)) return 0;
            switch (crystalLogic.get_current_value()) {
                case "Safe":
                    return targetDamage - selfDamage;
                case "Damage":
                    return targetDamage;
                case "Nearby":
                    double distance = mc.player.getDistanceSq(blockPos);
                    return targetDamage - distance;
            }
        }
        return 0;
    }

    private boolean isPlayerValid(EntityPlayer player) {
        if (player.getHealth() + player.getAbsorptionAmount() <= 0 || player == mc.player) return false;
        if (FriendUtil.isFriend(player.getName())) return false;
        if (player.getName().equals(mc.player.getName())) return false;
        if (player.getDistanceSq(mc.player) > 13 * 13) return false;
        if (this.palceObiFeet.get_value(true) && obiFeetCounter >= timeoutTicksObiFeet.get_value(1) && mc.player.getDistance(player) < 5) {
            try {
                this.blockObiNextToPlayer(player);
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void blockObiNextToPlayer(EntityPlayer player) {
        if (ObiYCheck.get_value(true) && Math.floor(player.posY) == Math.floor(mc.player.posY)) return;
        obiFeetCounter = 0;
        BlockPos pos = EntityUtil.getFlooredPos(player).down();
        if (EntityUtil.isInHole(player) || mc.world.getBlockState(pos).getBlock() == Blocks.AIR) return;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                BlockPos checkPos = pos.add(i, 0, j);
                if (mc.world.getBlockState(checkPos).getMaterial().isReplaceable()) {
                    BlockUtil.placeBlock(checkPos, PlayerUtil.findObiInHotbar(), rotateObiFeet.get_value(true), rotateObiFeet.get_value(true), swing);
                }
            }
        }
    }

    private int findCrystalsHotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    private boolean setYawPitch(EntityEnderCrystal crystal) {
        if (rotateMode.in("Off") || rotateMode.in("Place")) return true;
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), crystal.getPositionEyes(mc.getRenderPartialTicks()));
        float yaw = angle[0];
        float pitch = angle[1];

        RotationUtil.setPlayerRotations(yaw, pitch);

        return true;
    }

    public boolean setYawPitch(BlockPos pos) {
        if (rotateMode.in("Off") || rotateMode.in("Break")) return true;
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
        float yaw = angle[0];
        float pitch = angle[1];

        RotationUtil.setPlayerRotations(yaw, pitch);

        return true;
    }

    static class RenderPos {
        public RenderPos(BlockPos pos, Double damage) {
            this.pos = pos;
            this.damage = damage;
        }

        Double damage;
        double fadeTimer;
        BlockPos pos;
    }

    static class DamageComparator implements Comparator<RenderPos> {
        @Override
        public int compare(RenderPos a, RenderPos b) {
            return b.damage.compareTo(a.damage);
        }
    }

    public static EntityPlayer get_target() {
        return ca_target;
    }

    final class Threads extends Thread {
        EntityEnderCrystal bestCrystal;

        public Threads() {
        }

        @Override
        public void run() {
            bestCrystal = AutoCrystal.INSTANCE.getBestCrystal();
            AutoCrystal.INSTANCE.staticEnderCrystal = bestCrystal;
        }
    }
}


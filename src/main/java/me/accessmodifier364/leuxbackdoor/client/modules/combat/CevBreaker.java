package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventDamageBlock;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;

public class CevBreaker extends Module {
    private final int[][] model = new int[][]{{1, 1, 0}, {-1, 1, 0}, {0, 1, 1}, {0, 1, -1}};
    Setting target = this.create("Target", "Target", "Nearest", this.combobox("Nearest", "Looking"));
    Setting breakCrystal = this.create("Break Crystal", "BreakMode", "Instant", this.combobox("Instant", "Packet", "Vanilla", "None"));
    Setting placeCrystal = this.create("Place Crystal", "PlaceCrystal", true);
    Setting autodisable = this.create("AutoDisableIB", "AutoDisableIB", false);
    Setting enemyRange = this.create("Range", "CivBreakerEnemyRange", 5.1, 1.0, 6.0);
    Setting supDelay = this.create("Sup Delay", "SupDelay", 1, 0, 4);
    Setting endDelay = this.create("End Delay", "EndDelay", 1, 0, 4);
    Setting hitDelay = this.create("Hit Delay", "HitDelay", 2, 0, 20);
    Setting confirmBreak = this.create("Confirm Break", "ConfirmBreak", true);
    Setting confirmPlace = this.create("Confirm Place", "ConfirmPlace", true);
    Setting crystalDelay = this.create("Crystal Delay", "CrystalDelay", 2, 0, 20);
    Setting antiWeakness = this.create("Anti Weakness", "AntiWeakness", false);
    Setting antiStep = this.create("Anti Step", "AntiStep", false);
    Setting trapPlayer = this.create("Trap Player", "TrapPlayer", false);
    Setting fastPlace = this.create("Fast Place", "FastPlace", false);
    Setting blocksPerTick = this.create("Blocks Per Tick", "BPS", 4, 2, 6);
    Setting pickSwitchTick = this.create("PickSwitchTick", "PickSwitchTick", 100, 0, 500);
    Setting switchSword = this.create("Switch Sword", "SwitchSword", false);
    Setting rotate = this.create("Rotate", "Rotate", true);
    Setting midHitDelay = this.create("Mid HitDelay", "MidHitDelay", 1, 0, 5);
    Setting chatMsg = this.create("Chat Messages", "Messages", true);
    Double[][] sur_block = new Double[4][3];
    private boolean noMaterials = false;
    private boolean hasMoved = false;
    private boolean isSneaking = false;
    private boolean isHole = true;
    private boolean enoughSpace = true;
    private boolean broken;
    private boolean deadPl;
    private boolean rotationPlayerMoved;
    private boolean prevBreak, instantBreaking;
    private int oldSlot = -1;
    private int stage;
    private int delayTimeTicks;
    private int hitTryTick;
    private int tickPick;
    private int[] slot_mat;
    private int[] delayTable;
    private int[] enemyCoordsInt;
    @EventHandler
    private final Listener<EventDamageBlock> listener2 = new Listener<>(event -> {
        if (this.enemyCoordsInt != null) {
            if (event.getPos().getX() + ((event.getPos().getX() < 0) ? 1 : 0) == this.enemyCoordsInt[0]) {
                if (event.getPos().getZ() + ((event.getPos().getZ() < 0) ? 1 : 0) == this.enemyCoordsInt[2]) {
                    this.destroyCrystalAlgo();
                }
            }
        }
    });
    private double[] enemyCoordsDouble;
    private structureTemp toPlace;
    private EntityPlayer aimTarget;

    public CevBreaker() {
        super(Category.combat);
        this.name = "CevBreaker";
        this.description = "rat";
    }

    @Override
    protected void enable() {
        this.aimTarget = null;
        this.delayTable = new int[]{this.supDelay.get_value(1), this.crystalDelay.get_value(1), this.hitDelay.get_value(1), this.endDelay.get_value(1)};
        this.toPlace = new structureTemp(0.0, 0, new ArrayList<>());
        this.isHole = true;
        final boolean b = false;
        this.broken = b;
        this.deadPl = b;
        this.rotationPlayerMoved = b;
        this.hasMoved = b;
        this.slot_mat = new int[]{-1, -1, -1, -1};
        final int n = 0;
        this.delayTimeTicks = n;
        this.stage = n;
        if (mc.player == null) {
            this.set_disable();
            return;
        }
        this.oldSlot = mc.player.inventory.currentItem;
        if (this.getAimTarget()) {
            return;
        }
        this.playerChecks();
    }

    private boolean getAimTarget() {
        if (this.target.in("Nearest")) {
            this.aimTarget = EntityUtil.findClosestTarget(this.enemyRange.get_value(1), this.aimTarget);
        } else {
            this.aimTarget = PlayerUtil.findLookingPlayer(this.enemyRange.get_value(1));
        }
        if (this.aimTarget == null || !this.target.in("Looking")) {
            if (!this.target.in("Looking") && this.aimTarget == null) {
                this.set_disable();
            }
            return this.aimTarget == null;
        }
        return false;
    }

    private void playerChecks() {
        if (this.getMaterialsSlot()) {
            if (this.is_in_hole()) {
                this.enemyCoordsDouble = new double[]{this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ};
                this.enemyCoordsInt = new int[]{(int) this.enemyCoordsDouble[0], (int) this.enemyCoordsDouble[1], (int) this.enemyCoordsDouble[2]};
                this.enoughSpace = this.createStructure();
            } else {
                this.isHole = false;
            }
        } else {
            this.noMaterials = true;
        }
    }

    @Override
    protected void disable() {
        if (mc.player == null) {
            return;
        }
        if (breakCrystal.in("Instant") && autodisable.get_value(true)) {
            ModLoader.get_module_manager().get_module_with_tag("InstantBreak").set_disable();
        }
        instantBreaking = false;
        if (this.chatMsg.get_value(true)) {
            String output = "";
            String materialsNeeded = "";
            if (this.aimTarget == null) {
                output = "No target found";
            } else if (this.noMaterials) {
                output = "No Materials Detected";
                materialsNeeded = this.getMissingMaterials();
            } else if (!this.isHole) {
                output = "Enemy is not in hole";
            } else if (!this.enoughSpace) {
                output = "Not enough space";
            } else if (this.hasMoved) {
                output = "Out of range";
            } else if (this.deadPl) {
                output = "Enemy is dead ";
            }

            if (!output.equals("")) {
                MessageUtil.send_client_error_message(output);
            }
            if (!materialsNeeded.equals("")) {
                MessageUtil.send_client_error_message("Materials missing:" + materialsNeeded);
            }
        }
        if (this.isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        if (this.oldSlot != mc.player.inventory.currentItem && this.oldSlot != -1) {
            mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.noMaterials = false;
    }

    private String getMissingMaterials() {
        final StringBuilder output = new StringBuilder();
        if (this.slot_mat[0] == -1) {
            output.append(" Obsidian");
        }
        if (this.slot_mat[1] == -1) {
            output.append(" Crystal");
        }
        if ((this.antiWeakness.get_value(true) || this.switchSword.get_value(true)) && this.slot_mat[3] == -1) {
            output.append(" Sword");
        }
        if (this.slot_mat[2] == -1) {
            output.append(" Pickaxe");
        }
        return output.toString();
    }

    @Override
    public void update() {
        if (mc.player == null || mc.player.isDead) {
            this.set_disable();
            return;
        }
        if (this.delayTimeTicks < this.delayTable[this.stage]) {
            ++this.delayTimeTicks;
            return;
        }
        this.delayTimeTicks = 0;
        if (this.enemyCoordsDouble == null || this.aimTarget == null) {
            if (this.aimTarget == null) {
                this.aimTarget = PlayerUtil.findLookingPlayer(this.enemyRange.get_value(1));
                if (this.aimTarget != null) {
                    this.playerChecks();
                }
            } else {
                this.checkVariable();
            }
            return;
        }
        if (this.aimTarget.isDead) {
            this.deadPl = true;
        }
        if ((int) this.aimTarget.posX != (int) this.enemyCoordsDouble[0] || (int) this.aimTarget.posZ != (int) this.enemyCoordsDouble[2]) {
            this.hasMoved = true;
        }
        if (this.checkVariable()) {
            return;
        }
        if (this.placeSupport()) {
            switch (this.stage) {
                case 1: {
                    this.placeBlockThings(this.stage);
                    if (this.fastPlace.get_value(true)) {
                        this.placeCrystal();
                    }
                    this.prevBreak = false;
                    this.tickPick = 0;
                    break;
                }
                case 2: {
                    if (this.confirmPlace.get_value(true) && !(BlockInteractHelper.getBlock(this.compactBlockPos(0)) instanceof BlockObsidian)) {
                        --this.stage;
                        return;
                    }
                    this.placeCrystal();
                    break;
                }
                case 3: {
                    if (this.confirmPlace.get_value(true) && this.getCrystal() == null) {
                        --this.stage;
                        return;
                    }
                    int switchValue = 3;
                    if (!this.switchSword.get_value(true) || this.tickPick == this.pickSwitchTick.get_value(1) || this.tickPick++ == 0) {
                        switchValue = 2;
                    }
                    if (mc.player.inventory.currentItem != this.slot_mat[switchValue]) {
                        mc.player.inventory.currentItem = this.slot_mat[switchValue];
                    }
                    final BlockPos obbyBreak = new BlockPos(this.enemyCoordsDouble[0], this.enemyCoordsInt[1] + 2, this.enemyCoordsDouble[2]);
                    if (BlockInteractHelper.getBlock(obbyBreak) instanceof BlockObsidian) {
                        final EnumFacing sideBreak = BlockInteractHelper.getPlaceableSide(obbyBreak);
                        if (sideBreak != null) {
                            if (this.breakCrystal.in("Packet")) {
                                if (!this.prevBreak) {
                                    mc.player.swingArm(EnumHand.MAIN_HAND);
                                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, obbyBreak, sideBreak));
                                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, obbyBreak, sideBreak));
                                    this.prevBreak = true;
                                }
                            } else if (this.breakCrystal.in("Vanilla")) {
                                BreakUtil.setblock(obbyBreak);
                                BreakUtil.breakblock(enemyRange.get_value(1));
                            } else if (this.breakCrystal.in("Instant")) {
                                if (!this.instantBreaking) {
                                    ModLoader.get_module_manager().get_module_with_tag("InstantBreak").set_enable();
                                    BreakUtil.setblock(obbyBreak);
                                    BreakUtil.breakblock(enemyRange.get_value(1));

                                    BreakUtil.setblock(null);
                                    BreakUtil.finished = true;

                                    instantBreaking = true;
                                }
                            }
                        }
                        break;
                    }
                    this.destroyCrystalAlgo();
                    break;
                }
            }
        }
    }

    private void placeCrystal() {
        this.placeBlockThings(this.stage);
    }

    private Entity getCrystal() {
        for (final Entity t : mc.world.loadedEntityList) {
            if (t instanceof EntityEnderCrystal && (int) t.posX == this.enemyCoordsInt[0] && (int) t.posZ == this.enemyCoordsInt[2] && t.posY - this.enemyCoordsInt[1] == 3.0) {
                return t;
            }
        }
        return null;
    }

    public void destroyCrystalAlgo() {
        final Entity crystal = this.getCrystal();
        if (this.confirmBreak.get_value(true) && this.broken && crystal == null) {
            this.stage = 0;
            this.broken = false;
        }
        if (crystal != null) {
            this.breakCrystalPiston(crystal);
            if (this.confirmBreak.get_value(true)) {
                this.broken = true;
            } else {
                this.stage = 0;
            }
        } else {
            this.stage = 0;
        }
    }

    private void breakCrystalPiston(final Entity crystal) {
        if (this.hitTryTick++ < this.midHitDelay.get_value(1)) {
            return;
        }
        this.hitTryTick = 0;
        if (this.antiWeakness.get_value(true)) {
            mc.player.inventory.currentItem = this.slot_mat[3];
        }
        if (this.breakCrystal.in("Vanilla")) {
            EntityUtil.attackEntity(crystal);
        } else if (this.breakCrystal.in("Packet") || this.breakCrystal.in("Instant")) {
            try {
                mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
                mc.player.swingArm(EnumHand.MAIN_HAND);
            } catch (NullPointerException ignored) {
            }
        }
    }

    private boolean placeSupport() {
        int checksDone = 0;
        int blockDone = 0;
        if (this.toPlace.supportBlock > 0) {
            do {
                final BlockPos targetPos = this.getTargetPos(checksDone);
                if (this.placeBlock(targetPos, 0) && ++blockDone == this.blocksPerTick.get_value(1)) {
                    return false;
                }
            } while (++checksDone != this.toPlace.supportBlock);
        }
        this.stage = ((this.stage == 0) ? 1 : this.stage);
        return true;
    }

    private boolean placeBlock(final BlockPos pos, final int step) {
        final Block block = mc.world.getBlockState(pos).getBlock();
        final EnumFacing side = BlockInteractHelper.getPlaceableSide(pos);
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        if (this.slot_mat[step] == 11 || mc.player.inventory.getStackInSlot(this.slot_mat[step]) != ItemStack.EMPTY) {
            if (mc.player.inventory.currentItem != this.slot_mat[step]) {
                mc.player.inventory.currentItem = ((this.slot_mat[step] == 11) ? mc.player.inventory.currentItem : this.slot_mat[step]);
            }
            if ((!this.isSneaking && BlockInteractHelper.blackList.contains(neighbourBlock)) || BlockInteractHelper.shulkerList.contains(neighbourBlock)) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                this.isSneaking = true;
            }
            if (this.rotate.get_value(true)) {
                BlockInteractHelper.faceVectorPacketInstant(hitVec, true);
            }
            EnumHand handSwing = EnumHand.MAIN_HAND;
            if (this.slot_mat[step] == 11) {
                handSwing = EnumHand.OFF_HAND;
            }
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, handSwing);
            mc.player.swingArm(handSwing);
            return true;
        }
        this.noMaterials = true;
        return false;
    }

    public void placeBlockThings(int step) {
        if (step != 1 || this.placeCrystal.get_value(true)) {
            --step;
            final BlockPos targetPos = this.compactBlockPos(step);
            this.placeBlock(targetPos, step);
        }
        ++this.stage;
    }

    public BlockPos compactBlockPos(final int step) {
        final BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + step));
        return new BlockPos(this.enemyCoordsDouble[0] + offsetPos.getX(), this.enemyCoordsDouble[1] + offsetPos.getY(), this.enemyCoordsDouble[2] + offsetPos.getZ());
    }

    private BlockPos getTargetPos(final int idx) {
        final BlockPos offsetPos = new BlockPos(this.toPlace.to_place.get(idx));
        return new BlockPos(this.enemyCoordsDouble[0] + offsetPos.getX(), this.enemyCoordsDouble[1] + offsetPos.getY(), this.enemyCoordsDouble[2] + offsetPos.getZ());
    }

    private boolean checkVariable() {
        if (this.noMaterials || !this.isHole || !this.enoughSpace || this.hasMoved || this.deadPl || this.rotationPlayerMoved) {
            this.set_disable();
            return true;
        }
        return false;
    }

    private boolean createStructure() {
        if (Objects.requireNonNull(BlockInteractHelper.getBlock(this.enemyCoordsDouble[0], this.enemyCoordsDouble[1] + 2.0, this.enemyCoordsDouble[2]).getRegistryName()).toString().toLowerCase().contains("bedrock") || !(BlockInteractHelper.getBlock(this.enemyCoordsDouble[0], this.enemyCoordsDouble[1] + 3.0, this.enemyCoordsDouble[2]) instanceof BlockAir) || !(BlockInteractHelper.getBlock(this.enemyCoordsDouble[0], this.enemyCoordsDouble[1] + 4.0, this.enemyCoordsDouble[2]) instanceof BlockAir)) {
            return false;
        }
        double min_found = Double.MAX_VALUE;
        int cor = 0;
        int i = 0;
        for (final Double[] cord_b : this.sur_block) {
            final double distance_now;
            if ((distance_now = mc.player.getDistanceSq(new BlockPos(cord_b[0], cord_b[1], cord_b[2]))) < min_found) {
                min_found = distance_now;
                cor = i;
            }
            ++i;
        }
        final int bias = (this.enemyCoordsInt[0] == (int) mc.player.posX || this.enemyCoordsInt[2] == (int) mc.player.posZ) ? -1 : 1;
        this.toPlace.to_place.add(new Vec3d(this.model[cor][0] * bias, 1.0, this.model[cor][2] * bias));
        this.toPlace.to_place.add(new Vec3d(this.model[cor][0] * bias, 2.0, this.model[cor][2] * bias));
        this.toPlace.supportBlock = 2;
        if (this.trapPlayer.get_value(true) || this.antiStep.get_value(true)) {
            for (int high = 1; high < 3; ++high) {
                if (high != 2 || this.antiStep.get_value(true)) {
                    for (final int[] modelBas : this.model) {
                        final Vec3d toAdd = new Vec3d(modelBas[0], high, modelBas[2]);
                        if (!this.toPlace.to_place.contains(toAdd)) {
                            this.toPlace.to_place.add(toAdd);
                            final structureTemp toPlace = this.toPlace;
                            ++toPlace.supportBlock;
                        }
                    }
                }
            }
        }
        this.toPlace.to_place.add(new Vec3d(0.0, 2.0, 0.0));
        this.toPlace.to_place.add(new Vec3d(0.0, 3.0, 0.0));
        return true;
    }

    private boolean getMaterialsSlot() {
        if (mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            this.slot_mat[1] = 11;
        }
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (this.slot_mat[1] == -1 && stack.getItem() instanceof ItemEndCrystal) {
                    this.slot_mat[1] = i;
                } else if ((this.antiWeakness.get_value(true) || this.switchSword.get_value(true)) && stack.getItem() instanceof ItemSword) {
                    this.slot_mat[3] = i;
                } else if (stack.getItem() instanceof ItemPickaxe) {
                    this.slot_mat[2] = i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock) stack.getItem()).getBlock();
                    if (block instanceof BlockObsidian) {
                        this.slot_mat[0] = i;
                    }
                }
            }
        }
        int count = 0;
        for (final int val : this.slot_mat) {
            if (val != -1) {
                ++count;
            }
        }
        return count >= 3 + ((this.antiWeakness.get_value(true) || this.switchSword.get_value(true)) ? 1 : 0);
    }

    private boolean is_in_hole() {
        this.sur_block = new Double[][]{{this.aimTarget.posX + 1.0, this.aimTarget.posY, this.aimTarget.posZ}, {this.aimTarget.posX - 1.0, this.aimTarget.posY, this.aimTarget.posZ}, {this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ + 1.0}, {this.aimTarget.posX, this.aimTarget.posY, this.aimTarget.posZ - 1.0}};
        return BlockUtil.isHole(EntityUtil.getPosition(this.aimTarget), true, true).getType() != BlockUtil.HoleType.NONE;
    }

    private static class structureTemp {
        public double distance;
        public int supportBlock;
        public ArrayList<Vec3d> to_place;
        public int direction;

        public structureTemp(final double distance, final int supportBlock, final ArrayList<Vec3d> to_place) {
            this.distance = distance;
            this.supportBlock = supportBlock;
            this.to_place = to_place;
            this.direction = -1;
        }
    }
}
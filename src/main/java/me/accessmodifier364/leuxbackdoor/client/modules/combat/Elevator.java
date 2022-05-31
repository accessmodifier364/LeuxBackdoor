package me.accessmodifier364.leuxbackdoor.client.modules.combat;

import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.BlockUtil;
import me.accessmodifier364.leuxbackdoor.client.util.FriendUtil;
import me.accessmodifier364.leuxbackdoor.client.util.MessageUtil;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.stream.Collectors;

public class Elevator extends Module {
    public Elevator() {
        super(Category.combat);
        this.name        = "Elevator";
        this.description = "anti hole camp completely made by me";
    }

    Setting rotate = create("Rotate","Rotate", false);
    Setting holefill = create("HoleFill", "HoleFill", false);
    //Setting skull = create("PlaceSkull", "PlaceSkull", false);
    Setting mode = create("Mode", "Mode", "Block", combobox("Torch", "Block"));
    Setting range = create("Range", "Range", 5, 1, 6);
    Setting swing = create("Swing", "Swing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));

    EntityPlayer target = null;
    int pistonSide;
    boolean pistonPlaced = false, redstonePlaced = false;
    BlockPos pistonToPlace = null, redstoneToPlace = null;

    @Override
    public void enable() {
        if (mc.player != null && mc.world != null) {
            try {

                checkFlags();

                /* CHING CHONG CODE ALERT
                if (skull.get_value(true) && target != null) {
                    if (getSkullSlot() != -1) {
                        BlockUtil.placeBlockIntercepted(target.getPosition(), getSkullSlot(), rotate.get_value(true), false, swing);
                    } else {
                        MessageUtil.send_client_message("You don't have SKULL");
                    }
                }
                */

                if (!pistonPlaced && pistonToPlace != null) {
                    if (pistonSide == 1) {
                        BlockUtil.rotatePacket(mc.player.posX + 1, mc.player.posY, mc.player.posZ);
                    } else if (pistonSide == 2) {
                        BlockUtil.rotatePacket(mc.player.posX, mc.player.posY, mc.player.posZ + 1);
                    } else if (pistonSide == 3) {
                        BlockUtil.rotatePacket(mc.player.posX - 1, mc.player.posY, mc.player.posZ);
                    } else if (pistonSide == 4) {
                        BlockUtil.rotatePacket(mc.player.posX, mc.player.posY, mc.player.posZ - 1);
                    }
                    BlockUtil.placeBlock(pistonToPlace, getPiston(), rotate.get_value(true), false, swing);

                }

                if (!redstonePlaced && redstoneToPlace != null) {
                    BlockUtil.placeBlock(redstoneToPlace, getRedstone(), rotate.get_value(true), false, swing);
                }

                set_disable();
            } catch (Exception e) {
                e.printStackTrace();
                MessageUtil.send_client_message("An error has ocurred: " + e.getMessage());
                set_disable();
            }
        }
    }

    @Override
    public void disable() {
        if (mc.player != null && mc.world != null) {
            redstonePlaced = false;
            pistonPlaced = false;
            pistonToPlace = null;
            redstoneToPlace = null;
            target = null;

            if (holefill.get_value(true)) {
                ModLoader.get_module_manager().get_module_with_tag("HoleFill").set_enable();
            }
        }
    }

    private boolean canPlace() {
        return mc.world.getBlockState(new BlockPos(target.posX, target.posY + 1, target.posZ)).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(new BlockPos(target.posX, target.posY + 2, target.posZ)).getBlock().equals(Blocks.AIR) && piston() && redstone();
    }

    private boolean piston() {
        boolean canPlacePiston = false;
        pistonSide = 0;

        BlockPos[] posToCheck = {
                new BlockPos(target.posX + 1, target.posY + 1, target.posZ),
                new BlockPos(target.posX, target.posY + 1, target.posZ + 1),
                new BlockPos(target.posX - 1, target.posY + 1, target.posZ),
                new BlockPos(target.posX, target.posY + 1, target.posZ - 1)
        };

        for (BlockPos checkPistons : posToCheck) {
            pistonSide += 1;
            if (mc.world.getBlockState(checkPistons).getBlock().equals(Blocks.PISTON) || mc.world.getBlockState(checkPistons).getBlock().equals(Blocks.STICKY_PISTON)) {
                pistonPlaced = true;
                break;
            }
        }

        if (pistonPlaced) {
            switch (pistonSide) {
                case 1:
                    if (mc.world.getBlockState(posToCheck[2]).getBlock().equals(Blocks.AIR)) {
                        canPlacePiston = true;
                    }
                    break;
                case 2:
                    if (mc.world.getBlockState(posToCheck[3]).getBlock().equals(Blocks.AIR)) {
                        canPlacePiston = true;
                    }
                    break;
                case 3:
                    if (mc.world.getBlockState(posToCheck[0]).getBlock().equals(Blocks.AIR)) {
                        canPlacePiston = true;
                    }
                    break;
                case 4:
                    if (mc.world.getBlockState(posToCheck[1]).getBlock().equals(Blocks.AIR)) {
                        canPlacePiston = true;
                    }
                    break;
            }
        } else {

            if (mc.world.getBlockState(posToCheck[0]).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(posToCheck[2]).getBlock().equals(Blocks.AIR)) {

                if (mc.player.getDistanceSq(posToCheck[0]) < mc.player.getDistanceSq(posToCheck[2])) {
                    if (mc.player.getDistanceSq(posToCheck[2]) <= 9) {
                        canPlacePiston = true;
                        pistonToPlace = posToCheck[2];
                        pistonSide = 3;
                    } else {
                        canPlacePiston = true;
                        pistonToPlace = posToCheck[0];
                        pistonSide = 1;
                    }
                } else {
                    if (mc.player.getDistanceSq(posToCheck[0]) <= 9) {
                        canPlacePiston = true;
                        pistonToPlace = posToCheck[0];
                        pistonSide = 1;
                    } else {
                        canPlacePiston = true;
                        pistonToPlace = posToCheck[2];
                        pistonSide = 3;
                    }
                }

            } else if (mc.world.getBlockState(posToCheck[1]).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(posToCheck[3]).getBlock().equals(Blocks.AIR)) {

                if (mc.player.getDistanceSq(posToCheck[1]) < mc.player.getDistanceSq(posToCheck[3])) {
                    if (mc.player.getDistanceSq(posToCheck[3]) <= 9) {
                        canPlacePiston = true;
                        pistonToPlace = posToCheck[3];
                        pistonSide = 4;
                    } else {
                            canPlacePiston = true;
                            pistonToPlace = posToCheck[1];
                            pistonSide = 2;
                    }
                } else {
                    if (mc.player.getDistanceSq(posToCheck[1]) <= 9) {
                        canPlacePiston = true;
                        pistonToPlace = posToCheck[1];
                        pistonSide = 2;
                    } else {
                            canPlacePiston = true;
                            pistonToPlace = posToCheck[3];
                            pistonSide = 4;
                    }
                }
            }

        }

        if (pistonToPlace != null) {
            if (!isntIntercepted(pistonToPlace)) {
                MessageUtil.send_client_message("Is intercepted by other entity");
                canPlacePiston = false;
            }
        }

        return canPlacePiston;
    }

    private boolean redstone() {
        boolean canPlaceRedstone = false;

        BlockPos redstoneSideA = null, redstoneSideB = null, redstoneSideC = null, redstoneSideD = null, redstoneSideE = null;

        switch (pistonSide) {
            case 1:
                redstoneSideA = new BlockPos(target.posX + 2, target.posY + 1, target.posZ);
                if (mode.in("Block")) {
                    redstoneSideB = new BlockPos(target.posX + 1, target.posY + 2, target.posZ);
                }
                redstoneSideC = new BlockPos(target.posX + 1, target.posY, target.posZ);
                redstoneSideD = new BlockPos(target.posX + 1, target.posY + 1, target.posZ + 1);
                redstoneSideE = new BlockPos(target.posX + 1, target.posY + 1, target.posZ - 1);
                break;
            case 2:
                redstoneSideA = new BlockPos(target.posX, target.posY + 1, target.posZ + 2);
                if (mode.in("Block")) {
                    redstoneSideB = new BlockPos(target.posX, target.posY + 2, target.posZ + 1);
                }
                redstoneSideC = new BlockPos(target.posX, target.posY, target.posZ + 1);
                redstoneSideD = new BlockPos(target.posX + 1, target.posY + 1, target.posZ + 1);
                redstoneSideE = new BlockPos(target.posX - 1, target.posY + 1, target.posZ + 1);
                break;
            case 3:
                redstoneSideA = new BlockPos(target.posX - 2, target.posY + 1, target.posZ);
                if (mode.in("Block")) {
                    redstoneSideB = new BlockPos(target.posX - 1, target.posY + 2, target.posZ);
                }
                redstoneSideC = new BlockPos(target.posX - 1, target.posY, target.posZ);
                redstoneSideD = new BlockPos(target.posX - 1, target.posY + 1, target.posZ + 1);
                redstoneSideE = new BlockPos(target.posX - 1, target.posY + 1, target.posZ - 1);
                break;
            case 4:
                redstoneSideA = new BlockPos(target.posX, target.posY + 1, target.posZ - 2);
                if (mode.in("Block")) {
                    redstoneSideB = new BlockPos(target.posX, target.posY + 2, target.posZ - 1);
                }
                redstoneSideC = new BlockPos(target.posX, target.posY, target.posZ - 1);
                redstoneSideD = new BlockPos(target.posX + 1, target.posY + 1, target.posZ - 1);
                redstoneSideE = new BlockPos(target.posX - 1, target.posY + 1, target.posZ - 1);
                break;
        }

        assert redstoneSideA != null;
        BlockPos[] check = {redstoneSideA, redstoneSideB, redstoneSideC, redstoneSideD, redstoneSideE};

        for (BlockPos pos : check) {
            if (pos != null) {
                if (mc.world.getBlockState(pos).getBlock().equals(Blocks.REDSTONE_TORCH)) {
                    canPlaceRedstone = true;
                    redstonePlaced = true;
                    break;
                } else if (mc.world.getBlockState(pos).getBlock().equals(Blocks.REDSTONE_BLOCK)) {
                    canPlaceRedstone = true;
                    redstonePlaced = true;
                    break;
                }
            }
        }

        if (!redstonePlaced) {
            for (BlockPos toPlace : check) {
                if (toPlace != null) {
                    if (mc.world.getBlockState(toPlace).getBlock().equals(Blocks.AIR) && isntIntercepted(toPlace)) {
                        redstoneToPlace = toPlace;
                        canPlaceRedstone = true;
                        break;
                    }
                }
            }
        }

        return canPlaceRedstone;

    }

    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        for (EntityPlayer targetList : mc.world.playerEntities.stream().filter(entityPlayer -> !FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            if (targetList != null && mc.player.getDistance(targetList) <= range.get_value(1)) {
                target = targetList;
            }
        }
        return target;
    }

    private static boolean isntIntercepted(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox()) && !(entity instanceof EntityItem)) {
                return false;
            }
        }
        return true;
    }

    public static int getHotbarSlot(final Block block) {
        for (int i = 0; i < 9; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) return i;
        }
        return -1;
    }

    public static int getSkullSlot() {
        List<ItemStack> mainInventory = mc.player.inventory.mainInventory;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mainInventory.get(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemSkull)
                return i;
        }
        return -1;
    }

    private void checkFlags() {
        target = getTarget();

        if (target == null) {
            MessageUtil.send_client_message("Can't find target");
            set_disable();
            return;
        }

        if (mc.player.posY > target.posY + 1 || (mc.player.getDistance(target) <= 2 && mc.player.posY < target.posY)) {
            MessageUtil.send_client_message("You cannot be 2+ blocks under the enemy or 2- above and near");
            set_disable();
            return;
        }

        BlockPos blockPos = new BlockPos(target.posX, target.posY + 0.2, target.posZ);
        if (!(mc.world.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK)) && !(mc.world.getBlockState(blockPos).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(blockPos).getBlock().equals(Blocks.ENDER_CHEST) || mc.world.getBlockState(blockPos).getBlock().equals(Blocks.SKULL)) && BlockUtil.isHole(blockPos, true, true).getType() == BlockUtil.HoleType.NONE) {
            MessageUtil.send_client_message("Player isn't in Hole, Packetflying, Burrowed");
            set_disable();
            return;
        }

        if (getHotbarSlot(Blocks.PISTON) == -1 && getHotbarSlot(Blocks.STICKY_PISTON) == -1) {
            MessageUtil.send_client_message("You don't have PISTON or STICKY_PISTON");
            set_disable();
            return;
        }

        if (mode.in("Torch") && getHotbarSlot(Blocks.REDSTONE_TORCH) == -1) {
            MessageUtil.send_client_message("You don't have REDSTONE_TORCH");
            set_disable();
            return;
        }

        if (mode.in("Block") && getHotbarSlot(Blocks.REDSTONE_BLOCK) == -1) {
            MessageUtil.send_client_message("You don't have REDSTONE_BLOCK");
            set_disable();
            return;
        }

        if (!canPlace()) {
            MessageUtil.send_client_message("Don't have space");
            set_disable();
        }
    }

    private int getRedstone() {
        int redstone_slot;
        if (mode.in("Block")) {
            redstone_slot = getHotbarSlot(Blocks.REDSTONE_BLOCK);
        } else {
            redstone_slot = getHotbarSlot(Blocks.REDSTONE_TORCH);
        }
        return redstone_slot;
    }

    private int getPiston() {
        int piston_slot;
        if (getHotbarSlot(Blocks.PISTON) != -1) {
            piston_slot = getHotbarSlot(Blocks.PISTON);
        } else {
            piston_slot = getHotbarSlot(Blocks.STICKY_PISTON);
        }
        return piston_slot;
    }

}
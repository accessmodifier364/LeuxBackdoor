package me.accessmodifier364.leuxbackdoor.client.modules.player;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventRenderLayers;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.client.util.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class AutoBuilder extends Module {
    public AutoBuilder() {
        super(Category.player);
        this.name        = "AutoBuilder";
        this.description = "cum";
    }

    Setting Mode = create("Mode", "Mode", "Highway", combobox("Highway", "Swastika", "HighwayTunnel", "Portal", "Flat", "Tower", "Cover", "Wall", "HighwayWall", "Stair"));
    Setting BuildingMode = create("Building Mode", "BuildingMode", "Dynamic", combobox("Dynamic", "Static"));
    Setting BlocksPerTick = create ("BlocksPerTick", "BlocksPerTick", 4, 1, 10);
    Setting ticks = create("Ticks", "Ticks", 4, 0, 15);
    Setting Render = create("Render", "Render", true);

    private final Vec3d Center = Vec3d.ZERO;
    private final ICamera camera = new Frustum();
    private final Timer NetherPortalTimer = new Timer();
    private BlockPos SourceBlock = null;
    int delayTicks;

    @Override
    public void enable() {
        if (fullNullCheck()) {
            set_disable();
        }

        delayTicks = 0;
        SourceBlock = null;
        BlockArray.clear();
    }

    private float PitchHead = 0.0f;
    private boolean SentPacket = false;
    ArrayList<BlockPos> BlockArray = new ArrayList<>();

    @Override
    public void update() {

        final Vec3d pos = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());

        BlockPos orignPos = new BlockPos(pos.x, pos.y+0.5f, pos.z);

        int lastSlot;
        Pair2<Integer, Block> l_Pair = findStackHotbar();

        int slot = -1;
        double l_Offset = pos.y - orignPos.getY();

        if (l_Pair != null) {
            slot = l_Pair.getFirst();

            if (l_Pair.getSecond() instanceof BlockSlab)
            {
                if (l_Offset == 0.5f)
                {
                    orignPos = new BlockPos(pos.x, pos.y+0.5f, pos.z);
                }
            }
        }

        if (BuildingMode.in("Dynamic"))
            BlockArray.clear();

        if (BlockArray.isEmpty())
            FillBlockArrayAsNeeded(pos, orignPos, l_Pair);

        boolean l_NeedPlace = false;

        float[] rotations = null;

        if (slot != -1)
        {
            if ((mc.player.onGround))
            {
                lastSlot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = slot;
                mc.playerController.updateController();

                int l_BlocksPerTick = BlocksPerTick.get_value(1);

                for (BlockPos l_Pos : BlockArray) {
                    /*ValidResult l_Result = BlockInteractHelper.valid(l_Pos);

                    if (l_Result == ValidResult.AlreadyBlockThere && !mc.world.getBlockState(l_Pos).getMaterial().isReplaceable())
                        continue;

                    if (l_Result == ValidResult.NoNeighbors)
                        continue;*/

                    if (delayTicks > ticks.get_value(1)) {
                        BlockInteractHelper.PlaceResult l_Place = BlockInteractHelper.place(l_Pos, 5.0F, false, (l_Offset == -0.5D));
                        if (l_Place != BlockInteractHelper.PlaceResult.Placed)
                            continue;
                        l_NeedPlace = true;
                        rotations = BlockInteractHelper.getLegitRotations(new Vec3d(l_Pos.getX(), l_Pos.getY(), l_Pos.getZ()));
                    delayTicks = 0;
                }
                    ++delayTicks;

                    if (rotations == null) {
                        set_disable();
                        return;
                    }
                    if (--l_BlocksPerTick <= 0)
                        break;
                }

                if (!slotEqualsBlock(lastSlot, l_Pair.getSecond()))
                {
                    mc.player.inventory.currentItem = lastSlot;
                }
                mc.playerController.updateController();
            }
        }

        if (!l_NeedPlace && Mode.in("Portal")) {
            if (mc.world.getBlockState(BlockArray.get(0).up()).getBlock() == Blocks.PORTAL || !VerifyPortalFrame(BlockArray))
                return;

            if (mc.player.getHeldItemMainhand().getItem() != Items.FLINT_AND_STEEL)
            {
                for (int l_I = 0; l_I < 9; ++l_I)
                {
                    ItemStack l_Stack = mc.player.inventory.getStackInSlot(l_I);
                    if (l_Stack.isEmpty())
                        continue;

                    if (l_Stack.getItem() == Items.FLINT_AND_STEEL)
                    {
                        mc.player.inventory.currentItem = l_I;
                        mc.playerController.updateController();
                        NetherPortalTimer.reset();
                        break;
                    }
                }
            }

            if (!NetherPortalTimer.passed(500))
            {
                if (SentPacket)
                {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockArray.get(0), EnumFacing.UP, EnumHand.MAIN_HAND, 0f, 0f, 0f));
                }

                rotations = BlockInteractHelper.getLegitRotations(new Vec3d(BlockArray.get(0).getX(), BlockArray.get(0).getY()+0.5f, BlockArray.get(0).getZ()));
                l_NeedPlace = true;
            }
            else
                return;
        }
        else if (l_NeedPlace && Mode.in("Portal"))
            NetherPortalTimer.reset();

        if (!l_NeedPlace || rotations == null)
        {
            PitchHead = -420.0f;
            SentPacket = false;
            return;
        }

        /// @todo: clean this up

        boolean l_IsSprinting = mc.player.isSprinting();

        if (l_IsSprinting != mc.player.serverSprintState)
        {
            if (l_IsSprinting)
            {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }

            mc.player.serverSprintState = l_IsSprinting;
        }

        boolean l_IsSneaking = mc.player.isSneaking();

        if (l_IsSneaking != mc.player.serverSneakState)
        {
            if (l_IsSneaking)
            {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            else
            {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            mc.player.serverSneakState = l_IsSneaking;
        }

        if (PlayerUtil.isCurrentViewEntity())
        {
            float l_Pitch = rotations[1];
            float l_Yaw = rotations[0];

            mc.player.rotationYawHead = l_Yaw;
            PitchHead = l_Pitch;

            AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
            double l_PosXDifference = mc.player.posX - mc.player.lastReportedPosX;
            double l_PosYDifference = axisalignedbb.minY - mc.player.lastReportedPosY;
            double l_PosZDifference = mc.player.posZ - mc.player.lastReportedPosZ;
            double l_YawDifference = l_Yaw - mc.player.lastReportedYaw;
            double l_RotationDifference = l_Pitch - mc.player.lastReportedPitch;
            ++mc.player.positionUpdateTicks;
            boolean l_MovedXYZ = l_PosXDifference * l_PosXDifference + l_PosYDifference * l_PosYDifference + l_PosZDifference * l_PosZDifference > 9.0E-4D || mc.player.positionUpdateTicks >= 20;
            boolean l_MovedRotation = l_YawDifference != 0.0D || l_RotationDifference != 0.0D;

            if (mc.player.isRiding())
            {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, -999.0D, mc.player.motionZ, l_Yaw, l_Pitch, mc.player.onGround));
                l_MovedXYZ = false;
            }
            else if (l_MovedXYZ && l_MovedRotation)
            {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, axisalignedbb.minY, mc.player.posZ, l_Yaw, l_Pitch, mc.player.onGround));
            }
            else if (l_MovedXYZ)
            {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, axisalignedbb.minY, mc.player.posZ, mc.player.onGround));
            }
            else if (l_MovedRotation)
            {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(l_Yaw, l_Pitch, mc.player.onGround));
            }
            else if (mc.player.prevOnGround != mc.player.onGround)
            {
                mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
            }

            if (l_MovedXYZ)
            {
                mc.player.lastReportedPosX = mc.player.posX;
                mc.player.lastReportedPosY = axisalignedbb.minY;
                mc.player.lastReportedPosZ = mc.player.posZ;
                mc.player.positionUpdateTicks = 0;
            }

            if (l_MovedRotation)
            {
                mc.player.lastReportedYaw = l_Yaw;
                mc.player.lastReportedPitch = l_Pitch;
            }

            SentPacket = true;
            mc.player.prevOnGround = mc.player.onGround;
            mc.player.autoJumpEnabled = mc.player.mc.gameSettings.autoJump;
        }
    }

    @EventHandler
    private final Listener<EventRenderLayers> OnRender = new Listener<>(p_Event -> {
        if (p_Event.getEntityLivingBase() == mc.player)
            p_Event.SetHeadPitch(PitchHead == -420.0f ? mc.player.rotationPitch : PitchHead);
    });

    @Override
    public void render() {
        if (Render.get_value(true)) {

            Iterator l_Itr = BlockArray.iterator();

            while (l_Itr.hasNext()) {
                BlockPos l_Pos = (BlockPos) l_Itr.next();

                IBlockState l_State = mc.world.getBlockState(l_Pos);

                if (l_State != null && l_State.getBlock() != Blocks.AIR && l_State.getBlock() != Blocks.WATER)
                    continue;

                final AxisAlignedBB bb = new AxisAlignedBB(l_Pos.getX() - mc.getRenderManager().viewerPosX,
                        l_Pos.getY() - mc.getRenderManager().viewerPosY, l_Pos.getZ() - mc.getRenderManager().viewerPosZ,
                        l_Pos.getX() + 1 - mc.getRenderManager().viewerPosX,
                        l_Pos.getY() + (1) - mc.getRenderManager().viewerPosY,
                        l_Pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);

                camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY,
                        mc.getRenderViewEntity().posZ);

                if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX,
                        bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ,
                        bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY,
                        bb.maxZ + mc.getRenderManager().viewerPosZ))) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    glEnable(GL_LINE_SMOOTH);
                    glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
                    glLineWidth(1.5f);

                    final double dist = mc.player.getDistance(l_Pos.getX() + 0.5f, l_Pos.getY() + 0.5f, l_Pos.getZ() + 0.5f)
                            * 0.75f;

                    float alpha = MathUtil.clamp((float) (dist * 255.0f / 5.0f / 255.0f), 0.0f, 0.3f);

                    //  public static void drawBoundingBox(AxisAlignedBB bb, float width, int color)


                    int l_Color = 0x9000FFFF;

                    RenderUtil.drawBoundingBox(bb, 1.0f, l_Color);
                    RenderUtil.drawFilledBox(bb, l_Color);
                    glDisable(GL_LINE_SMOOTH);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    private boolean slotEqualsBlock(int slot, Block type)
    {
        if (mc.player.inventory.getStackInSlot(slot).getItem() instanceof ItemBlock)
        {
            final ItemBlock block = (ItemBlock) mc.player.inventory.getStackInSlot(slot).getItem();
            return block.getBlock() == type;
        }

        return false;
    }

    private void FillBlockArrayAsNeeded(final Vec3d pos, final BlockPos orignPos, final Pair2<Integer, Block> p_Pair) {
        BlockPos interpPos = null;

        if (Mode.in("Highway")) {
            switch (PlayerUtil.GetFacing()) {
                case East:
                    BlockArray.add(orignPos.down());
                    BlockArray.add(orignPos.down().east());
                    BlockArray.add(orignPos.down().east().north());
                    BlockArray.add(orignPos.down().east().south());
                    BlockArray.add(orignPos.down().east().north().north());
                    BlockArray.add(orignPos.down().east().south().south());
                    BlockArray.add(orignPos.down().east().north().north().north());
                    BlockArray.add(orignPos.down().east().south().south().south());
                    BlockArray.add(orignPos.down().east().north().north().north().up());
                    BlockArray.add(orignPos.down().east().south().south().south().up());
                    break;
                case North:
                    BlockArray.add(orignPos.down());
                    BlockArray.add(orignPos.down().north());
                    BlockArray.add(orignPos.down().north().east());
                    BlockArray.add(orignPos.down().north().west());
                    BlockArray.add(orignPos.down().north().east().east());
                    BlockArray.add(orignPos.down().north().west().west());
                    BlockArray.add(orignPos.down().north().east().east().east());
                    BlockArray.add(orignPos.down().north().west().west().west());
                    BlockArray.add(orignPos.down().north().east().east().east().up());
                    BlockArray.add(orignPos.down().north().west().west().west().up());
                    break;
                case South:
                    BlockArray.add(orignPos.down());
                    BlockArray.add(orignPos.down().south());
                    BlockArray.add(orignPos.down().south().east());
                    BlockArray.add(orignPos.down().south().west());
                    BlockArray.add(orignPos.down().south().east().east());
                    BlockArray.add(orignPos.down().south().west().west());
                    BlockArray.add(orignPos.down().south().east().east().east());
                    BlockArray.add(orignPos.down().south().west().west().west());
                    BlockArray.add(orignPos.down().south().east().east().east().up());
                    BlockArray.add(orignPos.down().south().west().west().west().up());
                    break;
                case West:
                    BlockArray.add(orignPos.down());
                    BlockArray.add(orignPos.down().west());
                    BlockArray.add(orignPos.down().west().north());
                    BlockArray.add(orignPos.down().west().south());
                    BlockArray.add(orignPos.down().west().north().north());
                    BlockArray.add(orignPos.down().west().south().south());
                    BlockArray.add(orignPos.down().west().north().north().north());
                    BlockArray.add(orignPos.down().west().south().south().south());
                    BlockArray.add(orignPos.down().west().north().north().north().up());
                    BlockArray.add(orignPos.down().west().south().south().south().up());
                    break;
                default:
                    break;
            }
        }
        
            if (Mode.in("HighwayTunnel")) {
                BlockArray.add(orignPos.down());
                BlockArray.add(orignPos.down().north());
                BlockArray.add(orignPos.down().north().east());
                BlockArray.add(orignPos.down().north().west());
                BlockArray.add(orignPos.down().north().east().east());
                BlockArray.add(orignPos.down().north().west().west());
                BlockArray.add(orignPos.down().north().east().east().east());
                BlockArray.add(orignPos.down().north().west().west().west());
                BlockArray.add(orignPos.down().north().east().east().east().up());
                BlockArray.add(orignPos.down().north().west().west().west().up());
                BlockArray.add(orignPos.down().north().east().east().east().up().up());
                BlockArray.add(orignPos.down().north().west().west().west().up().up());
                BlockArray.add(orignPos.down().north().east().east().east().up().up().up());
                BlockArray.add(orignPos.down().north().west().west().west().up().up().up());
                BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up());
                BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up());
                BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up().west());
                BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up().east());
                BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up().west().west());
                BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up().east().east());
                BlockArray.add(orignPos.down().north().east().east().east().up().up().up().up().west().west().west());
                BlockArray.add(orignPos.down().north().west().west().west().up().up().up().up().east().east().east());
            }
            
            if (Mode.in("Swastika")) {
                switch (PlayerUtil.GetFacing()) {
                    case East:
                        interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();
                        BlockArray.add(interpPos);
                        BlockArray.add(interpPos.north());
                        BlockArray.add(interpPos.north().north());
                        BlockArray.add(interpPos.up());
                        BlockArray.add(interpPos.up().up());
                        BlockArray.add(interpPos.up().up().north());
                        BlockArray.add(interpPos.up().up().north().north());
                        BlockArray.add(interpPos.up().up().north().north().up());
                        BlockArray.add(interpPos.up().up().north().north().up().up());
                        BlockArray.add(interpPos.up().up().south());
                        BlockArray.add(interpPos.up().up().south().south());
                        BlockArray.add(interpPos.up().up().south().south().down());
                        BlockArray.add(interpPos.up().up().south().south().down().down());
                        BlockArray.add(interpPos.up().up().up());
                        BlockArray.add(interpPos.up().up().up().up());
                        BlockArray.add(interpPos.up().up().up().up().south());
                        BlockArray.add(interpPos.up().up().up().up().south().south());
                        break;
                    case North:
                        interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();
                        BlockArray.add(interpPos);
                        BlockArray.add(interpPos.west());
                        BlockArray.add(interpPos.west().west());
                        BlockArray.add(interpPos.up());
                        BlockArray.add(interpPos.up().up());
                        BlockArray.add(interpPos.up().up().west());
                        BlockArray.add(interpPos.up().up().west().west());
                        BlockArray.add(interpPos.up().up().west().west().up());
                        BlockArray.add(interpPos.up().up().west().west().up().up());
                        BlockArray.add(interpPos.up().up().east());
                        BlockArray.add(interpPos.up().up().east().east());
                        BlockArray.add(interpPos.up().up().east().east().down());
                        BlockArray.add(interpPos.up().up().east().east().down().down());
                        BlockArray.add(interpPos.up().up().up());
                        BlockArray.add(interpPos.up().up().up().up());
                        BlockArray.add(interpPos.up().up().up().up().east());
                        BlockArray.add(interpPos.up().up().up().up().east().east());
                        break;
                    case South:
                        interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();
                        BlockArray.add(interpPos);
                        BlockArray.add(interpPos.east());
                        BlockArray.add(interpPos.east().east());
                        BlockArray.add(interpPos.up());
                        BlockArray.add(interpPos.up().up());
                        BlockArray.add(interpPos.up().up().east());
                        BlockArray.add(interpPos.up().up().east().east());
                        BlockArray.add(interpPos.up().up().east().east().up());
                        BlockArray.add(interpPos.up().up().east().east().up().up());
                        BlockArray.add(interpPos.up().up().west());
                        BlockArray.add(interpPos.up().up().west().west());
                        BlockArray.add(interpPos.up().up().west().west().down());
                        BlockArray.add(interpPos.up().up().west().west().down().down());
                        BlockArray.add(interpPos.up().up().up());
                        BlockArray.add(interpPos.up().up().up().up());
                        BlockArray.add(interpPos.up().up().up().up().west());
                        BlockArray.add(interpPos.up().up().up().up().west().west());
                        break;
                    case West:
                        interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();
                        BlockArray.add(interpPos);
                        BlockArray.add(interpPos.south());
                        BlockArray.add(interpPos.south().south());
                        BlockArray.add(interpPos.up());
                        BlockArray.add(interpPos.up().up());
                        BlockArray.add(interpPos.up().up().south());
                        BlockArray.add(interpPos.up().up().south().south());
                        BlockArray.add(interpPos.up().up().south().south().up());
                        BlockArray.add(interpPos.up().up().south().south().up().up());
                        BlockArray.add(interpPos.up().up().north());
                        BlockArray.add(interpPos.up().up().north().north());
                        BlockArray.add(interpPos.up().up().north().north().down());
                        BlockArray.add(interpPos.up().up().north().north().down().down());
                        BlockArray.add(interpPos.up().up().up());
                        BlockArray.add(interpPos.up().up().up().up());
                        BlockArray.add(interpPos.up().up().up().up().north());
                        BlockArray.add(interpPos.up().up().up().up().north().north());
                        break;
                    default:
                        break;
                }
            }

        if (Mode.in("Portal")) {

            switch (PlayerUtil.GetFacing()) {
                case East:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();
                    BlockArray.add(interpPos.south());
                    BlockArray.add(interpPos.south().south());
                    BlockArray.add(interpPos);
                    BlockArray.add(interpPos.south().south().up());
                    BlockArray.add(interpPos.south().south().up().up());
                    BlockArray.add(interpPos.south().south().up().up().up());
                    BlockArray.add(interpPos.south().south().up().up().up().up());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down().down());
                    break;
                case North:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();
                    BlockArray.add(interpPos.east());
                    BlockArray.add(interpPos.east().east());
                    BlockArray.add(interpPos);
                    BlockArray.add(interpPos.east().east().up());
                    BlockArray.add(interpPos.east().east().up().up());
                    BlockArray.add(interpPos.east().east().up().up().up());
                    BlockArray.add(interpPos.east().east().up().up().up().up());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down().down());
                    break;
                case South:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();
                    BlockArray.add(interpPos.east());
                    BlockArray.add(interpPos.east().east());
                    BlockArray.add(interpPos);
                    BlockArray.add(interpPos.east().east().up());
                    BlockArray.add(interpPos.east().east().up().up());
                    BlockArray.add(interpPos.east().east().up().up().up());
                    BlockArray.add(interpPos.east().east().up().up().up().up());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down());
                    BlockArray.add(interpPos.east().east().up().up().up().up().west().west().west().down().down().down().down());
                    break;
                case West:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();
                    BlockArray.add(interpPos.south());
                    BlockArray.add(interpPos.south().south());
                    BlockArray.add(interpPos);
                    BlockArray.add(interpPos.south().south().up());
                    BlockArray.add(interpPos.south().south().up().up());
                    BlockArray.add(interpPos.south().south().up().up().up());
                    BlockArray.add(interpPos.south().south().up().up().up().up());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down());
                    BlockArray.add(interpPos.south().south().up().up().up().up().north().north().north().down().down().down().down());
                    break;
                default:
                    break;
            }
        }

        if (Mode.in("Flat")) {

            for (int l_X = -3; l_X <= 3; ++l_X)
                for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                    BlockArray.add(orignPos.down().add(l_X, 0, l_Y));
                }
        }

        if (Mode.in("Cover")) {
            if (p_Pair == null)
                return;

            for (int l_X = -3; l_X < 3; ++l_X)
                for (int l_Y = -3; l_Y < 3; ++l_Y) {
                    int l_Tries = 5;
                    BlockPos l_Pos = orignPos.down().add(l_X, 0, l_Y);

                    if (mc.world.getBlockState(l_Pos).getBlock() == p_Pair.getSecond() || mc.world.getBlockState(l_Pos.down()).getBlock() == Blocks.AIR || mc.world.getBlockState(l_Pos.down()).getBlock() == p_Pair.getSecond())
                        continue;

                    while (mc.world.getBlockState(l_Pos).getBlock() != Blocks.AIR && mc.world.getBlockState(l_Pos).getBlock() != Blocks.FIRE) {
                        if (mc.world.getBlockState(l_Pos).getBlock() == p_Pair.getSecond())
                            break;

                        l_Pos = l_Pos.up();

                        if (--l_Tries <= 0)
                            break;
                    }

                    BlockArray.add(l_Pos);
                }
        }
        
        if (Mode.in("Tower")) {
            BlockArray.add(orignPos.up());
            BlockArray.add(orignPos);
            BlockArray.add(orignPos.down());
        }

        if (Mode.in("Wall")) {

            switch (PlayerUtil.GetFacing()) {
                case East:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();

                    for (int l_X = -3; l_X <= 3; ++l_X) {
                        for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                            BlockArray.add(interpPos.add(0, l_Y, l_X));
                        }
                    }
                    break;
                case North:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();

                    for (int l_X = -3; l_X <= 3; ++l_X) {
                        for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                            BlockArray.add(interpPos.add(l_X, l_Y, 0));
                        }
                    }
                    break;
                case South:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();

                    for (int l_X = -3; l_X <= 3; ++l_X) {
                        for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                            BlockArray.add(interpPos.add(l_X, l_Y, 0));
                        }
                    }
                    break;
                case West:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();

                    for (int l_X = -3; l_X <= 3; ++l_X) {
                        for (int l_Y = -3; l_Y <= 3; ++l_Y) {
                            BlockArray.add(interpPos.add(0, l_Y, l_X));
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if (Mode.in("HighwayWall")) {
            switch (PlayerUtil.GetFacing()) {
                case East:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).east().east();

                    for (int l_X = -2; l_X <= 3; ++l_X) {
                        for (int l_Y = 0; l_Y < 3; ++l_Y) {
                            BlockArray.add(interpPos.add(0, l_Y, l_X));
                        }
                    }
                    break;
                case North:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).north().north();

                    for (int l_X = -2; l_X <= 3; ++l_X) {
                        for (int l_Y = 0; l_Y < 3; ++l_Y) {
                            BlockArray.add(interpPos.add(l_X, l_Y, 0));
                        }
                    }
                    break;
                case South:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).south().south();

                    for (int l_X = -2; l_X <= 3; ++l_X) {
                        for (int l_Y = 0; l_Y < 3; ++l_Y) {
                            BlockArray.add(interpPos.add(l_X, l_Y, 0));
                        }
                    }
                    break;
                case West:
                    interpPos = new BlockPos(pos.x, pos.y, pos.z).west().west();

                    for (int l_X = -2; l_X <= 3; ++l_X) {
                        for (int l_Y = 0; l_Y < 3; ++l_Y) {
                            BlockArray.add(interpPos.add(0, l_Y, l_X));
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if (Mode.in("Stair")) {

            interpPos = orignPos.down();

            switch (PlayerUtil.GetFacing()) {
                case East:
                    BlockArray.add(interpPos.east());
                    BlockArray.add(interpPos.east().up());
                    break;
                case North:
                    BlockArray.add(interpPos.north());
                    BlockArray.add(interpPos.north().up());
                    break;
                case South:
                    BlockArray.add(interpPos.south());
                    BlockArray.add(interpPos.south().up());
                    break;
                case West:
                    BlockArray.add(interpPos.west());
                    BlockArray.add(interpPos.west().up());
                    break;
                default:
                    break;
            }
        }
    }

    private Pair2<Integer, Block> findStackHotbar() {
        if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBlock)
            return new Pair2<>(mc.player.inventory.currentItem, ((ItemBlock) mc.player.getHeldItemMainhand().getItem()).getBlock());

        for (int i = 0; i < 9; i++)
        {
            final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBlock)
            {
                final ItemBlock block = (ItemBlock) stack.getItem();

                return new Pair2<>(i, block.getBlock());
            }
        }
        return null;
    }

    public Vec3d GetCenter(double posX, double posY, double posZ)
    {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D ;

        return new Vec3d(x, y, z);
    }

    /// Verifies the array is all obsidian
    private boolean VerifyPortalFrame(ArrayList<BlockPos> p_Blocks)
    {
        for (BlockPos l_Pos : p_Blocks)
        {
            IBlockState l_State = mc.world.getBlockState(l_Pos);

            if (l_State == null || !(l_State.getBlock() instanceof BlockObsidian))
                return false;
        }

        return true;
    }
}
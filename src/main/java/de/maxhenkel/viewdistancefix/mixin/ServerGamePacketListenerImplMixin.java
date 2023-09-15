package de.maxhenkel.viewdistancefix.mixin;

import com.mojang.authlib.GameProfile;
import de.maxhenkel.viewdistancefix.ViewDistanceFix;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    @Final
    protected MinecraftServer server;

    @Redirect(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;Z)V"))
    private void injected(Connection connection, Packet<?> packet, PacketSendListener packetSendListener, boolean bl) {
        if (packet instanceof ClientboundSetChunkCacheRadiusPacket) {
            connection.send(new ClientboundSetChunkCacheRadiusPacket(Math.max(server.getPlayerList().getViewDistance(), ViewDistanceFix.distances.getOrDefault(getOwner().getId(), server.getPlayerList().getViewDistance()))), packetSendListener, bl);
        } else {
            connection.send(packet, packetSendListener, bl);
        }
    }

    @Shadow
    public abstract GameProfile getOwner();

}

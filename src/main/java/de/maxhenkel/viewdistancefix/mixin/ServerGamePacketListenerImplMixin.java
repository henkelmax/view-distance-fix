package de.maxhenkel.viewdistancefix.mixin;

import de.maxhenkel.viewdistancefix.ViewDistanceFix;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Shadow
    @Final
    private MinecraftServer server;

    @Redirect(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V"))
    private void injected(Connection connection, Packet<?> p, @Nullable GenericFutureListener<? extends Future<? super Void>> listener) {
        if (p instanceof ClientboundSetChunkCacheRadiusPacket) {
            connection.send(new ClientboundSetChunkCacheRadiusPacket(Math.max(server.getPlayerList().getViewDistance(), ViewDistanceFix.distances.getOrDefault(player.getUUID(), server.getPlayerList().getViewDistance()))), listener);
        } else {
            connection.send(p, listener);
        }
    }
}

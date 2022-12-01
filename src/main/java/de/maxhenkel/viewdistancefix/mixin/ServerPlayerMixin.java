package de.maxhenkel.viewdistancefix.mixin;

import com.mojang.authlib.GameProfile;
import de.maxhenkel.viewdistancefix.ViewDistanceFix;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    @Shadow
    public ServerGamePacketListenerImpl connection;

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @Inject(method = "updateOptions", at = @At("HEAD"))
    private void updateOptions(ServerboundClientInformationPacket packet, CallbackInfo cir) {
        ViewDistanceFix.distances.put(getUUID(), packet.viewDistance());
        connection.send(new ClientboundSetChunkCacheRadiusPacket(packet.viewDistance()));
    }

}

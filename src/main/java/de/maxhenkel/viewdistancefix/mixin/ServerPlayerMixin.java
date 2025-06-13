package de.maxhenkel.viewdistancefix.mixin;

import com.mojang.authlib.GameProfile;
import de.maxhenkel.viewdistancefix.ViewDistanceFix;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {

    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "updateOptions", at = @At("HEAD"))
    private void updateOptions(ClientInformation clientInformation, CallbackInfo cir) {
        ViewDistanceFix.distances.put(getUUID(), clientInformation.viewDistance());
    }

}

package de.maxhenkel.viewdistancefix;

import de.maxhenkel.viewdistancefix.config.ServerConfig;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ViewDistanceFix implements DedicatedServerModInitializer {

    public static final String MODID = "viewdistancefix";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ServerConfig SERVER_CONFIG;

    public static Map<UUID, Integer> distances = new HashMap<>();

    @Override
    public void onInitializeServer() {
        // SERVER_CONFIG = ConfigBuilder.build(FabricLoader.getInstance().getConfigDir().resolve(MODID).resolve("%s-server.properties".formatted(MODID)), ServerConfig::new);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            Integer distance = distances.get(handler.player.getUUID());
            if (distance != null) {
                handler.player.connection.send(new ClientboundSetChunkCacheRadiusPacket(distance));
            }
        });
    }

}

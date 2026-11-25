package dev.architectstudio;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ArchitectStudio.MOD_ID)
public final class ArchitectStudio {
    public static final String MOD_ID = "architectstudio";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ArchitectStudio(IEventBus modEventBus) {
        LOGGER.info("ArchitectStudio is starting on distribution: {}", FMLEnvironment.dist);
    }
}

package dev.architectstudio.client;

import dev.architectstudio.ArchitectStudio;
import dev.architectstudio.client.screen.ArchitectStudioScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@Mod.EventBusSubscriber(
        modid = ArchitectStudio.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.GAME,
        value = Dist.CLIENT
)
public final class ClientGameEvents {
    private ClientGameEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        while (ClientModEvents.OPEN_EDITOR.consumeClick()) {
            if (minecraft.player != null && minecraft.screen == null) {
                minecraft.setScreen(new ArchitectStudioScreen());
            }
        }
    }
}

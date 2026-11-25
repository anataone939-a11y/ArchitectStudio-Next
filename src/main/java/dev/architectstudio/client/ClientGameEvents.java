package dev.architectstudio.client;

import dev.architectstudio.client.screen.ArchitectStudioScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public final class ClientGameEvents {
    private ClientGameEvents() {
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        while (ClientModEvents.OPEN_EDITOR.consumeClick()) {
            if (minecraft.player != null && minecraft.screen == null) {
                minecraft.setScreen(new ArchitectStudioScreen());
            }
        }
    }
}

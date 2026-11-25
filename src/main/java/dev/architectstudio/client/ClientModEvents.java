package dev.architectstudio.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public final class ClientModEvents {
    public static final KeyMapping OPEN_EDITOR = new KeyMapping(
            "key.architectstudio.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "key.categories.architectstudio"
    );

    private ClientModEvents() {
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_EDITOR);
    }
}

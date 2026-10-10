package com.xmod_devx.fastpleacex.client;

import com.xmod_devx.fastpleacex.config.FastPleaceXConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

/** Client-Einstiegspunkt für FastPleaceX. */
public final class FastPleaceXClient implements ClientModInitializer {
    private static final FastPleaceXConfig CONFIG = FastPleaceXConfig.get();
    private static KeyBinding settingsKey;
    private static int cooldown;

    @Override
    public void onInitializeClient() {
        CONFIG.load();
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastpleacex.open_settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.fastpleacex"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(FastPleaceXClient::tick);
    }

    private static void tick(MinecraftClient client) {
        while (settingsKey.wasPressed()) {
            client.setScreen(new FastPleaceXSettingsScreen(client.currentScreen));
        }

        if (cooldown > 0) cooldown--;
        if (!CONFIG.enabled || client.player == null || client.interactionManager == null
                || client.currentScreen != null || !client.options.useKey.isPressed()
                || client.crosshairTarget == null
                || client.crosshairTarget.getType() != HitResult.Type.BLOCK) {
            return;
        }

        if (!(client.crosshairTarget instanceof BlockHitResult blockHit)) return;
        ItemStack held = client.player.getMainHandStack();
        if (held.isEmpty() || cooldown > 0 || client.player.isUsingItem()) return;

        // Dies nutzt ausschließlich die normale Client-Interaktion. Der Server entscheidet,
        // ob und wie schnell der Block platziert wird; keine Limits oder Prüfungen werden umgangen.
        ActionResult result = client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
        if (result.isAccepted()) {
            cooldown = CONFIG.delayTicks;
        }
    }
}

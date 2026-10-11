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
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

/** Client-Einstiegspunkt für FastPleaceX. */
public final class FastPleaceXClient implements ClientModInitializer {
    private static final FastPleaceXConfig CONFIG = FastPleaceXConfig.get();
    private static final KeyBinding.Category KEY_CATEGORY =
            KeyBinding.Category.create(Identifier.of("fastpleacex", "main"));
    private static KeyBinding settingsKey;
    private static KeyBinding blacklistKey;
    private static KeyBinding toggleKey;
    private static long nextPlacementNanos;

    @Override
    public void onInitializeClient() {
        CONFIG.load();
        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastpleacex.open_settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY));
        blacklistKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastpleacex.open_blacklist", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY));
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fastpleacex.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, KEY_CATEGORY));
        ClientTickEvents.END_CLIENT_TICK.register(FastPleaceXClient::tick);
    }

    private static void tick(MinecraftClient client) {
        while (settingsKey.wasPressed()) client.setScreen(new FastPleaceXSettingsScreen(client.currentScreen));
        while (blacklistKey.wasPressed()) client.setScreen(new FastPleaceXBlacklistScreen(client.currentScreen));
        while (toggleKey.wasPressed()) {
            CONFIG.enabled = !CONFIG.enabled;
            CONFIG.save();
            if (!CONFIG.enabled) nextPlacementNanos = 0L;
        }

        if (!CONFIG.enabled || client.player == null || client.interactionManager == null
                || client.currentScreen != null || !client.options.useKey.isPressed()
                || client.crosshairTarget == null || client.crosshairTarget.getType() != HitResult.Type.BLOCK) return;
        if (!(client.crosshairTarget instanceof BlockHitResult blockHit)) return;

        ItemStack held = client.player.getMainHandStack();
        if (held.isEmpty() || CONFIG.isBlacklisted(held) || client.player.isUsingItem()) return;

        long now = System.nanoTime();
        if (now < nextPlacementNanos) return;

        // Normale Client-Interaktion: Der Server entscheidet weiterhin über Reichweite, Cooldowns und Regeln.
        ActionResult result = client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, blockHit);
        if (result.isAccepted()) {
            long delayNanos = Math.round(CONFIG.delayMs * 1_000_000.0);
            nextPlacementNanos = now + delayNanos;
        }
    }
}

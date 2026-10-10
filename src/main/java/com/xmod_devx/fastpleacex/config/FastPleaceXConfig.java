package com.xmod_devx.fastpleacex.config;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/** Persistente Konfiguration ohne zusätzliche Mod-Abhängigkeit. */
public final class FastPleaceXConfig {
    public static final double MIN_DELAY_TICKS = 0.0;
    public static final double MAX_DELAY_TICKS = 4.0;
    private static final String DEFAULT_BLACKLIST = "minecraft:obsidian,minecraft:respawn_anchor";
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("fastpleacex.properties");
    private static final FastPleaceXConfig INSTANCE = new FastPleaceXConfig();

    public boolean enabled = true;
    /** Verzögerung in Ticks; die GUI stellt Zehntelschritte zwischen 0,0 und 4,0 dar. */
    public double delayTicks = 0.0;
    public final Set<String> blacklist = new LinkedHashSet<>();

    private FastPleaceXConfig() {
        resetBlacklistToDefaults();
    }

    public static FastPleaceXConfig get() {
        return INSTANCE;
    }

    public void resetBlacklistToDefaults() {
        blacklist.clear();
        blacklist.add("minecraft:obsidian");
        blacklist.add("minecraft:respawn_anchor");
    }

    public boolean isBlacklisted(ItemStack stack) {
        return stack.getItem() instanceof BlockItem
                && blacklist.contains(Registries.ITEM.getId(stack.getItem()).toString());
    }

    public void load() {
        if (!Files.exists(FILE)) return;
        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(FILE)) {
            properties.load(reader);
            enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));
            delayTicks = clamp(Double.parseDouble(properties.getProperty("delayTicks", "0.0")));
            blacklist.clear();
            String saved = properties.getProperty("blacklist", DEFAULT_BLACKLIST);
            for (String id : saved.split(",")) {
                String normalized = id.trim().toLowerCase(Locale.ROOT);
                if (!normalized.isEmpty()) blacklist.add(normalized);
            }
        } catch (IOException | NumberFormatException ignored) {
            // Bei einer beschädigten Datei bleiben die sicheren Standardwerte aktiv.
        }
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        properties.setProperty("delayTicks", String.format(Locale.ROOT, "%.1f", clamp(delayTicks)));
        properties.setProperty("blacklist", String.join(",", blacklist));
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) {
                properties.store(writer, "FastPleaceX configuration by Xmod_devX");
            }
        } catch (IOException ignored) {
            // Ein fehlgeschlagenes Speichern darf das Spiel nicht beenden.
        }
    }

    public static double clamp(double value) {
        return Math.max(MIN_DELAY_TICKS, Math.min(MAX_DELAY_TICKS, value));
    }
}

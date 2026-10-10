package com.xmod_devx.fastpleacex.config;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Persistente, bewusst kleine Konfiguration ohne zusätzliche Mod-Abhängigkeit. */
public final class FastPleaceXConfig {
    public static final int MIN_DELAY_TICKS = 0;
    public static final int MAX_DELAY_TICKS = 4;

    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("fastpleacex.properties");
    private static final FastPleaceXConfig INSTANCE = new FastPleaceXConfig();

    public boolean enabled = true;
    public int delayTicks = 0;

    private FastPleaceXConfig() {
    }

    public static FastPleaceXConfig get() {
        return INSTANCE;
    }

    public void load() {
        if (!Files.exists(FILE)) return;
        Properties properties = new Properties();
        try (var reader = Files.newBufferedReader(FILE)) {
            properties.load(reader);
            enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));
            delayTicks = clamp(Integer.parseInt(properties.getProperty("delayTicks", "0")));
        } catch (IOException | NumberFormatException ignored) {
            // Bei einer beschädigten Datei bleiben die sicheren Standardwerte aktiv.
        }
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        properties.setProperty("delayTicks", Integer.toString(clamp(delayTicks)));
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) {
                properties.store(writer, "FastPleaceX configuration by Xmod_devX");
            }
        } catch (IOException ignored) {
            // Ein fehlgeschlagenes Speichern darf das Spiel nicht beenden.
        }
    }

    public static int clamp(int value) {
        return Math.max(MIN_DELAY_TICKS, Math.min(MAX_DELAY_TICKS, value));
    }
}

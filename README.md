# FastPleaceX

Fabric-Client-Mod für Minecraft **1.21.1** von **Xmod_devX**.

## Funktionen

- Hält die rechte Maustaste gedrückt, um die normale Block-Interaktion wiederholt anzustoßen.
- Einstellungsmenü mit `G`.
- Aktivierung und Platzierintervall (0–4 Ticks) konfigurierbar.
- Konfiguration wird unter `.minecraft/config/fastpleacex.properties` gespeichert.

Die Mod verwendet ausschließlich die normale Fabric-/Minecraft-Interaktion. Serverregeln, Vanilla-Cooldowns, Reichweiten- und Anti-Cheat-Prüfungen werden **nicht** umgangen. Verwende sie nur dort, wo schnelle Platzierung erlaubt ist.

## Bauen

Voraussetzungen: Java 21 und Internetzugriff für Gradle-Abhängigkeiten.

```bash
./gradlew build
```

Das fertige JAR liegt anschließend unter `build/libs/fastpleacex-1.0.0.jar`.

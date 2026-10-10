# FastPleaceX

Fabric-Client-Mod für Minecraft **1.21.1** von **Xmod_devX**.

## Funktionen

- Hält die rechte Maustaste gedrückt, um die normale Block-Interaktion wiederholt anzustoßen.
- Einstellungsmenü mit `G`.
- Aktivierung und Platzierintervall von 0–4 Ticks konfigurierbar.
- Block-Blacklist mit eigenem Menü auf `K`.
- Standardmäßig geschützt: `minecraft:obsidian` und `minecraft:respawn_anchor`.
- Im Blacklist-Menü können Einträge durch Anklicken entfernt oder der aktuell gehaltene Block hinzugefügt werden.
- Konfiguration wird unter `.minecraft/config/fastpleacex.properties` gespeichert.

Die Mod verwendet ausschließlich die normale Fabric-/Minecraft-Interaktion. Serverregeln, Vanilla-Cooldowns, Reichweiten- und Anti-Cheat-Prüfungen werden **nicht** umgangen. Verwende sie nur dort, wo schnelle Platzierung erlaubt ist.

## Installation

1. Installiere **Java 21**.
2. Installiere **Fabric Loader für Minecraft 1.21.1** und die passende **Fabric API**.
3. Lade `fastpleacex-1.0.0.jar` aus den Releases bzw. aus dem Build-Verzeichnis herunter.
4. Kopiere die JAR in den Minecraft-Ordner `mods`:
   - Windows: `%APPDATA%\\.minecraft\\mods`
   - Linux: `~/.minecraft/mods`
   - macOS: `~/Library/Application Support/minecraft/mods`
5. Starte Minecraft mit dem Fabric-1.21.1-Profil.

## Bedienung

- `G`: Allgemeine FastPleaceX-Einstellungen öffnen.
- `K`: Blacklist öffnen.
- Rechte Maustaste halten: Schnellplatzieren, sofern die Funktion aktiv ist und der gehaltene Block nicht auf der Blacklist steht.
- Einträge im Blacklist-Menü anklicken, um sie zu entfernen; mit **Block aus Hand hinzufügen** wird der aktuell gehaltene Block eingetragen.

## Bauen aus dem Quellcode

Voraussetzungen: Java 21 und Internetzugriff für Gradle-Abhängigkeiten.

```bash
./gradlew build
```

Unter Windows:

```bat
gradlew.bat build
```

Das fertige JAR liegt anschließend unter `build/libs/fastpleacex-1.0.0.jar`.

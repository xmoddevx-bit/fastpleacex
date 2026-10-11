# FastPleaceX-Blacklist anpassen

Die Blacklist verhindert, dass FastPleaceX bestimmte BlockItems automatisch anstößt. Standardmäßig sind `minecraft:obsidian` und `minecraft:respawn_anchor` geschützt.

## Blacklist öffnen

1. Starte Minecraft mit Fabric und FastPleaceX.
2. Drücke im Spiel `K`, oder öffne mit `G` die FastPleaceX-Einstellungen und wähle **Blacklist öffnen**.
3. Die Blacklist zeigt alle derzeit geschützten Blocktypen. Klicke einen Eintrag an, um ihn zu entfernen.

## Jeden Minecraft-Block auswählen

1. Öffne die Blacklist.
2. Klicke auf **Block auswählen**.
3. Der Picker enthält alle registrierten `BlockItem`s der laufenden Minecraft-Installation, alphabetisch nach ihrer vollständigen ID sortiert. Dadurch sind Vanilla-Blöcke von `minecraft:grass_block` bis `minecraft:bedrock` erreichbar.
4. Nutze das Suchfeld, zum Beispiel `obsidian`, `respawn_anchor`, `grass` oder `bedrock`.
5. Ein Eintrag mit `+` wird durch Anklicken zur Blacklist hinzugefügt. Ein Eintrag mit `✓` ist bereits geschützt; ein erneuter Klick entfernt ihn.
6. Mit `<` und `>` wechselst du durch alle Seiten. Die Auswahl wird sofort gespeichert.
7. Mit **Zurück** kehrst du zur Blacklist zurück.

## Beispiele

- **Obsidian schützen:** Suche nach `obsidian` und klicke `minecraft:obsidian` an.
- **Anker schützen:** Suche nach `respawn_anchor` und klicke `minecraft:respawn_anchor` an.
- **Gras schützen:** Suche nach `grass_block` und wähle `minecraft:grass_block`.
- **Bedrock schützen:** Suche nach `bedrock` und wähle `minecraft:bedrock`.

## Standardwerte wiederherstellen

In der Blacklist-GUI setzt **Standard wiederherstellen** die Liste auf Obsidian und Respawn-Anker zurück. Die Änderung wird sofort in `.minecraft/config/fastpleacex.properties` gespeichert.

## HUD und Verzögerung

Der HUD-Indikator oben links zeigt:

- `OFF`: FastPleaceX ist deaktiviert.
- `ON`: FastPleaceX ist aktiv, aber rechte Maustaste wird nicht gehalten oder kein gültiger Block ist im Fadenkreuz.
- `ACTIVE`: FastPleaceX kann in diesem Tick platzieren.

Die Verzögerung wird in Millisekunden angezeigt. Der Client-Tick bleibt jedoch die technische Ausführungsgrenze: Der normale Fabric-Tick läuft typischerweise mit 20 Ticks pro Sekunde. Werte unter etwa 50 ms können daher nicht garantieren, dass mehr Platzieraktionen pro Sekunde ausgeführt werden.

`P` schaltet FastPleaceX direkt an oder aus. Server-Cooldowns, Reichweite, Anti-Cheat-Regeln und serverseitige Interaktionslimits werden nicht umgangen. Ob eine bestimmte Einstellung auf einem Server erlaubt oder reibungslos ist, hängt von dessen Regeln und Plugin-Konfiguration ab.

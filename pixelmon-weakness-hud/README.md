# Pixelmon Weakness HUD (1.16.5)

Client-side Forge mod for Minecraft 1.16.5 that displays type weakness icons next to the enemy Pokémon name while in a Pixelmon battle.

- Minecraft: 1.16.5
- Forge: 36.2.x
- Pixelmon: 9.1.x (tested against 9.1.7)

This mod does NOT bundle Pixelmon API. It uses reflection to detect the battle screen and to read enemy types.

## Build

```bash
./gradlew build
```

The jar will be at `build/libs/pixelmon_weakness_hud-<version>.jar`.

## Install

- Put the built jar into your `mods` folder alongside Pixelmon.
- Optional: add a texture atlas with type icons at `assets/pixelmon_weakness_hud/textures/gui/types.png` (256x256 grid, 16px cells). See `assets/.../textures/gui/README.txt` for details.

## Notes

- Positions are heuristic to sit near the top-left enemy info. You can adjust offsets in `ClientOverlayRenderer` (`baseX`, `baseY`).
- If Pixelmon changes GUI class names, detection may fail; the overlay simply won’t draw rather than crash.
- Weakness set shows any attack type with ≥2x effectiveness against the target’s combined types.
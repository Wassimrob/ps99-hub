# PM Battle Info Plus (MC 1.16.5 / Pixelmon 9.1.13)

- Only for Minecraft 1.16.5 and Pixelmon 9.1.13
- Hold Left Alt in battle to show predicted damage % for each move against the current opponent

Build:
- Java 8
- `./gradlew build`
- Place the built jar into your mods folder along with Pixelmon 9.1.13

Notes:
- Uses simplified damage calculation (STAB, effectiveness, burn). Items, abilities, weather, screens, and many niche effects are not yet modeled.
- If Pixelmon classes change, you may need to adjust API calls in `PixelmonDamageService`.
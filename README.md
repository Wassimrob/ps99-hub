# Pixelmon Smash MC - Random Tournament + UTM (Android)

This Android app (Kotlin + Jetpack Compose) provides:

- Random Tournament: quick single-elimination bracket generator (Smash MC-style randomization)
- UTM (Team Manager): simple team creation and 6-member roster editor using placeholder species data

Note: UI is inspired, not using any trademarked assets/names. Species are placeholders.

## Build (local)

Prereqs:
- JDK 17 installed and set as default (`JAVA_HOME` pointing to JDK 17)
- Android SDK with platform 34 and Build Tools 34+ (or open in Android Studio Hedgehog/Koala)

Steps:
1. Clone or open this project.
2. From a terminal in the project root:
   ```bash
   ./gradlew assembleDebug
   ```
3. The APK will be at:
   `app/build/outputs/apk/debug/app-debug.apk`

## Run

Install the APK on a device or emulator running Android 7.0 (API 24) or newer.

## Features

- Tournament
  - Enter participants, generate bracket with BYEs to next power-of-two
  - Visualize rounds and pairings
- UTM
  - Create teams, add up to six members from sample species

## Roadmap
- Match results and advancement logic
- CSV/JSON import/export for teams
- Custom themes and icons
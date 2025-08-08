package com.example.pmbattleinfo;

import net.minecraft.client.Minecraft;

public class DamageHudProvider {
    public static String[] getDamageOverlayLines() {
        try {
            return PixelmonDamageService.describePredictedDamage();
        } catch (Throwable t) {
            return new String[]{
                "PMBI+: Hold Alt to show dmg%",
                "No battle or Pixelmon API unavailable"
            };
        }
    }
}
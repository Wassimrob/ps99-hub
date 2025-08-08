package com.example.pixelmonweaknesshud;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PixelmonWeaknessHud.MOD_ID)
public class PixelmonWeaknessHud {
    public static final String MOD_ID = "pixelmon_weakness_hud";

    public PixelmonWeaknessHud() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientOnly::init);
    }

    private static class ClientOnly implements DistExecutor.SafeRunnable {
        static void init() {
            // Force class load to register static event subscribers
            ClientOverlayRenderer.ensureRegistered();
        }

        @Override
        public void run() {
            init();
        }
    }
}
package com.wassim.crashmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod("wassimcrashmod")
public class WassimCrashMod {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    
    // The two specific keys that will trigger the crash
    private static KeyBinding crashKey1;
    private static KeyBinding crashKey2;
    
    public WassimCrashMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Wassim's Random Mod initialized!");
        LogSimulator.simulateSystemLogs();
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        // Register the two crash keys
        crashKey1 = new KeyBinding("key.wassim.crash1", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F9, "key.categories.wassim");
        crashKey2 = new KeyBinding("key.wassim.crash2", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_F10, "key.categories.wassim");
        
        ClientRegistry.registerKeyBinding(crashKey1);
        ClientRegistry.registerKeyBinding(crashKey2);
        
        LOGGER.info("Wassim's crash keys registered: F9 and F10");
    }
    
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getInstance().player == null) return;
        
        // Check if both keys are pressed simultaneously
        if (crashKey1.isDown() && crashKey2.isDown()) {
            LOGGER.info("Wassim's mod activated - preparing for random crash...");
            
            // Random chance to use different crash methods
            if (RANDOM.nextInt(10) < 3) {
                // 30% chance for memory leak crash
                LOGGER.warn("Memory leak simulation initiated");
                CrashHandler.simulateMemoryLeak();
            } else {
                // 70% chance for random crash
                CrashHandler.triggerRandomCrash();
            }
        }
    }
}
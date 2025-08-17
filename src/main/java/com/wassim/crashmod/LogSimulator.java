package com.wassim.crashmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class LogSimulator {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    
    public static void simulateSystemLogs() {
        // Simulate system startup logs
        LOGGER.info("Initializing Wassim's Random Mod v1.0.0");
        LOGGER.info("Loading mod components...");
        LOGGER.info("Registering event handlers...");
        LOGGER.info("Mod initialization complete");
        
        // Simulate periodic system logs
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(RANDOM.nextInt(30000) + 15000); // 15-45 seconds
                    
                    String[] systemMessages = {
                        "Performing routine memory cleanup...",
                        "Checking system integrity...",
                        "Updating mod state...",
                        "Synchronizing with game engine...",
                        "Validating resource allocations...",
                        "Monitoring performance metrics...",
                        "Running background tasks...",
                        "Checking for updates...",
                        "Optimizing memory usage...",
                        "Validating game state..."
                    };
                    
                    String message = systemMessages[RANDOM.nextInt(systemMessages.length)];
                    LOGGER.debug(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    public static void generateCrashContext() {
        // Generate realistic crash context information
        LOGGER.error("=== CRASH REPORT ===");
        LOGGER.error("Minecraft Version: 1.16.5");
        LOGGER.error("Forge Version: 36.2.39");
        LOGGER.error("Mod Version: 1.0.0");
        LOGGER.error("Java Version: " + System.getProperty("java.version"));
        LOGGER.error("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        LOGGER.error("Memory: " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB total, " + 
                    Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB free");
        LOGGER.error("JVM Flags: " + System.getProperty("java.vm.args", "None"));
        LOGGER.error("Crash Time: " + System.currentTimeMillis());
        LOGGER.error("==================");
    }
    
    public static void simulatePerformanceIssues() {
        // Simulate performance degradation before crash
        LOGGER.warn("Performance degradation detected");
        LOGGER.warn("Frame rate dropping below acceptable levels");
        LOGGER.warn("Memory usage increasing steadily");
        LOGGER.warn("Garbage collection frequency increasing");
        LOGGER.error("System response time degrading");
    }
}
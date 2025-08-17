package com.wassim.crashmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CrashHandler {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    
    public static void triggerRandomCrash() {
        LOGGER.warn("Wassim's mod detected suspicious activity...");
        
        // Simulate performance issues before crash
        LogSimulator.simulatePerformanceIssues();
        
        // Random delay to make it seem more natural
        try {
            Thread.sleep(RANDOM.nextInt(1000) + 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Generate crash context
        LogSimulator.generateCrashContext();
        
        // Generate a random crash reason to make it seem like a real crash
        String[] crashReasons = {
            "OutOfMemoryError: Java heap space",
            "StackOverflowError",
            "ArrayIndexOutOfBoundsException",
            "NullPointerException",
            "ClassCastException",
            "IllegalArgumentException",
            "RuntimeException: Unexpected error occurred",
            "VirtualMachineError: Out of memory",
            "LinkageError: Class loading failed",
            "NoClassDefFoundError: Critical class missing"
        };
        
        String crashReason = crashReasons[RANDOM.nextInt(crashReasons.length)];
        LOGGER.error("Critical error: " + crashReason);
        
        // Force a crash with a random error
        switch (RANDOM.nextInt(5)) {
            case 0:
                // Memory crash simulation
                LOGGER.error("Memory usage exceeded limits");
                LOGGER.error("JVM heap space allocation failed");
                LOGGER.error("System resources depleted");
                throw new OutOfMemoryError("Java heap space");
            case 1:
                // Stack overflow simulation
                LOGGER.error("Stack overflow detected");
                LOGGER.error("Recursive call depth exceeded");
                LOGGER.error("Stack frame allocation failed");
                throw new StackOverflowError("Stack overflow in wassim's mod");
            case 2:
                // Null pointer simulation
                LOGGER.error("Null reference encountered");
                LOGGER.error("Object dereference failed");
                LOGGER.error("Critical component not initialized");
                throw new NullPointerException("Unexpected null value in wassim's mod");
            case 3:
                // Runtime exception simulation
                LOGGER.error("Runtime exception occurred");
                LOGGER.error("Application state corrupted");
                LOGGER.error("Unrecoverable error detected");
                throw new RuntimeException("Unexpected error in wassim's mod: " + crashReason);
            case 4:
                // Virtual machine error simulation
                LOGGER.error("Virtual machine error");
                LOGGER.error("JVM internal failure");
                LOGGER.error("System integrity compromised");
                throw new VirtualMachineError("Critical JVM failure in wassim's mod") {};
        }
    }
    
    public static void simulateMemoryLeak() {
        LOGGER.warn("Memory usage increasing abnormally...");
        LOGGER.warn("Garbage collection ineffective");
        LOGGER.error("Memory leak detected");
        
        // Try to allocate memory until we crash
        try {
            byte[][] memoryHog = new byte[1000][];
            for (int i = 0; i < 1000; i++) {
                memoryHog[i] = new byte[1024 * 1024]; // 1MB chunks
                if (i % 100 == 0) {
                    LOGGER.warn("Allocated " + (i + 1) + "MB of memory");
                }
            }
        } catch (OutOfMemoryError e) {
            LOGGER.error("Memory allocation failed: " + e.getMessage());
            throw e;
        }
    }
}
#!/bin/bash

echo "Building Wassim's Random Mod..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1-2)
if [[ "$JAVA_VERSION" != "1.8" ]]; then
    echo "Warning: Java version $JAVA_VERSION detected. Java 8 is recommended for Minecraft 1.16.5"
fi

# Make gradlew executable
chmod +x gradlew

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Build the mod
echo "Building mod..."
./gradlew build

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "Mod JAR file created in build/libs/"
    ls -la build/libs/
else
    echo "Build failed!"
    exit 1
fi
#!/bin/bash

echo "🚀 Wassim's Random Mod - Build and Upload Script"
echo "=================================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Error: Java is not installed or not in PATH"
    echo "Please install Java 8 or later and try again"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1-2)
echo "📋 Java version detected: $JAVA_VERSION"

# Check if gradlew exists and is executable
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found"
    exit 1
fi

chmod +x gradlew

echo ""
echo "🔨 Building Wassim's Random Mod..."

# Clean and build
./gradlew clean build

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    
    # Check if JAR was created
    if [ -f "build/libs/wassim-crash-mod-1.0.0.jar" ]; then
        echo "📦 JAR file created successfully!"
        echo "📁 Location: build/libs/wassim-crash-mod-1.0.0.jar"
        
        echo ""
        echo "📤 Uploading to bashupload.com..."
        
        # Upload to bashupload.com
        UPLOAD_URL=$(curl -s -F "file=@build/libs/wassim-crash-mod-1.0.0.jar" https://bashupload.com/)
        
        if [ $? -eq 0 ] && [ ! -z "$UPLOAD_URL" ]; then
            echo ""
            echo "🎉 Upload successful!"
            echo "📥 Download link: $UPLOAD_URL"
            echo ""
            echo "🔗 Share this link with others to download your mod!"
            
            # Save the link to a file
            echo "$UPLOAD_URL" > download_link.txt
            echo "📝 Download link saved to download_link.txt"
            
            # Show file info
            echo ""
            echo "📊 File Information:"
            ls -lh build/libs/wassim-crash-mod-1.0.0.jar
            
        else
            echo "❌ Upload failed!"
            echo "Please try again or check your internet connection."
        fi
        
    else
        echo "❌ JAR file not found after build!"
        echo "Check the build/libs/ directory for any generated files."
    fi
    
else
    echo "❌ Build failed!"
    echo "Please check the error messages above and fix any issues."
    exit 1
fi
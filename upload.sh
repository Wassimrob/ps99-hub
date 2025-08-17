#!/bin/bash

echo "Uploading Wassim's Random Mod to bashupload.com..."

# Check if curl is installed
if ! command -v curl &> /dev/null; then
    echo "Error: curl is not installed. Please install curl first."
    exit 1
fi

# Check if the mod JAR exists
if [ ! -f "build/libs/wassim-crash-mod-1.0.0.jar" ]; then
    echo "Error: Mod JAR file not found. Please build the mod first using ./build.sh"
    exit 1
fi

# Create a zip file with all mod files
echo "Creating distribution package..."
zip -r wassim-crash-mod-1.0.0.zip \
    build/libs/wassim-crash-mod-1.0.0.jar \
    README.md \
    INSTALLATION.md \
    src/ \
    build.gradle \
    gradle.properties \
    settings.gradle

if [ $? -eq 0 ]; then
    echo "Distribution package created successfully!"
    
    # Upload to bashupload.com
    echo "Uploading to bashupload.com..."
    UPLOAD_URL=$(curl -s -F "file=@wassim-crash-mod-1.0.0.zip" https://bashupload.com/)
    
    if [ $? -eq 0 ] && [ ! -z "$UPLOAD_URL" ]; then
        echo ""
        echo "✅ Upload successful!"
        echo "📥 Download link: $UPLOAD_URL"
        echo ""
        echo "Share this link with others to download your mod!"
        
        # Save the link to a file
        echo "$UPLOAD_URL" > download_link.txt
        echo "Download link saved to download_link.txt"
    else
        echo "❌ Upload failed!"
        echo "Please try again or check your internet connection."
    fi
else
    echo "❌ Failed to create distribution package!"
    exit 1
fi
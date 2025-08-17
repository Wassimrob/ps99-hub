#!/bin/bash

echo "Uploading Wassim's Random Mod source code to bashupload.com..."

# Check if curl is installed
if ! command -v curl &> /dev/null; then
    echo "Error: curl is not installed. Please install curl first."
    exit 1
fi

# Create a zip file with source code
echo "Creating source code package..."
zip -r wassim-crash-mod-source.zip \
    src/ \
    build.gradle \
    gradle.properties \
    settings.gradle \
    README.md \
    INSTALLATION.md \
    build.sh \
    upload.sh

if [ $? -eq 0 ]; then
    echo "Source code package created successfully!"
    
    # Upload to bashupload.com
    echo "Uploading to bashupload.com..."
    UPLOAD_URL=$(curl -s -F "file=@wassim-crash-mod-source.zip" https://bashupload.com/)
    
    if [ $? -eq 0 ] && [ ! -z "$UPLOAD_URL" ]; then
        echo ""
        echo "✅ Upload successful!"
        echo "📥 Download link: $UPLOAD_URL"
        echo ""
        echo "Share this link with others to download your mod source code!"
        
        # Save the link to a file
        echo "$UPLOAD_URL" > source_download_link.txt
        echo "Download link saved to source_download_link.txt"
    else
        echo "❌ Upload failed!"
        echo "Please try again or check your internet connection."
    fi
else
    echo "❌ Failed to create source code package!"
    exit 1
fi
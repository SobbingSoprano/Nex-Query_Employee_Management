#!/bin/bash
# Compile and run NexQuery application with MySQL connector

# Set the base directory
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Create bin directory if it doesn't exist
mkdir -p "$DIR/bin"

# Compile Java files
echo "Compiling Java files..."
javac -d "$DIR/bin" -cp "$DIR/lib/*" "$DIR/src/"**/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful. Running application..."
    echo ""
    # Run the application
    java -cp "$DIR/bin:$DIR/lib/*" App
else
    echo "Compilation failed!"
    exit 1
fi

#!/bin/sh

# Remove past log file if it exists
rm -f debug.log

# Run Java program, with arguments
java -Xmx1024m -cp "wikivoyage-listings.jar:build/libs/wikivoyage-listings.jar:lib/*:config/" org.wikivoyage.listings.Main "$@"

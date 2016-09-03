#!/bin/sh

java -Xmx1024m -cp "wikivoyage-listings.jar:build/libs/wikivoyage-listings.jar:lib/*:config/" org.wikivoyage.listings.Main "$@"

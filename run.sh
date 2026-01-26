#!/bin/bash
# Compile the project first
ant compile

# Run the application directly using java
# -cp "build:lib/*" sets the classpath to include compiled classes and all JARs in lib
java -cp "build:lib/*" ua.notion.musiclibrary.Main

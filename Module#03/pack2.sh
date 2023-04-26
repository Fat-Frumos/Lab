#!/bin/bash

# Set the author and version
AUTHOR="Polyak Pavlo"
VERSION="1.0.0"

directories=($(find . -name "*.java" -type f -printf "%h\n" | sort -u))

# Create package-info.java files
for directory in "${directories[@]}"; do
    package_name=$(echo $directory | sed 's/\.\///g' | sed 's/\//\./g')
    package_file="${directory}/package-info.java"
    if [ ! -f $package_file ]; then
        echo "Creating $package_file"
        echo "/**" > $package_file
        echo " * ${AUTHOR}" >> $package_file
        echo " * Version ${VERSION}" >> $package_file
        echo " */" >> $package_file
        echo "package ${package_name};" >> $package_file
    fi
done
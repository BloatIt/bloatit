#!/bin/bash

# Use absolute paths for source and destination directory parameters

SOURCE_WWW=$1
DEST_WWW=$2

BUILD_VERSION_FILE=$3

VERSION=$(head -n 1 "$BUILD_VERSION_FILE" | sed s/project.version=//g)

mkdir -p "$DEST_WWW"

#create existing map
cd "$DEST_WWW"
existingmap="$(find . -type f | sed -r 's#^(.*)-([0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z]+)?)(\.(tar\.)?([a-zA-Z]+))$#\1\4 \0#g')"
   
cd "$SOURCE_WWW"

for line in $(find . -type f)
do

    filename=$(basename $line)
    dirname=$(dirname $line)

    #Create target dir if not exists
    if [ ! -e "$DEST_WWW/$dirname" ]
    then
        mkdir -p "$DEST_WWW/$dirname"
    fi

    #echo "process "$line
    
    matchlist=$(echo "$existingmap" | grep "$line " | cut -d " " -f 2)
    
    #echo "matchlist "$matchlist
    
    lastfile="$(echo "$matchlist" | sort -V | tail -n 1)"
    
    #echo "lastfile "$lastfile
    
    #echo "$(cmp  "$line" "$DEST_WWW/$lastfile" && echo $?)"
    if  [ -z "$lastfile" ] || [ "$(cmp -s "$line" "$DEST_WWW/$lastfile" && echo $?)" != 0 ]
    then
        echo "copy "$line
        cp "$line" "$DEST_WWW/$dirname/$(echo "$filename" |sed -r "s#^(.*)(\\.(tar\\.)?([a-zA-Z]+))\$#\\1-$VERSION\\2#g")"
    fi
done

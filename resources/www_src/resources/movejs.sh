#!/bin/sh
# A small script that takes all JS in directory, for each js, finds the locale (if available)
# then creates a directory for this locale, and move the js to this directory

for file in *.js ; do
    echo "$file"
    extension=${file##*.}
    filename=${file%.*}
    cut=$(echo $filename | grep -v "i18n" | cut -d "-" -f 2 )
    [ -n "$cut" ] && mkdir -p ../../../$cut && cp $file ../../../$cut/"jquery.ui.datepicker.js"
done




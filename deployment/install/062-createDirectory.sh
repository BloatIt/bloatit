#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Create the needed directory for a elveos deamon to works.
----------------------

### Usage 

You must export the USER variable so that this script know which user to use.
(export USER=...)

### Details

Create in the elveos home directory:

 * .upload
 * .local/share/bloatit/lucene
 * .local/share/bloatit/log
 * .config/bloatit
 * www
 * resources

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi

    sudo su -c '
    cd
    mkdir .upload
    mkdir -p .local/share/bloatit/lucene
    mkdir -p .local/share/bloatit/log
    mkdir -p .config/bloatit
    mkdir www
    mkdir resources
    ' $USER

fi

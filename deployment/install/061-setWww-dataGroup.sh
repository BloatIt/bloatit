#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: add the www-data user in the specified user group.
----------------------

### Usage 

You must export the USER variable so that this script know which user to use.
(export USER=...)

### Details

The www-data user will have to read some files into the home of the elveos user.
To set the rights correctly we have to allow the users in the group elveos to read those files, and to add www-data into the elveos group.

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi
    USER="$1"
    sudo addgroup www-data $USER

fi



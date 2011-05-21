#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Create a new user
----------------------

This script just call addUser ...

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi
    sudo adduser $USER 

fi

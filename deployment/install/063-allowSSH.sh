#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Add a ssh key into the authorized key of a user.
----------------------

### Usage 

You must export the USER variable. (export USER=...)

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi

    read -p "Authorize the $USER user to use ssh. Past your public key: " key
    sudo su -c "
    cd 
    mkdir .ssh/
    echo \"$key\" >> ~/.ssh/authorized_keys
    " $USER
fi

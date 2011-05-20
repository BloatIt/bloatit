#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Add a ssh key into the authorized key of a user.
----------------------

### Usage 

    $0 [username]: If *username* is specified then the script is launch for the user *username*

EOF

else
    USER="$1"

    read -p "Authorize the $USER user to use ssh. Past your public key: " key
    sudo su -c "
    cd 
    mkdir .ssh/
    echo \"$key\" >> ~/.ssh/authorized_keys
    " $USER
fi

#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Create a new user
----------------------

### Usage 

    $0 [username]: If *username* is specified then the script is launch for the user *username*

EOF

else
    USER="$1"
    sudo adduser $USER 

fi

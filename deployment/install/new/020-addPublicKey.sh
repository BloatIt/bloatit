#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: Add a public key into the ssh authorized_keys.
EOF

elif [ "$1" = exec ] ; then 

    mkdir ~/.ssh
    read -p "Enter your public key: " _KEY
    echo "$_KEY" >> ~/.ssh/authorized_keys

else 
    echo "Parameter must be empty or 'exec'"
fi

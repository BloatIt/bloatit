#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: create a database user and a new db.
-----------------------------

### Usage

You must export the USER variable so that this script know which user to use.
(export USER=...)

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi
    stty -echo
    read -p "Input the db password for $USER: " PASSWORD ; echo
    stty echo

    sudo su -c " 
cat << EOF | psql -f -
CREATE USER $USER;
CREATE DATABASE $USER OWNER $USER;
ALTER USER $USER WITH ENCRYPTED PASSWORD '$PASSWORD' ;
EOF
    " postgres
fi

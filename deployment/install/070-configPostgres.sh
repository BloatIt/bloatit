#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: Configure the postgresql db
-----------------------------

### Usage

You must export the USER variable so that this script know which user to use.
(export USER=...)

Configure the postgresql db:
- Only local person can connect on the db
- The elveos user can connect on the elveos db with its password (md5).
- The postgres user can connect 
- Every other user are refused.
In fact everything must be done by hand. (It is very hard to make an automatic script...)

### Optimizing postgres

TODO !!

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi
    sudo sed -i -r '/all/ s/md5$/reject/g' /etc/postgresql/8.4/main/pg_hba.conf

    echo "You will have to configure the postgres server yourself ..."
    echo "Copy these lines: (don't forgot the rejected to add...)"
    echo "local   $USER         $USER                          md5" 
    echo "host    $USER         $USER   127.0.0.1/32           md5" 
    read
    sudo vim /etc/postgresql/8.4/main/pg_hba.conf
    sudo service postgresql restart
fi



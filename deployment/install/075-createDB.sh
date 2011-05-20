#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0 [ username password ] :

Create a database user and a db with the same name.

   username: the database user.
   password: the database user's password. Make sur it is strong enougth (10 chars).

EOF

else
    if [ -z "$2" ] ; then
        echo Second parameter must be the password. Abording.
        exit
    fi
    PASSWORD=$1
    USER=$2
    sudo su -c " 
cat << EOF | psql -f -
CREATE USER $USER;
CREATE DATABASE $USER OWNER $USER;
ALTER USER $USER WITH ENCRYPTED PASSWORD '$PASSWORD' ;
EOF
    " postgres
fi

#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0 [ username ] :

Configure the postgresql db:
- Only local person can connect on the db
- The elveos user can connect on the elveos db with its password (md5).
- The postgres user can connect 
- Every other user are refused.
In fact everything must be done by hand. (It is very hard to make an automatic script...)

if a username is specified this script is executed.
EOF

else
    USER=$1
    sudo sed -i -r '/all/ s/md5$/reject/g' /etc/postgresql/8.4/main/pg_hba.conf

    echo "You will have to configure the postgres server yourself ..."
    echo "Copy these lines: (don't forgot the rejected to add...)"
    echo "local   $1         $1                          md5" 
    echo "host    $1         $1   127.0.0.1/32           md5" 
    read
    sudo vim /etc/postgresql/8.4/main/pg_hba.conf
    sudo service postgresql restart
fi

add_database(){
PASSWORD=$1
USER=$2
sudo su -c " 
cat << EOF | psql -f -
CREATE USER $USER;
CREATE DATABASE $USER OWNER $USER;
ALTER USER $USER WITH ENCRYPTED PASSWORD '$PASSWORD' ;
EOF
" postgres
}


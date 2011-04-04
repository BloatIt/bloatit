#!/bin/bash

configure_postgres(){
USER=$1
sudo sed -i -r '/all/ s/md5$/reject/g' /etc/postgresql/8.4/main/pg_hba.conf

echo "You will have to configure the postgres server yourself ..."
echo "Copy these lines: (don't forgot the rejected to add...)"
echo "local   $1         $1                          md5" 
echo "host    $1         $1   127.0.0.1/32           md5" 
read
sudo vim /etc/postgresql/8.4/main/pg_hba.conf
sudo service postgresql restart
}

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

if [ "$1" = "right" ] ; then
   if [ -n "$2" ] ; then
       configure_postgres "$2"
   else 
       echo "error waiting for username"
   fi
fi 

if [ "$1" = "addDB" ] ; then
   if [ -n "$2" ] && [ -n "$3" ] ; then
       add_database "$2" "$3"
   else 
       echo "error waiting for password and username"
   fi
fi 

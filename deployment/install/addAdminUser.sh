#!/bin/bash

ADMIN_NAME=administrator
if [ -n "$1" ]
then
    ADMIN_NAME=$1
fi

cat << EOF
We need a Admin user and no root login

EOF

addAdministrator(){
    apt-get install sudo
    adduser $ADMIN_NAME
    addgroup $ADMIN_NAME sudo
    passwd -l root
}

read -p "Do you want to do it ? (y/N)" reponse

if [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] 
then
    addAdministrator
fi


#!/bin/bash

cat << EOF
We need a Admin user and no root login

ADMIN_NAME=administrator
apt-get install sudo
adduser $ADMIN_NAME
addgroup $ADMIN_NAME sudo
passwd -l root

EOF



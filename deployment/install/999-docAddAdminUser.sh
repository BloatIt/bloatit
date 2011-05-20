#!/bin/bash

cat << EOF

How to add a new admin user and remove the root login:
------------------------------------

    ADMIN_NAME=administrator
    apt-get install sudo
    adduser $ADMIN_NAME
    addgroup $ADMIN_NAME sudo
    passwd -l root

EOF



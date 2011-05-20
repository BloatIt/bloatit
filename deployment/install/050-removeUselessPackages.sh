#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: Remove useless packages :
-----------------------------

This script will remove:

- python (Less interpretor the better)
- netcat-traditional (Obvously it is a very good tool for hakers)
- xserver-xorg (not needed)

EOF

elif [ "$1" = exec ] ; then 
    sudo apt-get remove --purge python laptop-detect netcat-traditional
    sudo apt-get remove --purge xserver-xorg virtualbox-ose-guest-x11 x11-common
    sudo apt-get remove --purge gcc
    sudo apt-get autoremove --purge
else 
    echo "Parameter must be message or exec"
fi


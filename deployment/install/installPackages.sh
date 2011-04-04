#!/bin/bash

# install the needed packages.
# Remove useless packages :
#  - python (Less interpretor the better)
#  - netcat-traditional (Obvously it is a very good tool for hakers)
#  - xserver-xorg (not needed)
#


install_pkg(){
sudo apt-get install vim less git-core bash-completion 
sudo apt-get install postgresql lighttpd openssh-server default-jre-headless rsync
}

purge_useless_pkg(){
sudo apt-get remove --purge python laptop-detect netcat-traditional
sudo apt-get remove --purge xserver-xorg virtualbox-ose-guest-x11 x11-common
sudo apt-get autoremove --purge
# We need perl for git.
}

if [ "$1" = "install" ] ; then
    install_pkg
elif [ "$1" = "purge" ] ; then
    purge_useless_pkg
else
    echo "usage $0 [ install | purge ]" 1>&2
fi


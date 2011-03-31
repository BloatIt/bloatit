#!/bin/bash

configure_sshd(){
local SSHD_CFG=/etc/ssh/sshd_config

sudo sed -i -r '/RSAAuthentication/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG
sudo sed -i -r '/PubkeyAuthentication/ s/#?(.*) no$/\1 yes/g' $SSHD_CFG
sudo sed -i -r '/IgnoreUserKnownHosts/ s/#?(.*) no$/\1 yes/g' $SSHD_CFG
sudo sed -i -r '/PasswordAuthentication/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG
sudo sed -i -r '/X11Forwarding/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG

sudo service ssh restart
}

add_trusted_key(){
mkdir ~/.ssh
echo "$1" >> ~/.ssh/authorized_keys
}

if [ "$1" = "addKey" ] ; then 
   if [ -n "$2" ] ; then
      add_trusted_key "$2"
   else 
      echo "I need a key ..."
   fi
fi

if [ "$1" = "configure" ] ; then 
    configure_sshd
fi

#!/bin/bash

usage(){
cat << EOF 
usage: $0 host 

This script ask questions and install dependencies / configuration to a distant host

OPTIONS:
   host can be localhost or elveos@192.168.0.13 ...
EOF
}

# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
COMMONS=$ROOT/commons/
DEPLOYMENT_INSTALL_SCRIPT=$ROOT/install/install.sh

# Add the includes 
. $COMMONS/includes.sh

if [ -z "$1" ] ; then
    error "You forgot to specify a host where to install the dependencies"
    exit 1
fi

remote_launch "$1" $DEPLOYMENT_INSTALL_SCRIPT 


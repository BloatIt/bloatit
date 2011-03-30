#!/bin/bash

# Context: make sure we are in the right directory and that the includes are done.
cd "$(dirname $0)"
. $PWD/commons/includes.sh

usage(){
cat << EOF 
usage: $0 host 

This script ask questions and install dependencies / configuration to a distant host

OPTIONS:
   host can be localhost or admin@192.168.0.13 ...
EOF
}

if [ -z "$1" ] ; then
    error "You forgot to specify a host where to install the dependencies"
    exit 1
fi

remote_launch "$1" install/deployement-root.sh 

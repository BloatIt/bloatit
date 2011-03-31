#!/bin/bash

usage(){
cat << EOF 
usage: $0 host version

This script rollback a distant versionned elveos server.

OPTIONS:
   host can be localhost or elveos@192.168.0.13 ...
   version could be 1.0.2-alpha
EOF
}

# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
COMMONS=$ROOT/commons/
ROLLBACK_SCRIPT=rollback/rollback.sh

# Add the includes 
. $COMMONS/includes.sh

if [ -z "$1" ] ; then
    error "You forgot to specify a host"
    exit 1
fi
if [ -z "$2" ] ; then
    error "You forgot the version"
    exit 1
fi


remote_launch "$1" $ROLLBACK_SCRIPT "$2"

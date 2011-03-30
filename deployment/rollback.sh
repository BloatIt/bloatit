#!/bin/bash

# Context: make sure we are in the right directory and that the includes are done.
cd "$(dirname $0)"
. $PWD/commons/includes.sh

usage(){
cat << EOF 
usage: $0 host version

This script rollback a distant versionned elveos server.

OPTIONS:
   host can be localhost or admin@192.168.0.13 ...
   version could be 1.0.2-alpha
EOF
}

if [ -z "$1" ] ; then
    error "You forgot to specify a host"
    exit 1
fi

if [ -z "$2" ] ; then
    error "You forgot the version"
    exit 1
fi

remote_launch "$1" deployment/rollback.sh "$2"

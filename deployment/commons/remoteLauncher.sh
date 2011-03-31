#!/bin/bash

##
## Launch a script on a specified host. (Using ssh).
## The location of the script on the remote host is not garenteed.
## The script is a local script found in bloatit/deployment folder.
## 
## WARNING: I assume the current path is bloatit/deployment
## 
## usage:
##
##      HOST:    The name of the computer on which we want to launch the script.
##      SCRIPT:  The script to launch. It must be a bash script, url relative from
##               the bloatit/deployment folder.
##      OPTS...: All the option following are the script options.
##
remote_launch() {
if [ -z "$1" ] || [ -z "$2" ] ; then 
    error "Error: host and scriptname are needed! "
    error "usage: $0 host scriptname [options ...]"
    exit 1
fi

HOST=$1
SCRIPT=$2

shift
shift

if [ "$HOST" = "localhost" ] || [ "$HOST" = "127.0.0.1" ] || [ "$HOST" = "::1" ] ; then
    cp -r . /tmp
    cd /tmp
    bash -- "$SCRIPT" $@
    cd /tmp && rm -rf deployment file install commons
else
    scp -r . $HOST:/tmp/
    ssh -t $HOST "cd /tmp && bash -- \"$SCRIPT\" $@"
    ssh $HOST "cd /tmp && rm -rf deployment file install commons"
fi
}

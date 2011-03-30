#!/bin/bash

exit_ok(){
    echo "exiting"
    exit 0
}

exit_fail(){
    echo "Failure. Abording know ! "
    kill $$
    exit 128
}

abort(){
    echo "aborting"
    exit 255
}

abort_if_non_zero(){
    if [ "$1" != "0" ] ; then 
        abort
    fi
}

##
## Send newly added data to the distant server
##
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    HOST : where to transfer the data (for example linkeos.com)
##    REPOS_DIR : the bloatit root dir.
##    USER : the distant user owning the elveos server.
transferData() {
    local _log_file="$1"
    local _host="$2"
    local _repos_dir="$3"
    local _user="$4"
    ./deployment/transfert.sh -d $_host -l $_log_file -s $_repos_dir -n $_user
    [ $? = 0 ] || exit_fail
}



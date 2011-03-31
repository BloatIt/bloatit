#!/bin/sh

# This script is to be included by other scripts.
# The COMMONS variable must be setted

. $COMMONS/remoteLauncher.sh
. $COMMONS/logger.sh

exit_ok(){
    success "exiting"
    exit 0
}

exit_fail(){
    error "Failure. Abording know ! "
    kill $$
    exit 128
}

abort(){
    warning "aborting"
    exit 255
}

abort_if_non_zero(){
    if [ "$1" != "0" ] ; then 
        abort
    fi
}

exit_on_failure(){
    if [ "$1" != "0" ] ; then 
        exit_fail
    fi
}




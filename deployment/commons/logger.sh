#!/bin/sh

BLUE="\\033[1;34m"
RED="\\033[1;31m" 
GREEN="\\033[1;32m" 
ORANGE="\\033[1;33m" 
NORMAL="\\033[0;39m"

error(){
 echo -e "$RED"$*"$NORMAL"
}
warning(){
 echo -e "$ORANGE"$*"$NORMAL"
}
info(){
 echo $*
}
menu(){
 echo -e "$BLUE--- "$*" ---$NORMAL"
}
success(){
 echo -e "$GREEN"$*"$NORMAL"
}

calculateLogFilename() {
LOG_FILE=log-$(date --rfc-3339=date)
if [ -e "$LOG_FILE" ] ; then
	local i=1
	while [ -e "$LOG_FILE-$i" ] ; do
		i=$((i+1))
	done
	LOG_FILE=$LOG_FILE-$i
fi
}


log_date() {
    (
        echo
        info "--------------------------------------------------------------------------------"
        info "$(date +%T): ""$1" 
        info "--------------------------------------------------------------------------------"
    ) | tee -a $2	
}

log_ok() {
        echo 
        info "$1"
        read -p "Do you want to continue ? (yes/NO) " response
        if [ "$response" == yes ] || [ "$response" == YES ] ; then
            info "Continue" >> $2	
        else
            info "Game over" >> $2
            return 1
        fi
}

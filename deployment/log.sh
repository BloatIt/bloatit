#!/bin/sh

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
        echo "--------------------------------------------------------------------------------"
        echo "$(date +%T): ""$1" 
        echo "--------------------------------------------------------------------------------"
    ) | tee -a $2	
}

log_ok() {
    (
        echo 
        echo "$1"
        echo "Do you want to continue ? (yes/NO)"
    ) | tee -a $2	
        read response
        if [ "$response" == yes ] || [ "$response" == YES ] ; then
            echo "Continue" >> $2	
        else
            echo "Game over" >> $2
            return 1
        fi
}

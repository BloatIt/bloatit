#!/bin/bash

usage()
{
cat << EOF
usage: $0 -d host { -b | -r date | -p | -l } [ -n name ]

This script perform backup or restore versions

OPTIONS:
   -h      Show this message. 
   -d      Destination host.
   -o      Log file. (Log will be append)
   -b      Perform a backup.
   -l      List different versions.
   -r      Restore the version from a specific date.
   -p      Restore to the previous version.
   -n      Distant user name. Default is "elveos".

DATE FORMAT
  The string "now" (refers to the current time)

  A sequences of digits, like "123456890" (indicating the time in  seconds
    after the epoch)

  A string like "2002-01-25T07:00:00+02:00" in datetime format

  An  interval,  which is a number followed by one of the characters s, m,
  h, D, W, M, or Y  (indicating  seconds,  minutes,  hours,  days,  weeks,
  months, or years respectively), or a series of such pairs.  In this case
  the string refers to the time that preceded  the  current  time  by  the
  length  of  the interval.  For instance, "1h78m" indicates the time that
  was one hour and 78 minutes ago.  The calendar here is  unsophisticated:
  a  month  is  always  30  days,  a year is always 365 days, and a day is
  always 86400 seconds.

  A date format of the form YYYY/MM/DD, YYYY-MM-DD, MM/DD/YYYY, or  MM-DD-
  YYYY,  which  indicates midnight on the day in question, relative to the
  current timezone settings.  For instance, "2002/3/5", "03-05-2002",  and
  "2002-3-05" all mean March 5th, 2002.

  A  backup session specification which is a non-negative integer followed
  by 'B'.  For instance, '0B' specifies the time of  the  current  mirror,
  and '3B' specifies the time of the 3rd newest increment.
EOF

}

ACTION=
USER=elveos
HOST=
LOG_FILE=

while getopts "hblpd:r:n:o:" OPTION
do
     case $OPTION in
   h)
       usage
       exit 1
       ;;
   b)
       ACTION=backup
       ;;
   r)
       ACTION=restore
       RESTORE_DIR=$OPTARG
       ;;
   p)
       ACTION=previous
       ;;
   l)
       ACTION=list
       ;;
   d)
       HOST=$OPTARG
       ;;
   o)
       LOG_FILE=$OPTARG
       ;;
   n)
       USER=$OPTARG
       ;;
   ?)
       usage 1>&2
       exit 1
       ;;
     esac
done

if [ -z "$HOST" ] || [ -z "$ACTION" ] || [ -z "$LOG_FILE" ]; then
    echo "An argument is missing" 1>&2
    usage 1>&2
    exit 1
fi

. $PWD/conf.sh
. $PWD/log.sh

INCLUDE=( $UPLOAD_DIR $CONF_DIR $SHARE_DIR )

restore() {
# restore in the RESTORE_DIR folder
local tmp=${INCLUDE[@]/#/--include $ROOT}
rdiff-backup -r $1 --force $USER@$HOST::$ROOT/$BACKUP_DIR $USER@$HOST::$ROOT/$RESTORE_DIR

if [ "$?" == 0 ] ; then
   # Delete the old content
   for i in ${INCLUDE[@]} ; do 
       echo "rm -rf $ROOT/$i"
       ssh $USER@$HOST rm -rf "$ROOT/$i"
   done
   # Copy the new one.
   ssh $USER@$HOST mv $RESTORE_DIR/* $ROOT/ && \
   echo "mv $RESTORE_DIR/* $ROOT/" 
else
   echo "rdiff-backup error. Abording restore." 1>&2
   exit 1
fi
}

if [ "$ACTION" = "backup" ] ; then
    log_date "Backup data in remote host: $USER@$HOST" $LOG_FILE
    rdiff-backup -v3 $(echo ${INCLUDE[@]/#/--include $ROOT}) --exclude "**" $USER@$HOST::$ROOT $USER@$HOST::$ROOT/$BACKUP_DIR | tee -a $LOG_FILE && echo "Backup done"

elif [ "$ACTION" = "previous" ] ; then
    log_date "Restore data in remote host: $USER@$HOST From previous version" $LOG_FILE
    restore now | tee -a $LOG_FILE && echo "Restore done"

elif [ "$ACTION" = "restore" ] ; then
    log_date "Restore data in remote host: $USER@$HOST" $LOG_FILE
    restore $RESTORE_DIR | tee -a $LOG_FILE && echo "Restore done"

elif [ "$ACTION" = "list" ] ; then
    rdiff-backup -v3 -l $USER@$HOST::$ROOT/$BACKUP_DIR | tee -a $LOG_FILE

fi


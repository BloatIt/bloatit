#!/bin/bash

usage()
{
cat << EOF
usage: $0 -d host -r releaseVersion -b bloatitFolder [-n name]
       $0 -h 

This script create a release, tag it and send it to a distant host.

OPTIONS:
   -h      Show this message. 
   -d      Destination host. Requiered.
   -r      Release version (Could be 1.0.alfa).
   -b      Bloatit root folder (git/mvn root).
   -n      Distant user name. Default is "elveos".
EOF
}

#
# Parsing the arguments
#

HOST=
RELEASE_VERSION=
REPOS_DIR=
USER=elveos

while getopts "d:r:b:n:h" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         d)
             HOST=$OPTARG
             ;;
         r)
             RELEASE_VERSION=$OPTARG
             ;;
         b)
             REPOS_DIR=$OPTARG
             ;;
         n)
             USER=$OPTARG
             ;;
         ?)
	     usage 1>&2
             exit
             ;;
     esac
done

if [ -z "$RELEASE_VERSION" ] || [ -z "$REPOS_DIR" ]
then
	echo -e "Arguments are missing !!! \n" 1>&2
	usage 1>&2
	exit 1
fi

if [ -z "$HOST" ] 
then 
	echo -e "You have to specify a host. !!! \n" 1>&2
	usage 1>&2
	exit 1
fi

. $PWD/log.sh
. $PWD/conf.sh
. $PWD/releaseUtils.sh

calculateLogFilename # We can know use the LOG_FILE variable.
PREFIX=elveos
SSH="ssh -t $USER@$HOST"
LIQUIBASE_DIR=$REPOS_DIR/main/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar

echo "HOST=$HOST
RELEASE_VERSION=$RELEASE_VERSION
REPOS_DIR=$REPOS_DIR
USER=$USER"
log_ok "You are about to revert to a prevous version. Are you sure you want to do this?" $LOG_FILE
abort_if_non_zero $?

revert "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$LIQUIBASE_DIR" "$USER" "$SSH"

echo "Revert done !"

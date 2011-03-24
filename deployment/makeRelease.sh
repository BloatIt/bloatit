#!/bin/bash

usage()
{
cat << EOF
usage: "$0" -d host -r releaseVersion { -s nextSnapshotVersion | -t } -b bloatitFolder [-n name] [-h] 

This script create a release, tag it and send it to a distant host.

OPTIONS:
   -h      Show this message. 
   -r      Release version (Could be 1.0.alfa).
   -s      Next snapshot number version (Must be only numeric).
   -d      Destination host. Requiered.
   -b      Bloatit root folder (git/mvn root).
   -n      Distant user name. Default is "bloatit".
   -t      Use tagged version.
EOF
}

#
# Parsing the arguments
#

HOST=
RELEASE_VERSION=
NEXT_SNAPSHOT_VERSION=
REPOS_DIR=
USE_TAG=
USER=bloatit

while getopts "thd:b:n:r:s:" OPTION
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
         s)
             NEXT_SNAPSHOT_VERSION=$OPTARG
             ;;
         b)
             REPOS_DIR=$OPTARG
             ;;
         t)
             USE_TAG="true"
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

if [ -z "$HOST" ] || [ -z "$RELEASE_VERSION" ] || [ -z "$REPOS_DIR" ]
then
	echo -e "Arguments are missing !!! \n" 1>&2
	usage 1>&2
	exit 1
fi

. $PWD/log.sh
. $PWD/conf.sh
. $PWD/releaseUtils.sh

calculateLogFilename # We can know use the LOG_FILE variable.
PREFIX=bloatit
MVN="mvn -f $REPOS_DIR/pom.xml" 
SSH="ssh -t $USER@$HOST"
LIQUIBASE_DIR=$REPOS_DIR/main/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar

echo "HOST=$HOST
RELEASE_VERSION=$RELEASE_VERSION
NEXT_SNAPSHOT_VERSION=$NEXT_SNAPSHOT_VERSION
REPOS_DIR=$REPOS_DIR
USE_TAG=$USE_TAG
USER=$USER"
log_ok "You are about to create a new release and send it to a distant server" $LOG_FILE
abort_if_non_zero $?

if [ -z "$USE_TAG" ] ; then
    performMvnRelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$NEXT_SNAPSHOT_VERSION" "$MVN"
    transferData "$LOG_FILE" "$HOST" "$REPOS_DIR" "$USER"
else
    log_date "Using the tag: $PREFIX-$RELEASE_VERSION" "$LOG_FILE"
    if [ -n "$( git status --porcelain)" ] ; then
	echo "You have non commited data !"
        exit_fail
    fi
    git checkout "$PREFIX-$RELEASE_VERSION"
    transferData "$LOG_FILE" "$HOST" "$REPOS_DIR" "$USER"
    git checkout HEAD
    git checkout master
    echo "WARNING: Going back to master !! "
fi


commitPrerelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$SSH"

stopBloatitServer "$LOG_FILE" "$SSH"

migratingDB "$LOG_FILE" "$LIQUIBASE_DIR" "$USER" "$SSH"

propagateConfFiles "$LOG_FILE" "$UP_CONF_DIR" "$CONF_DIR" "$UP_SHARE_DIR" "$SHARE_DIR" "$UP_RESSOURCES" "$CLASSES" "$SSH"

commitRelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$SSH"

startBloatitServer "$LOG_FILE" "$SSH"

echo "Release done ! "

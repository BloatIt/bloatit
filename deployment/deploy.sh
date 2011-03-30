#!/bin/bash

# Context: make sure we are in the right directory and that the includes are done.
cd "$(dirname $0)"
. $PWD/commons/includes.sh

usage()
{
cat << EOF
usage: $0 -d host -r releaseVersion -b bloatitFolder [-n name]
       $0 -h 

This script send a release to a distant host.

OPTIONS:
   -h      Show this message. 
   -d      Destination host. Requiered.
   -r      Release version (Could be 1.0-alfa).
   -b      Bloatit root folder (git/mvn root).
   -n      Distant user name. Default is "elveos".
EOF
}

#
# Parsing the arguments
#
FOLDER=deployment
HOST=
RELEASE_VERSION=
REPOS_DIR=
USER=elveos

while getopts "hd:r:b:n:" OPTION
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
	echo -e "You have to specify a host !! \n" 1>&2
	usage 1>&2
	exit 1
fi


. $PWD/commons/logger.sh
. $PWD/$FOLDER/conf.sh
. $PWD/$FOLDER/releaseUtils.sh

calculateLogFilename # We can know use the LOG_FILE variable.
PREFIX=elveos
MVN="mvn -f $REPOS_DIR/pom.xml" 
SSH="ssh -t $USER@$HOST"
LIQUIBASE_DIR=$REPOS_DIR/main/liquibase/liquibase-core-2.0.2-SNAPSHOT.jar

echo "HOST=$HOST
RELEASE_VERSION=$RELEASE_VERSION
NEXT_SNAPSHOT_VERSION=$NEXT_SNAPSHOT_VERSION
REPOS_DIR=$REPOS_DIR
USE_TAG=$USE_TAG
LOCAL_ONLY=$LOCAL_ONLY
USER=$USER"
log_ok "You are about to create a new release and send it to a distant server" $LOG_FILE
abort_if_non_zero $?

# First checkout the tag.
log_date "Using the tag: $PREFIX-$RELEASE_VERSION" "$LOG_FILE"
if [ -n "$( git status --porcelain)" ] ; then
    echo "You have non commited data !"
    exit_fail
fi
git checkout "$PREFIX-$RELEASE_VERSION" || exit_fail
$MVN clean install -Dmaven.test.skip=true || exit_fail

# Then transfer the data to the host
transferData "$LOG_FILE" "$HOST" "$REPOS_DIR" "$USER"

# And return to the master branch
warning "WARNING: Going back to master !! "
git checkout master

#
# Local work is done. do the distant work.
#

remote_launch "" deployment/deployDoDistantWork.sh "$RELEASE_VERSION" "$UP_CONF_DIR" "$CONF_DIR" "$UP_SHARE_DIR" "$SHARE_DIR" "$UP_RESSOURCES" "$CLASSES"

success "Release done ! "

#!/bin/bash

usage()
{
cat << EOF
usage: $0 -d host -r releaseVersion -s nextSnapshotVersion -b bloatitFolder [-n name] [-h] 

This script create a release, tag it and send it to a distant host.

OPTIONS:
   -h      Show this message. 
   -r      Release version (Could be 1.0.alfa).
   -s      Next snapshot number version (Must be only numeric).
   -d      Destination host. Requiered.
   -b      Bloatit root folder (git/mvn root).
   -n      Distant user name. Default is "bloatit".
EOF
}

#
# Parsing the arguments
#

HOST=
RELEASE_VERSION=
NEXT_SNAPSHOT_VERSION=
REPOS_DIR=
USER=bloatit

while getopts "hd:b:n:r:s:" OPTION
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
         n)
             USER=$OPTARG
             ;;
         ?)
	     usage 1>&2
             exit
             ;;
     esac
done

if [ -z "$HOST" ] || [ -z "$RELEASE_VERSION" ] || [ -z "$NEXT_SNAPSHOT_VERSION" ] || [ -z "$REPOS_DIR" ]
then
	echo -e "Arguments are missing !!! \n" 1>&2
	usage 1>&2
	exit 1
fi

PREFIX=bloatit
MVN="mvn -f $REPOS_DIR/pom.xml "

. $PWD/log.sh
. $PWD/conf.sh

SSH="ssh $USER@$HOST"

exit_ok(){
    echo "exiting"
    exit 0
}

exit_fail(){
    echo "Failure. Abording know ! "
    exit 128
}

abort(){
    echo "aborting"
    exit 255
}

abort_if_non_zero(){
    if [ $1 != "0" ] ; then 
        abort
    fi
}

calculateLogFilename
# We can know use the LOG_FILE variable.

log_ok "You are about to create a new release and send it to a distant server" $LOG_FILE
abort_if_non_zero $?

##
## Perform the mvn release.
##

log_date "Make a mvn release." $LOG_FILE
(
    $MVN release:clean
    $MVN install -Dmaven.test.skip=true
    $MVN --batch-mode -Dtag=$PREFIX-$RELEASE_VERSION release:prepare \
                      -DreleaseVersion=$RELEASE_VERSION \
		      -DdevelopmentVersion=$NEXT_SNAPSHOT_VERSION-SNAPSHOT \
		      -DautoVersionSubmodules=true \
    && $MVN release:perform

    _result=$?
) | tee -a $LOG_FILE

[ "$_result" = "0" ] || exit_fail


##
## Send newly added data to the distant server
##

./transfert.sh -d $HOST -l $LOG_FILE -s $REPOS_DIR -n $USER

[ $? = 0 ] || exit_fail

##
## Commit the git distant new data
##

$SSH "
git status
git add -A
git commit -m \"New PreRelease $PREFIX-$RELEASE_VERSION\"
"

##
## Stopping the server.
##
log_date "Stopping the bloatit server." $LOG_FILE
(
    $SSH "/etc/init.d/bloatit stop ; sleep 2"
    _result=$?
) | tee -a $LOG_FILE

##
## Migrating DB.
##

##
## Propagate conf files.
##
#remote execute:
(

MERGE_FILE_SCRIPT=mergeFIles.sh
cat ./$MERGE_FILE_SCRIPT | $SSH " 
cat > /tmp/$MERGE_FILE_SCRIPT
chmod u+x /tmp/$MERGE_FILE_SCRIPT

# .config files
/tmp/$MERGE_FILE_SCRIPT $UP_CONF_DIR $CONF_DIR
# .local/share files
/tmp/$MERGE_FILE_SCRIPT $UP_SHARE_DIR $SHARE_DIR
# ressources files
/tmp/$MERGE_FILE_SCRIPT $UP_RESSOURCES $CLASSES
"
    _result=$?
) | tee -a $LOG_FILE

[ $_result = 0 ] || exit_fail

##
## Launching the server.
##
log_date "Starting the bloatit server." $LOG_FILE
(
    $SSH "/etc/init.d/bloatit start"
    _result=$?
) | tee -a $LOG_FILE

[ $_result = 0 ] || exit_fail

echo "Release done ! "

#!/bin/bash


USER=bloatit
REPOS_DIR=/home/thomas/bloatit/
MVN=mvn

PREFIX=bloatit
RELEASE_VERSION=1.0
NEXT_SNAPSHOT_VERSION=1.1

. $PWD/log.sh
. $PWD/conf.sh

SSH=ssh $USER@$HOST

exit_ok(){
    echo "exiting"
    exit 0
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
    $MVN --batch-mode -Dtag=$PREFIX-$RELEASE_VERSION release:prepare \
                      -DreleaseVersion=$RELEASE_VERSION \
		      -DdevelopmentVersion=$NEXT_SNAPSHOT_VERSION-SNAPSHOT
		      -DautoVersionSubmodules=true
    $MVN release:perform
) | tee -a $LOG_FILE

##
## Send newly added data to the distant server
##

./transfert.sh -d $HOST -l $LOG_FILE -s $REPOS_DIR -n $USER

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

##
## Migrating DB.
##

##
## Propagate conf files.
##

#remote execute:
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
##
## Launching the server.
##

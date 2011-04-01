#!/bin/bash

# Usage / documentation
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

# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
PREFIX=elveos
COMMONS=$ROOT/commons/
LIQUIBASE_DIR=$ROOT/files/liquibase-core-2.0.2-SNAPSHOT.jar
TRANSFERT_SCRIPT=$ROOT/deployment/transfert.sh
DEPLOYMENT_REMOTE_SCRIPT=deployment/deployDoDistantWork.sh

# Add the includes 
. $COMMONS/includes.sh

calculateLogFilename # We can know use the LOG_FILE variable.

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
# Make sure there is no bug in the command line.
if [ -z "$RELEASE_VERSION" ] || [ -z "$REPOS_DIR" ] || [ -z "$HOST" ] 
then
	error "Arguments are missing !!! \n"
	usage 1>&2
	exit 1
fi
if [ -e "$REPOS_DIR" ] 
then
	cd "$REPOS_DIR"
	REPOS_DIR=$PWD
	cd -
else
    error "Repos directory is not found !"
    exit 1
fi

MVN="mvn -f $REPOS_DIR/pom.xml" 

echo "HOST=$HOST
RELEASE_VERSION=$RELEASE_VERSION
REPOS_DIR=$REPOS_DIR
USER=$USER"
log_ok "You are about to deploy a release to a distant server" 
abort_if_non_zero $?

# First checkout the tag.
log_date "Using the tag: $PREFIX-$RELEASE_VERSION" 
if [ -n "$( git status --porcelain)" ] ; then
    error "You have non-commited data !"
    exit 1
fi
git checkout "$PREFIX-$RELEASE_VERSION"
$MVN clean install -Dmaven.test.skip=true 

# Then transfer the data to the host
$TRANSFERT_SCRIPT -d $HOST -s $REPOS_DIR -n $USER
exit_on_failure $?

# And return to the master branch
warning "WARNING: Going back to master !! "
git checkout master

#
# Local work is done. do the distant work.
#
remote_launch "$USER@$HOST" $DEPLOYMENT_REMOTE_SCRIPT "$RELEASE_VERSION" 

success "Release done ! "

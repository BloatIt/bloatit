#!/bin/bash

usage()
{
cat << EOF
usage: $0 -r releaseVersion -b bloatitFolder
       $0 -h 

This script create a release and tag it.

OPTIONS:
   -h      Show this message. 
   -r      Release version (Could be 1.0-alfa).
   -b      Bloatit root folder (git/mvn root).
EOF
}
# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
COMMONS=$ROOT/commons/
PREFIX=elveos

# Add the includes 
. $COMMONS/includes.sh

#
# Parsing the arguments
#
FOLDER=deployment
RELEASE_VERSION=
NEXT_SNAPSHOT_VERSION=
REPOS_DIR=
while getopts "hr:b:" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         r)
             RELEASE_VERSION=$OPTARG
             NEXT_SNAPSHOT_VERSION=$(echo $RELEASE_VERSION | sed -r "s/\\-.*$//g")
             ;;
         b)
             REPOS_DIR=$OPTARG
             ;;
         ?)
	     usage 1>&2
             exit
             ;;
     esac
done
if [ -z "$RELEASE_VERSION" ] 
then
	echo -e "Arguments are missing !!! \n" 1>&2
	usage 1>&2
	exit 1
fi

MVN="mvn -f $REPOS_DIR/pom.xml" 

echo "RELEASE_VERSION=$RELEASE_VERSION
NEXT_SNAPSHOT_VERSION=$NEXT_SNAPSHOT_VERSION
REPOS_DIR=$REPOS_DIR"
log_ok "You are about to create a new release and send it to a distant server" 
abort_if_non_zero $?

##
## Perform the mvn release.
## Ordered parameters :
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
##    NEXT_SNAPSHOT_VERSION : the version of the next snapshot.
##    MVN : the mvn command to launch (for example "mvn -f ../pom.xml") 
performMvnRelease() {
    local _prefix="$1"
    local _release_version="$2"
    local _next_snapshot_version="$3"
    local _mvn="$4"

    stty -echo
    read -p "I need the master password: " _password ; echo
    stty echo

    log_date "Make a mvn release." 
    $_mvn install -Dmaven.test.skip=true
    exit_on_failure $?

    $_mvn --batch-mode \
        -Dtag=$_prefix-$_release_version release:prepare \
        -DreleaseVersion=$_release_version \
        -DdevelopmentVersion=$_next_snapshot_version-SNAPSHOT \
        -DautoVersionSubmodules=true \
        -Darguments="-DargLine=-DmasterPassword=$_password" \
        && $_mvn release:clean

    exit_on_failure $?
}

performMvnRelease "$PREFIX" "$RELEASE_VERSION" "$NEXT_SNAPSHOT_VERSION" "$MVN"

success "Release done."


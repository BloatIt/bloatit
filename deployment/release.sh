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

GRADLE="gradle -b $REPOS_DIR/build.gradle" 

if [ -n "$(git status --porcelain)" ] ; then
	echo there is non commited data. abording.
	exit
fi

if [ -n "$(git tag | grep "^elveos-$RELEASE_VERSION$")" ] ; then
	echo "I found a tag with this current version. Make sure you have not make any mistakes. abording."
	exit
fi

echo "RELEASE_VERSION=$RELEASE_VERSION
NEXT_SNAPSHOT_VERSION=$NEXT_SNAPSHOT_VERSION
REPOS_DIR=$REPOS_DIR"
log_ok "You are about to create a new release and send it to a distant server" 
abort_if_non_zero $?


##
## Change the versions in the poms files
##    REPOS_DIR the directory where the main build.gradle is
##    VERSION : the version string of the release.
changeVersion(){
	local _repos_dir="$1"
        local _version="$2"
	
	sed -i -r "s/(^ *version ?= ?')[0-9a-zA-Z._-]+('$)/\1$_version\2/g" "$_repos_dir/build.gradle"
}

##
## Perform the mvn release.
## Ordered parameters :
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
##    NEXT_SNAPSHOT_VERSION : the version of the next snapshot.
##    REPOS_DIR the directory where the main build.gradle is
##    GRADLE : the gradle command to launch (for example "gradle -b ../build.gradle") 
performRelease() {
    local _prefix="$1"
    local _release_version="$2"
    local _next_snapshot_version="$3-SNAPSHOT"
    local _repos_dir="$4"
    local _gradle="$5"

	#
	# Create the realease
	#
    log_date "Change the versions in the build.gradle to: $_release_version"
    changeVersion "$_repos_dir" "$_release_version"
    exit_on_failure $?

    log_date "gradle clean build + tests"
    $_gradle clean build 
    exit_on_failure $?

	log_date "Create a git tag on this current version: $_release_version"
	git commit -a -m "release the $_release_version"
	git tag "elveos-$_release_version"

	#
	# Go back to working version
	#
	log_date "Going to the new snapshot version (pom): $_next_snapshot_version"
	changeVersion "$_repos_dir" "$_next_snapshot_version"
    exit_on_failure $?

	log_date "gradle build"
	$_gradle build -x test
    exit_on_failure $?

	log_date "Commit the new gradle version"
	git commit -a -m "Going to the dev version: $_next_snapshot_version"
    exit_on_failure $?

    read -p "Do you want to push the tag: elveos-$_release_version. (y/N)" _reponse
    if [ "$_reponse" = "y" ] || [ "$_reponse" = "Y" ] ; then
        git push origin "elveos-$_release_version"
    fi
}

performRelease "$PREFIX" "$RELEASE_VERSION" "$NEXT_SNAPSHOT_VERSION" "$REPOS_DIR" "$GRADLE"

success "Release done."


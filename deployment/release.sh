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

MVN="echo -f $REPOS_DIR/pom.xml" 

echo "RELEASE_VERSION=$RELEASE_VERSION
NEXT_SNAPSHOT_VERSION=$NEXT_SNAPSHOT_VERSION
REPOS_DIR=$REPOS_DIR"
log_ok "You are about to create a new release and send it to a distant server" 
abort_if_non_zero $?


##
## Change the versions in the poms files
##    REPOS_DIR the directory where the main pom.xml is
##    VERSION : the version string of the release.
changePomVersion(){
	local _repos_dir="$1"
    local _version="$2"

	local _xmls=xmlstarlet
	local _xmlstarlet_opt="-N mvn=http://maven.apache.org/POM/4.0.0" 

	local _dirs=( $( $_xmls sel $_xmlstarlet_opt -t -v "/mvn:project/mvn:modules" "$_repos_dir"/pom.xml) "." ) 

	for i in ${_dirs[@]} ; do
		$_xmls ed -L  $_xmlstarlet_opt -u "/mvn:project/mvn:version" -v "$_version" "$_repos_dir/$i/pom.xml"
		_artifacts="$_artifacts $( $_xmls sel $_xmlstarlet_opt -t -v "/mvn:project/mvn:artifactId" "$_repos_dir/$i/pom.xml")"
	done
	_artifacts=( $_artifacts )
	for i in ${_dirs[@]} ; do
		for j in ${_artifacts[@]} ; do
			$_xmls ed -L $_xmlstarlet_opt -u "//mvn:dependency[mvn:artifactId='$j']/mvn:version" -v "$_version" "$_repos_dir/$i/pom.xml"
		done
	done
}

##
## Perform the mvn release.
## Ordered parameters :
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
##    NEXT_SNAPSHOT_VERSION : the version of the next snapshot.
##    REPOS_DIR the directory where the main pom.xml is
##    MVN : the mvn command to launch (for example "mvn -f ../pom.xml") 
performMvnRelease() {
    local _prefix="$1"
    local _release_version="$2"
    local _next_snapshot_version="$3"
	local _repos_dir="$4"
    local _mvn="$3"

    stty -echo
    read -p "I need the master password: " _password ; echo
    stty echo

	log_date "Change the versions in the poms to: $_release_version"
	changePomVersion "$_repos_dir" "$_release_version"

    log_date "Mvn clean install + tests"
    $_mvn clean install
    exit_on_failure $?

	log_date "Create a git tag on this current version: $_release_version"

	
    $_mvn --batch-mode \
        -Dtag=$_prefix-$_release_version release:prepare \
        -DreleaseVersion=$_release_version \
        -DdevelopmentVersion=$_next_snapshot_version-SNAPSHOT \
        -DautoVersionSubmodules=true \
        -Darguments="-DargLine=-DmasterPassword=$_password" \
        && $_mvn release:clean \
        || ( $_mvn release:rollback ; false )

    exit_on_failure $?
}

performMvnRelease "$PREFIX" "$RELEASE_VERSION" "$NEXT_SNAPSHOT_VERSION" "$REPOS_DIR" "$MVN"

success "Release done."


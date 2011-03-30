#!/bin/bash

usage()
{
cat << EOF
usage: $0 -r releaseVersion [-n name]
       $0 -h 

This script create a release, tag it and send it to a distant host.

OPTIONS:
   -h      Show this message. 
   -r      Release version (Could be 1.0.alfa).
   -n      Distant user name. Default is "elveos".
EOF
}

#
# Parsing the arguments
#

RELEASE_VERSION=
USER=elveos

while getopts "r:n:h" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         r)
             RELEASE_VERSION=$OPTARG
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

if [ -z "$RELEASE_VERSION" ] 
then
	echo -e "Arguments are missing !!! \n" 1>&2
	usage 1>&2
	exit 1
fi

. $PWD/log.sh
. $PWD/conf.sh
. $PWD/releaseUtils.sh

calculateLogFilename # We can know use the LOG_FILE variable.
PREFIX=elveos
SSH="ssh -t $USER@$HOST"
LIQUIBASE_DIR=file/liquibase-core-2.0.2-SNAPSHOT.jar

echo "RELEASE_VERSION=$RELEASE_VERSION
USER=$USER"
log_ok "You are about to revert to a prevous version. Are you sure you want to do this?" $LOG_FILE
abort_if_non_zero $?

##
## Perform a revert
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
##    LIQUIBASE_JAR : the full path to the liquibase-core jar.
##    USER : the elveos user
revert() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
    local _liquibase="$4"
    local _user="$5"

    local _classpath="."
    _classpath="$_classpath:/home/$_user/jars/dom4j-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/postgresql-8.4-701.jdbc4.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-api-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-log4j12-1.5.8.jar"

    log_date "Reverting the db to tag: $_prefix-$_release_version" $_log_file
    (
        cd /home/$_user/java/

        java -jar /tmp/$_liquibase --classpath=$_classpath rollback "$_prefix-$_release_version"

        [ $? = 0 ] || exit_fail
    ) | tee -a $_log_file

    log_date "Reverting to git tag: $_prefix-$_release_version" $_log_file
    (
	git checkout \"$_prefix-$_release_version\"
	git checkout -b \"$_prefix-$_release_version-branch\"
        [ $? = 0 ] || exit_fail
    )| tee -a $_log_file
}

revert "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$LIQUIBASE_DIR" "$USER" 

echo "Revert done !"

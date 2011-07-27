#!/bin/sh

# Usage / documentation
usage(){
cat << EOF 
usage: $0 releaseVersion 

This scritp do the distant work in a rollback

EOF
}

# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
COMMONS=$ROOT/../commons/
MERGE_FILE_SCRIPT=$ROOT/mergeFiles.sh
LIQUIBASE_DIR=files/liquibase-core-2.0.2-SNAPSHOT.jar
PREFIX=elveos

UPLOAD_DIR=.upload
UP_RESSOURCES=$UPLOAD_DIR/ressources
UP_CONF_DIR=$UPLOAD_DIR/conf
UP_SHARE_DIR=$UPLOAD_DIR/share
CONF_DIR=.config/bloatit/
SHARE_DIR=.local/share/bloatit/
CLASSES=java/
DEPEDENCIES=jars/


# Add the includes 
. $COMMONS/includes.sh

#
# Parsing the arguments
#
RELEASE_VERSION="$1"
# Make sure there is no bug in the command line.
if [ "$#" != 1 ]
then
    error "Arguments are missing !!! \n"
    usage 1>&2
    exit 1
fi



##
## Migrating DB.
## Ordered parameters :
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
##    LIQUIBASE_JAR : the full path to the liquibase-core jar.
##    USER : the elveos user
migratingDB() {
    local _prefix="$1"
    local _release_version="$2"
    local _liquibase="$3"
    local _user="$4"

    local _classpath="."
    _classpath="$_classpath:/home/$_user/jars/dom4j-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/postgresql-8.4-702.jdbc4.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-api-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-log4j12-1.6.0.jar"

    log_date "Migrating the DB." 

    stty -echo
    read -p "What is the Postgresql $_user password ? : " _password
    echo 
    stty echo
    cd /home/$_user/java/

    java -jar /tmp/$_liquibase --classpath=$_classpath \
                               --password=$_password \
                               --driver=org.postgresql.Driver \
                               --referenceDriver=org.postgresql.Driver \
                               --url=jdbc:postgresql://localhost/$_user \
                               --username=$_user \
                               --changeLogFile=liquibase/current.liquibase.xml \
                               rollback "$_prefix-$_release_version"
    exit_on_failure $?
    _password=
}

gitCheckout(){
    local _prefix="$1"
    local _release_version="$2"

    git checkout "$_prefix-$_release_version"
    git branch "branch-$_prefix-$_release_version"
    
}


#!/bin/sh

# Usage / documentation
usage(){
cat << EOF 
usage: $0 releaseVersion 

This scritp do the distant work in a deployment.

OPTIONS:
   host can be localhost or admin@192.168.0.13 ...
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
## Commit the git distant new data
## Ordered parameters :
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
commitPrerelease() {
    local _prefix="$1"
    local _release_version="$2"
    cd ~ 
    git status
    git add -A
    git commit -m "New PreRelease $_prefix-$_release_version"
}

##
## Stopping the server.
## Ordered parameters :
stopBloatitServer() {
    log_date "Stopping the elveos server." 
    /etc/init.d/elveos stop && sleep 2
    exit_on_failure $?
}

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
    _classpath="$_classpath:/home/$_user/jars/postgresql-8.4-701.jdbc4.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-api-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-log4j12-1.5.8.jar"

    log_date "Migrating the DB." 

    stty -echo
    read -p "What is the Postgresql $_user password ? : " _password
    echo 
    stty echo
    cd /home/$_user/java/

    java -jar /tmp/$_liquibase --classpath=$_classpath --password=$_password update
    exit_on_failure $?

    java -jar /tmp/$_liquibase --classpath=$_classpath --password=$_password tag "$_prefix-$_release_version"
    exit_on_failure $?

    _password=
}

##
## Propagate conf files.
## Will look for a "mergeFile.sh" file in the current directory.
##
## Ordered parameters :
##    UP_CONF_DIR : remote directory where the conf files has been uploaded
##    CONF_DIR : where to put the conf files.
##    UP_SHARE_DIR : remote directory where the share files has been uploaded
##    SHARE_DIR : where to put the share files.
##    UP_RESSOURCES : where the resources has been uploaded.
##    CLASSES : where to put the resources (where the java classes are)
propagateConfFiles() {
    local _up_conf_dir="$UP_CONF_DIR"
    local _conf_dir="$CONF_DIR"
    local _up_share_dir="$UP_SHARE_DIR"
    local _share_dir="$SHARE_DIR"
    local _up_ressources="$UP_RESSOURCES"
    local _classes="$CLASSES"
    log_date "Merging the conf files." 
    # .config files
    bash $MERGE_FILE_SCRIPT -f $_up_conf_dir -t $_conf_dir
    exit_on_failure $?

    # .local/share files
    bash $MERGE_FILE_SCRIPT -f $_up_share_dir -t $_share_dir
    exit_on_failure $?

    # ressources files
    bash $MERGE_FILE_SCRIPT -f $_up_ressources -t $_classes
    exit_on_failure $?

}

##
## Commit the git distant new data
## Ordered parameters :
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
commitRelease() {
    local _prefix="$1"
    local _release_version="$2"
    git status
    git add -A
    git commit -m "New Release $_prefix-$_release_version"
    git tag "$_prefix-$_release_version"
}

##
## Launching the server.
## Ordered parameters :
startBloatitServer() {
    log_date "Starting the elveos server." 
    /etc/init.d/elveos start
    exit_on_failure $?
}

commitPrerelease "$PREFIX" "$RELEASE_VERSION"

stopBloatitServer
exit_on_failure $?

propagateConfFiles 
exit_on_failure $?

migratingDB "$PREFIX" "$RELEASE_VERSION" "$LIQUIBASE_DIR" "$USER"
exit_on_failure $?

commitRelease "$PREFIX" "$RELEASE_VERSION"
exit_on_failure $?

success "Deployment done."

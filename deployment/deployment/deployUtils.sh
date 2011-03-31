#!/bin/sh

. $PWD/deployment/releaseUtils.sh

##
## Commit the git distant new data
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
commitPrerelease() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"

        git status
        git add -A
        git commit -m \"New PreRelease $_prefix-$_release_version\"
}

##
## Stopping the server.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
stopBloatitServer() {
    local _log_file="$1"
    log_date "Stopping the elveos server." $_log_file
    (
        /etc/init.d/elveos stop && sleep 2
        [ $? = 0 ] || exit_fail
    ) | tee -a $_log_file
}

##
## Migrating DB.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
##    LIQUIBASE_JAR : the full path to the liquibase-core jar.
##    USER : the elveos user
migratingDB() {
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

    log_date "Migrating the DB." $_log_file
    (
        cd /home/$_user/java/

        java -jar /tmp/$_liquibase --classpath=$_classpath update
        java -jar /tmp/$_liquibase --classpath=$_classpath tag "$_prefix-$_release_version"
        [ $? = 0 ] || exit_fail
    ) | tee -a $_log_file
}

##
## Propagate conf files.
## Will look for a "mergeFile.sh" file in the current directory.
##
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    UP_CONF_DIR : remote directory where the conf files has been uploaded
##    CONF_DIR : where to put the conf files.
##    UP_SHARE_DIR : remote directory where the share files has been uploaded
##    SHARE_DIR : where to put the share files.
##    UP_RESSOURCES : where the resources has been uploaded.
##    CLASSES : where to put the resources (where the java classes are)
propagateConfFiles() {
    local _log_file="$1"
    local _up_conf_dir="$2"
    local _conf_dir="$3"
    local _up_share_dir="$4"
    local _share_dir="$5"
    local _up_ressources="$6"
    local _classes="$7"
log_date "Merging the conf files." $LOG_FILE
(
    # .config files
    bash deployment/$_merge_file_script $_up_conf_dir $_conf_dir
    [ $? = 0 ] || exit_fail

    # .local/share files
    bash deployment/$_merge_file_script $_up_share_dir $_share_dir
    [ $? = 0 ] || exit_fail

    # ressources files
    bash deployment/$_merge_file_script $_up_ressources $_classes
    [ $? = 0 ] || exit_fail

) | tee -a $_log_file
}

##
## Commit the git distant new data
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "elveos").
##    RELEASE_VERSION : the version string of the release.
commitRelease() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
        git status
        git add -A
        git commit -m \"New Release $_prefix-$_release_version\"
        git tag "$_prefix-$_release_version"
}

##
## Launching the server.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
startBloatitServer() {
    log_date "Starting the elveos server." $_log_file
    local _log_file="$1"
    (
        /etc/init.d/elveos start
        [ $? = 0 ] || exit_fail

    ) | tee -a $_log_file
}



commitPrerelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION"

stopBloatitServer "$LOG_FILE"

propagateConfFiles "$LOG_FILE" "$UP_CONF_DIR" "$CONF_DIR" "$UP_SHARE_DIR" "$SHARE_DIR" "$UP_RESSOURCES" "$CLASSES"

migratingDB "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$LIQUIBASE_DIR" "$USER"

commitRelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION"


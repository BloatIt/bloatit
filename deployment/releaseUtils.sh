#!/bin/bash

exit_ok(){
    echo "exiting"
    exit 0
}

exit_fail(){
    echo "Failure. Abording know ! "
    kill $$
    exit 128
}

abort(){
    echo "aborting"
    exit 255
}

abort_if_non_zero(){
    if [ "$1" != "0" ] ; then 
        abort
    fi
}

##
## Perform the mvn release.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "bloatit").
##    RELEASE_VERSION : the version string of the release.
##    NEXT_SNAPSHOT_VERSION : the version of the next snapshot.
##    MVN : the mvn command to launch (for example "mvn -f ../pom.xml") 
performMvnRelease() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
    local _next_snapshot_version="$4"
    local _mvn="$5"
    log_date "Make a mvn release." $_log_file
    (
        $_mvn release:clean
        $_mvn install -Dmaven.test.skip=true
        $_mvn --batch-mode -Dtag=$_prefix-$_release_version release:prepare \
                          -DreleaseVersion=$_release_version \
		          -DdevelopmentVersion=$_next_snapshot_version-SNAPSHOT \
		          -DautoVersionSubmodules=true \
        && $_mvn release:clean
    # Do not do the perform.
    # Just clean if there is no errors
    
       [ "$?" = "0" ] || exit_fail
    
    ) | tee -a $_log_file
}

##
## Send newly added data to the distant server
##
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    HOST : where to transfer the data (for example linkeos.com)
##    REPOS_DIR : the bloatit root dir.
##    USER : the distant user owning the bloatit server.
transferData() {
    local _log_file="$1"
    local _host="$2"
    local _repos_dir="$3"
    local _user="$4"
    ./transfert.sh -d $_host -l $_log_file -s $_repos_dir -n $_user
    [ $? = 0 ] || exit_fail
}

##
## Commit the git distant new data
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "bloatit").
##    RELEASE_VERSION : the version string of the release.
##    SSH : the ssh command to launch (do not forget the "-t"). For example: "ssh -t -l bloatit linkeos.org"
commitPrerelease() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
    local _ssh="$4"
    $_ssh "
        git status
        git add -A
        git commit -m \"New PreRelease $_prefix-$_release_version\"
    "
}

##
## Stopping the server.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    SSH : the ssh command to launch (do not forget the "-t"). For example: "ssh -t -l bloatit linkeos.org"
stopBloatitServer() {
    local _log_file="$1"
    local _ssh="$2"
    log_date "Stopping the bloatit server." $_log_file
    (
        $_ssh "/etc/init.d/bloatit stop && sleep 2"
        [ $? = 0 ] || exit_fail
    ) | tee -a $_log_file
}

##
## Migrating DB.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "bloatit").
##    RELEASE_VERSION : the version string of the release.
##    LIQUIBASE_JAR : the full path to the liquibase-core jar.
##    USER : the bloatit user
##    SSH : the ssh command to launch ("-t" requiered). For example: "ssh -t -l bloatit linkeos.org"
migratingDB() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
    local _liquibase="$4"
    local _user="$5"
    local _ssh="$6"

    local _classpath="."
    _classpath="$_classpath:/home/$_user/jars/dom4j-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/postgresql-8.4-701.jdbc4.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-api-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-log4j12-1.5.8.jar"

    log_date "Migrating the DB." $_log_file
    (
        cat $_liquibase | $_ssh "
            cat > /tmp/$(basename $_liquibase)
            cd /home/$_user/java/

            java -jar /tmp/$(basename $_liquibase) --classpath=$_classpath update
            java -jar /tmp/$(basename $_liquibase) --classpath=$_classpath tag "$_prefix-$_release_version"

            rm /tmp/$(basename $_liquibase)
        "
        [ $? = 0 ] || exit_fail
    ) | tee -a $_log_file
}

##
## Perform a revert
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "bloatit").
##    RELEASE_VERSION : the version string of the release.
##    LIQUIBASE_JAR : the full path to the liquibase-core jar.
##    USER : the bloatit user
##    SSH : the ssh command to launch ("-t" requiered). For example: "ssh -t -l bloatit linkeos.org"
revert() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
    local _liquibase="$4"
    local _user="$5"
    local _ssh="$6"

    local _classpath="."
    _classpath="$_classpath:/home/$_user/jars/dom4j-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/postgresql-8.4-701.jdbc4.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-api-1.6.1.jar"
    _classpath="$_classpath:/home/$_user/jars/slf4j-log4j12-1.5.8.jar"

    log_date "Reverting to git tag: $_prefix-$_release_version" $_log_file
    (
        $_ssh "
	    git checkout \"$_prefix-$_release_version\"
	    git checkout -b \"$_prefix-$_release_version-branch\"
        "
        [ $? = 0 ] || exit_fail
    )| tee -a $_log_file

    log_date "Reverting the db to tag: $_prefix-$_release_version" $_log_file
    (
        cat $_liquibase | $_ssh "
            cat > /tmp/$(basename $_liquibase)
            cd /home/$_user/java/

            java -jar /tmp/$(basename $_liquibase) --classpath=$_classpath rollback "$_prefix-$_release_version"

            rm /tmp/$(basename $_liquibase)
        "
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
##    SSH : the ssh command to launch ("-t" requiered). For example: "ssh -t -l bloatit linkeos.org"
propagateConfFiles() {
    local _log_file="$1"
    local _up_conf_dir="$2"
    local _conf_dir="$3"
    local _up_share_dir="$4"
    local _share_dir="$5"
    local _up_ressources="$6"
    local _classes="$7"
    local _ssh="$8"
log_date "Merging the conf files." $LOG_FILE
(
_merge_file_script=mergeFiles.sh
cat ./$_merge_file_script | $_ssh "cat > /tmp/$_merge_file_script"

$_ssh "
# .config files
bash /tmp/$_merge_file_script $_up_conf_dir $_conf_dir
# .local/share files
bash /tmp/$_merge_file_script $_up_share_dir $_share_dir
# ressources files
bash /tmp/$_merge_file_script $_up_ressources $_classes

rm /tmp/$_merge_file_script
"
    [ $? = 0 ] || exit_fail
) | tee -a $_log_file
}

##
## Commit the git distant new data
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    PREFIX : the tag prefix name (For example "bloatit").
##    RELEASE_VERSION : the version string of the release.
##    SSH : the ssh command to launch (do not forget the "-t"). For example: "ssh -t -l bloatit linkeos.org"
commitRelease() {
    local _log_file="$1"
    local _prefix="$2"
    local _release_version="$3"
    local _ssh="$4"
    $_ssh "
        git status
        git add -A
        git commit -m \"New Release $_prefix-$_release_version\"
        git tag "$_prefix-$_release_version"
    "
}

##
## Launching the server.
## Ordered parameters :
##    LOG_FILE : the file where to append log output.
##    SSH : the ssh command to launch (do not forget the "-t"). For example: "ssh -t -l bloatit linkeos.org"
startBloatitServer() {
    log_date "Starting the bloatit server." $_log_file
    local _log_file="$1"
    local _ssh="$2"
    (
        $_ssh "/etc/init.d/bloatit start"
        [ $? = 0 ] || exit_fail
    ) | tee -a $_log_file
}


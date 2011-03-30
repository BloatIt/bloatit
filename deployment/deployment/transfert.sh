#!/bin/sh

usage()
{
cat << EOF
usage: $0 -d host -l log [ -s source ] [-n name] [-h] 

This script transfer data from a local repository to a distant server.

OPTIONS:
   -h      Show this message. 
   -d      Destination host. Requiered.
   -l      Log file name, messages will be append to the file. Requiered.
   -s      Source folder (Bloatit root git repo). Default is "$PWD".
   -n      Distant user name. Default is "elveos".
EOF
}

#
# Parsing the arguments
#

HOST=
LOG_FILE=
DISTANT_NAME=elveos
SOURCE=$PWD

while getopts "hd:l:s:n:" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         d)
             HOST=$OPTARG
             ;;
         l)
             LOG_FILE=$OPTARG
             ;;
         s)
             SOURCE=$OPTARG
             ;;
         n)
             DISTANT_NAME=$OPTARG
             ;;
         ?)
	     usage 1>&2
             exit
             ;;
     esac
done

if [ -z "$DISTANT_NAME" ] || [ -z "$LOG_FILE" ]
then
	echo "Arguments are missing !!! \n" 1>&2
	usage 1>&2
	exit 1
fi


DEST=$DISTANT_NAME@$HOST:
SOURCE_CLASSES=$SOURCE/main/target/classes/
SOURCE_DEPENDENCIES=$SOURCE/main/target/dependencies/
SOURCE_WWW=$SOURCE/www/
SOURCE_UP_CONF_DIR=$SOURCE/etc/
SOURCE_UP_SHARE_DIR=$SOURCE/share/
SOURCE_LIGHTTPD=$SOURCE/lighttpd/
SOURCE_DOC=$SOURCE/doc/websitedoc/

# Include the global conf and the log system.
. $PWD/conf.sh
. $PWD/log.sh

# Create a custom rsync launcher

#
# --progress: Show a the progression on cout.
# -h -h: show size in 1024 multiples.
# --recursve --exclude: exlude recursively some files.
# --times: keep the modification times.
# --links: send the links.
# --delete: remove non present file.
# --perms --chmod: apply permission on each files.
#

RSYNC=" rsync -v4 -h -h --log-file=$LOG_FILE \
	--recursive --exclude \"pom.xml\" --exclude \"*~\" \
	--compress --skip-compress=gz/jpg/mp[34]/7z/bz2/jar --rsh=/usr/bin/ssh \
	--times  --links --delete "
# keep rights on directories for user.
# other and groups have no rights.
# and make sure no file is executable.
PERMS=" --perms --chmod=og=,Fu+r,F-x"

#
# Send data
#

# Send the dependencies (mostly it is some jars)
log_date "Sending jar dependencies to $DEST$DEPEDENCIES" $LOG_FILE
$RSYNC $PERMS $SOURCE_DEPENDENCIES $DEST$DEPEDENCIES

# Send the classes from de bloatit project (not the father !)
# Every xml and properties in the root folder files are ignored.
# Even in the SOURCE (not deleted or modified)
log_date "Sending bloatit classes to $DEST$CLASSES" $LOG_FILE
$RSYNC $PERMS --exclude="$DEPEDENCIES" --include="*/**" --exclude="*.xml" --exclude="*.properties" $SOURCE_CLASSES $DEST$CLASSES

# Take the xml and properties file in the root CLASS folder and put them into 
# the RESSOURCE folder. 
# This is done to make sure local modif are not errased. 
log_date "Sending bloatit ressources to $DEST$UP_RESSOURCES" $LOG_FILE
$RSYNC $PERMS --include="*.xml" --include="*.properties" --exclude="**" $SOURCE_CLASSES $DEST$UP_RESSOURCES

# Send the data in www folder
log_date "Sending 'www' folder to $DEST$WWW" $LOG_FILE
$RSYNC  --perms --chmod=o=,Fug+r,F-x $SOURCE_WWW $DEST$WWW

# Send the data in etc folder (in a temporary directory)
log_date "Sending 'etc' folder to $DEST$UP_CONF_DIR" $LOG_FILE
$RSYNC $PERMS $SOURCE_UP_CONF_DIR $DEST$UP_CONF_DIR

# Send the data in share folder (in a temporary directory)
log_date "Sending 'share' folder to $DEST$UP_SHARE_DIR" $LOG_FILE
$RSYNC $PERMS $SOURCE_UP_SHARE_DIR $DEST$UP_SHARE_DIR

# Send the lighttpd conf
log_date "Sending lighttpd conf files $DEST$LIGHTTPD" $LOG_FILE
$RSYNC $PERMS $SOURCE_LIGHTTPD $DEST$LIGHTTPD

# Send the website doc files
log_date "Sending websit doc files $DEST$DOC" $LOG_FILE
$RSYNC --perms --chmod=o=,Fug+r,F-x  $SOURCE_DOC $DEST$DOC


#!/bin/sh

usage()
{
cat << EOF
usage: $0 -d host [ -s source ] [-n name] [-h] 

This script transfer data from a local repository to a distant server.

OPTIONS:
   -h      Show this message. 
   -d      Destination host. Requiered.
   -s      Source folder (Bloatit root git repo). Default is "$PWD".
   -n      Distant user name. Default is "elveos".
EOF
}

# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
COMMONS=$ROOT/../commons/

# Add the includes 
. $COMMONS/includes.sh

#
# Parsing the arguments
#
HOST=
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
if [ -z "$DISTANT_NAME" ] 
then
	error "Arguments are missing !!! \n" 1>&2
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

RSYNC=" rsync -v4 -h -h \
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
log_date "Sending jar dependencies to $DEST$DEPEDENCIES"
$RSYNC $PERMS $SOURCE_DEPENDENCIES $DEST$DEPEDENCIES
exit_on_failure $?

# Send the classes from de bloatit project (not the father !)
# Every xml and properties in the root folder files are ignored.
# Even in the SOURCE (not deleted or modified)
log_date "Sending bloatit classes to $DEST$CLASSES" 
$RSYNC $PERMS --exclude="$DEPEDENCIES" --include="*/**" --exclude="*.xml" --exclude="*.properties" $SOURCE_CLASSES $DEST$CLASSES
exit_on_failure $?

# Take the xml and properties file in the root CLASS folder and put them into 
# the RESSOURCE folder. 
# This is done to make sure local modif are not errased. 
log_date "Sending bloatit ressources to $DEST$UP_RESSOURCES"
$RSYNC $PERMS --include="*.xml" --include="*.properties" --exclude="**" $SOURCE_CLASSES $DEST$UP_RESSOURCES
exit_on_failure $?

# Send the data in www folder
log_date "Sending 'www' folder to $DEST$WWW"
$RSYNC  --perms --chmod=o=,Fug+r,F-x $SOURCE_WWW $DEST$WWW
exit_on_failure $?

# Send the data in etc folder (in a temporary directory)
log_date "Sending 'etc' folder to $DEST$UP_CONF_DIR"
$RSYNC $PERMS $SOURCE_UP_CONF_DIR $DEST$UP_CONF_DIR
exit_on_failure $?

# Send the data in share folder (in a temporary directory)
log_date "Sending 'share' folder to $DEST$UP_SHARE_DIR"
$RSYNC $PERMS $SOURCE_UP_SHARE_DIR $DEST$UP_SHARE_DIR
exit_on_failure $?

# Send the website doc files
log_date "Sending websit doc files $DEST$DOC"
$RSYNC --perms --chmod=o=,Fug+r,F-x  $SOURCE_DOC $DEST$DOC
exit_on_failure $?

success "File sent."

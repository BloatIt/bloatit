#!/bin/bash

usage(){
    cat << EOF
    This script backup distant elveos resources.

    OPTIONS:
    -b | -r            : -b backup / -r restore
    -d Host            : The distant host where is the db.
    -n User name       : the user name to connect to on the distant host
    -e encrypting user : the encrypting user email (gpg must have its key)
    -f folder name     : the local folder name in which the backup is made.
EOF
}

exit_fail(){
    echo failure !
    exit
}

HOST=elveos.org
USER=elveos
ENCRYPT_USER=thomas@elveos.org
FILE_NAME=$HOST.resources-backup
BACKUP_DIR=.local/share/bloatit/
while getopts "d:n:e:f:brh" OPTION
do
     case $OPTION in
         d)
             HOST=$OPTARG
             ;;
         n)
             USER=$OPTARG
             ;;
         e)
             ENCRYPT_USER=$OPTARG
             ;;
         f)
             FILE_NAME=$OPTARG
             ;;
         b)
             OPERATION=backup
             ;;
         r)
             OPERATION=restore
             ;;
         h)
             usage
             exit 1
             ;;
         ?)
	     usage 1>&2
             exit
             ;;
     esac
done

#
# --progress: Show a the progression on cout.
# -h -h: show size in 1024 multiples.
# --recursve --exclude: exlude recursively some files.
# --times: keep the modification times.
# --links: send the links.
# --delete: remove non present file.
# --perms: apply permission on each files.
#

RSYNC=" rsync -v4 -h -h \
	--recursive --exclude \"*~\" \
	--compress --skip-compress=gz/jpg/mp[34]/7z/bz2/jar --rsh=/usr/bin/ssh \
	--times  --links --delete --perms "

if [ "$OPERATION" = backup ] ; then
    $RSYNC $USER@$HOST:$BACKUP_DIR $FILE_NAME.tmp
    tar -cJ $FILE_NAME.tmp | gpg --output $FILE_NAME --encrypt --recipient $ENCRYPT_USER
    rm -rf $FILE_NAME
fi

if [ "$OPERATION" = restore ] ; then
    TMP_DIR=/tmp/restore
    if [ ! -e $TMP_DIR ] ; then 
        mkdir -p $TMP_DIR
    fi

    # WARNING recovery to the home dir !
    gpg --decrypt $FILE_NAME | ( cd $TMP_DIR && tar -xvJf - )
    $RSYNC $TMP_DIR/ $USER@$HOST:

    rm -rf $TMP_DIR
fi

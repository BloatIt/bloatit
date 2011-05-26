#!/bin/bash

cat << EOF > /dev/null
This script backup the database and the .local/share/bloatit folder.
It send the backuped file using scp.
You can easily add a new address where to send the backuped files using the SEND_HOSTS variable.

REMEMBER:
Each SEND_HOSTS must have their ssh public keys authorized.
You must have the gpg public key for encrypting the content (do not forget to sign it)
EOF

USER=administrator
WORKDIR=/tmp/backup

# DB backup
POSTGRES_ROOT=postgres
DB_NAME=elveos
DB_BACKUP_FILE="elveos.org-DB-$(date +%y-%m-%d-%R)"

# .local/share/bloatit backup
SHARE_FOLDER_ROOT=/home/elveos/
SHARE_FOLDER=.local/share/bloatit/
SHARE_BACKUP_FILE="elveos.org-SHARE-$(date +%y-%m-%d-%R)"

# /var/lib/aide/aide.db.new backup
AIDE_DB=/var/lib/aide/aide.db.new
AIDE_DB_BACKUP_FILE="elveos.org-AIDE-$(date +%y-%m-%d-%R)"

# Encrypt with
ENCRYPT_USERS=( "thomas@elveos.org" "fred.bertolus@gmail.com" "yoann@linkeos.com" )

# Send backup to these hosts.
SEND_HOSTS=( "elveos-backup@f2.b219.org:" "elveos-backup@b219.org:" )

if [ "$(id -u)" != "0" ] ; then 
    echo "You must be root to use this script"
    exit
fi

echo "Going to workdir"
if [ ! -e $WORKDIR ] ; then 
    mkdir -p $WORKDIR
fi
cd $WORKDIR

for i in ${ENCRYPT_USERS[@]} ; do 
    recipients="$recipients --recipient $i"
done

echo "Dump the db, encrypt it with $ENCRYPT_USER"
su $POSTGRES_ROOT -c "pg_dump -Fc $DB_NAME -o" | \
    su $USER -c "gpg --output \"$DB_BACKUP_FILE\" --encrypt $recipients"

echo "encrypting .local/share files"
cd $SHARE_FOLDER_ROOT
tar -cJ "$SHARE_FOLDER" | su $USER -c "gpg --output \"$WORKDIR/$SHARE_BACKUP_FILE\" --encrypt $recipients" 

for i in ${SEND_HOSTS[@]} ;  do
    su $USER -c "
	scp \"$WORKDIR/$DB_BACKUP_FILE\"             \
            \"$WORKDIR/$SHARE_BACKUP_FILE\"          \
            \"$i\"
     "

    su $USER -c "
	scp \"$AIDE_DB\"             \
            \"$i$AIDE_DB_BACKUP_FILE\"
     "
done

# deleting tmp files
rm -rf $WORKDIR/*


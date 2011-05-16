
usage(){
    cat << EOF
    This script backup a distant postgresql database.
    You must have pg_dump and pg_restore locally installed.

    OPTIONS:
    -b | -r            : -b backup / -r restore
    -d Host            : The distant host where is the db.
    -n User name       : the user name to connect to on the distant host
    -a db name         : the name of the db we want to backup
    -u db user name    : the user name on postgresql to use
    -e encrypting user : the encrypting user email (gpg must have its key)
    -l local port      : the local port on which we will make the ssh tunnel
    -t distant port    : the distant port to connect on the DB.
    -f file name       : the local file name on which the backup is made.
EOF
}


exit_fail(){
    echo failure !
    exit
}

HOST=elveos.org
USER=elveos
DB_NAME=elveos
DB_USER=elveos
ENCRYPT_USER=thomas@elveos.org
LOCAL_PORT=8888
DISTANT_PORT=5432
FILE_NAME=$HOST.db-backup
while getopts "d:n:a:u:e:l:t:f:brh" OPTION
do
     case $OPTION in
         d)
             HOST=$OPTARG
             ;;
         n)
             USER=$OPTARG
             ;;
         a)
             DB_NAME=$OPTARG
             ;;
         u)
             DB_USER=$OPTARG
             ;;
         e)
             ENCRYPT_USER=$OPTARG
             ;;
         l)
             LOCAL_PORT=$OPTARG
             ;;
         t)
             DISTANT_PORT=$OPTARG
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

SSH="ssh -L localhost:$LOCAL_PORT:localhost:$DISTANT_PORT $USER@$HOST -N -f"

tunnel(){
    echo "Create the ssh tunnel"
    $SSH
    [ "$?" = 0 ] || exit_fail
}

untunnel(){
    echo "Killing ssh tunnel"
    kill $(ps -fe | grep "$SSH" | grep -v grep | tr -s " " | cut -d " " -f 2)
}

if [ $OPERATION = backup ] ; then 
    tunnel

    echo "Dump the db, encrypt it with $ENCRYPT_USER"
    pg_dump -Fc $DB_NAME -h localhost -p $LOCAL_PORT -U $DB_USER -o | \
        gpg --output $FILE_NAME --encrypt --recipient $ENCRYPT_USER

    untunnel

fi

if [ $OPERATION = restore ] ; then
    tunnel

    gpg --decrypt $FILE_NAME | \
        pg_restore -d $DB_NAME -h localhost -p $LOCAL_PORT -U $DB_USER 

    untunnel
fi

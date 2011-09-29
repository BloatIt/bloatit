#!/bin/bash

# Usage / documentation
usage()
{
cat << EOF
usage: $0 { -d | -m | -a }  -s stat 
       $0 -h 

This script generate a state table for a period.

OPTIONS:
   -h      Show this message. 
   -d      2 Day (hour by hour)
   -m      Monthly (day by day)
   -a      sum on all time
   -s      the stat type (can be: ??)
EOF
}

#
# Parsing the arguments
#
PERIOD=
STAT=
OUTPUT=
TABLE_DIR=tables
SQL_DIR=sql
DB=~/.local/share/bloatit/stats.db
while getopts "hdmas:o:" OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         d)
             PERIOD=daily
             ;;
         m)
             PERIOD=monthly
             ;;
         a)
             PERIOD=all
             ;;
         s)
             STAT=$OPTARG
             ;;
         ?)
         usage 1>&2
             exit
             ;;
     esac
done

[ -z "$PERIOD" ] && usage 1>&2 && exit
[ -z "$STAT" ] && usage 1>&2 && exit


if [ "$PERIOD" = "monthly" ] ; then
    cat << EOF | sqlite3 "$DB"
    CREATE TABLE IF NOT EXISTS mydates(time datetime);
    BEGIN;
    $(
            for i in $(seq 0 29) ; do 
            echo "INSERT INTO mydates (time) VALUES (datetime('now', '-30 days', 'start of day', '+$i days' ));"
            done
     )
    COMMIT;
EOF

elif [ "$PERIOD" = "daily" ] ; then
    cat << EOF | sqlite3 "$DB"
    CREATE TABLE IF NOT EXISTS mydates(time datetime);
    BEGIN;
    $(
            for i in $(seq 0 47) ; do 
            echo "INSERT INTO mydates (time) VALUES (strftime('%Y-%m-%d %H:00:00', datetime('now', '-2 days', '+$i hours' )));"
            done
     )
    COMMIT;
EOF
fi

cat $SQL_DIR/$STAT-$PERIOD.sql | sqlite3 "$DB"


sqlite3 "$DB" "delete from mydates;"

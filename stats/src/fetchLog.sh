#!/bin/bash

# make sure to launch directly in this folder !
# DO NOT MOVE ME !

DB_PATH=~/.local/share/bloatit/stats.db
export PYTHONPATH=$PYTHONPATH:$PWD

LAST_FETCH_FILE=".$(id -u -n).$(ip addr | grep link/ether | grep -o -E "..:..:..:..:..:.." | head -1).lastFetch"


if [ ! -e $DB_PATH ] ; then
    RESET="rm $LAST_FETCH_FILE"
fi

ssh elveos@elveos.org "
cd ~/.local/share/bloatit/log/
$RESET
if [ -e $LAST_FETCH_FILE ] ; then
   find . -newer $LAST_FETCH_FILE -iname 'infos.log*' -exec grep 'Access:' {} \;
else 
   find . -iname 'infos.log*' -exec grep 'Access:' {} \; 
fi
[ $? = 0 ] && touch $LAST_FETCH_FILE 
" | sed -E "s/(2011-..-..) (..:..:..),.../\\1T\\2/g"|  python bloatitstats/filldb.py -d $DB_PATH

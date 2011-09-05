#!/bin/bash

# make sure to launch directly in this folder !
# DO NOT MOVE ME !

export PYTHONPATH=$PYTHONPATH:$PWD

LAST_FETCH_FILE=".$(id -u -n).$(ip addr | grep link/ether | grep -o -E "..:..:..:..:..:.." | head -1).lastFetch"

ssh elveos@elveos.org "
cd ~/.local/share/bloatit/log/ 
if [ -e $LAST_FETCH_FILE ] ; then
   find . -newer $LAST_FETCH_FILE -iname 'infos.log*' -exec grep 'Access:' {} \;
else 
   find . -iname 'infos.log*' -exec grep 'Access:' {} \; 
fi
[ $? = 0 ] && touch $LAST_FETCH_FILE 
" | sed -E "s/(2011-..-..) (..:..:..)/\\1T\\2/g"|  python bloatitstats/filldb.py -d ~/.local/share/bloatit/stats.db

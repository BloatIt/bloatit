#!/bin/bash

if [ -z "$1" ] ; then
cat << EOF

$0: Add the elveos script in /etc/init.d
-----------------------------
### Usage

You must export the FILE_DIR variable so that this script know where to find the default conf files (rewrite and fastcgi)

EOF

elif [ "$1" = "exec" ] ; then
    sudo cp $FILE_DIR/elveos /etc/init.d/
fi

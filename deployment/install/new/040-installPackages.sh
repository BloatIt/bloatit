#!/bin/bash

if [ "$1" = message ] ; then 

cat << EOF
$0: Install the needed packages.

Some basic packages like vim, less, bash-completion
Working packages like postgresql lighttpd default-jre-headless 
Utils packages like zip unzip git-core rsync 
EOF

elif [ "$1" = exec ] ; then 
    sudo apt-get install vim less git-core bash-completion 
    sudo apt-get install postgresql lighttpd openssh-server default-jre-headless rsync

    # Utils
    sudo apt-get install zip unzip
else 
    echo "Parameter must be 'message' or 'exec'"
fi


#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Versionning of the home directory using git
----------------------

### Usage 

You must export the USER variable. (export USER=...)

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi

    sudo su -c '
    cd
    # configure the git for bloatit home
    cat << "EOF" > .gitignore
# Ignoring bash elements
.bash*
.profile
# Ignoring ssh
.ssh
# others
.lesshst
.vim*
.upload
# functioning
.local/share/bloatit/**
EOF

cat << "EOF" > .gitconfig
[user]
        name = $USER
        email = $USER@linkeos.com
[color]
        ui = true
EOF

git init
git add -A
git commit -m "first commit"
' $USER

fi

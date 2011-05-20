#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Versionning of the home directory using git
----------------------

### Usage 

    $0 [username]: If *username* is specified then the script is launch for the user *username*

EOF

else
    USER="$1"

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

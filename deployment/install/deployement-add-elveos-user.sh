#!/bin/bash

USER=elveos

add_user(){
    sudo adduser $USER 
}

www_in_user_group(){
    sudo addgroup www-data $USER
}

create_directories(){
sudo su -c '
mkdir .upload
mkdir -p .local/share/bloatit/lucene
mkdir -p .local/share/bloatit/log
mkdir -p .config/bloatit
mkdir www
' $USER
}

set_right_restrictive(){
sudo su -c '
find . -type f -execdir chmod 440 {} \;
find . -type d -execdir chmod 710 {} \;
' $USER
}

set_right_dangerous(){
sudo su -c '
find . -type f -execdir chmod 666 {} \;
find . -type d -execdir chmod 777 {} \;
' $USER
}

git_versionning_of_home(){
sudo su -c '
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
}

usage(){
cat << EOF
usage: $0 { -s | -c } [-u username] 

Create a user and add the right folder

OPTIONS:
   -h      Show this message. 
   -u      specify a username (default=elveos)
   -c      Complet: for a complete deployment (rigorous right etc.)
   -s      Simplified: for a simple deployment (no user creation. no git versionning. simple rights"
EOF

}

while getopts "u:sc" OPTION
do
     case $OPTION in
         h)
             usage
             ;;
         u)
             USER=$OPTARG
             ;;
         c)
             MODE=complet
             ;;
         s)
             MODE=simple
             ;;
         ?)
	     usage 1>&2
             exit
             ;;
     esac
done

if [ "$MODE" = "complet" ] ; then
   add_user
   www_in_user_group
   create_directories
   set_right_restrictive
   git_versionning_of_home
elif [ "$MODE" = "simple" ] ; then
   www_in_user_group
   create_directories
   set_right_dangerous
else 
   usage
fi


#!/bin/bash

USER=elveos

if [ -n "$3" ] ; then
    USER="$3"
fi

add_user(){
    sudo adduser $USER 
}

www_in_user_group(){
    sudo addgroup www-data $USER
}

create_directories(){
    sudo su -c '
    cd
    mkdir .upload
    mkdir -p .local/share/bloatit/lucene
    mkdir -p .local/share/bloatit/log
    mkdir -p .config/bloatit
    mkdir www
    ' $USER
}

set_right_restrictive(){
    sudo su -c '
    cd
    find . -type f -execdir chmod 440 {} \;
    find . -type d -execdir chmod 710 {} \;
    ' $USER
}

set_right_dangerous(){
    sudo su -c '
    cd
    find . -type f -execdir chmod 666 {} \;
    find . -type d -execdir chmod 777 {} \;
    ' $USER
}

allow_ssh_connection(){
    read -p "Authorize the $USER user to use ssh. Past your public key: " key
    sudo su -c "
    cd 
    mkdir .ssh/
    echo \"$key\" >> ~/.ssh/authorized_keys
    " $USER
}

git_versionning_of_home(){
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
}

if [ -z "$1" ] ; then
    cat << EOF
$0 [exec action [username]]: Create a new user and configure its home directory
    exec: execute the script
    action: 
        create -> create the new user
        addWWWGroup -> add the www-data user into the new user group (So that the www-data user can read the home of the new user)
        createDirs -> create the 'standard' directories in the user home.
        setRights -> set the rights of all the files in the user home.
        allowSSH  -> Allow ssh the new user to connect by ssh.
        homeGit   -> version the home with git.
    username: default is elveos.


- Every folder are rxwr-x---
- Every files are  r--r-----
EOF

elif [ "$1" = "exec" ] ; then 

    case "$2" in 
        create)
            add_user
            ;;
        addgroup)
            addgroup
            ;;
        createDirs)
            create_directories
            ;;
        setRights)
            set_right_restrictive
            ;;
        allowSSH)
            allow_ssh_connection
            ;;
        homeGit)
            git_versionning_of_home
            ;;
    esac
fi

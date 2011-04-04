#!/bin/bash

configure_sshd(){
    local SSHD_CFG=/etc/ssh/sshd_config

    sudo sed -i -r '/RSAAuthentication/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG
    sudo sed -i -r '/PubkeyAuthentication/ s/#?(.*) no$/\1 yes/g' $SSHD_CFG
    sudo sed -i -r '/IgnoreUserKnownHosts/ s/#?(.*) no$/\1 yes/g' $SSHD_CFG
    sudo sed -i -r '/PasswordAuthentication/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG
    sudo sed -i -r '/X11Forwarding/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG

    cat << EOF
# Restrict the openSSH server :
# - authentication by public key only
# - No X11 forwarding
# - No known host trusting.

Other tips:

-> ListenAddress 192.168.0.1
Ne faîtes écouter ssh que sur une interface donnée, 

-> PermitRootLogin no
Essayez autant que possible de ne pas autoriser de connexion en tant que root.

-> Port 666 ou ListenAddress 192.168.0.1:666
Change le port d'écoute, ainsi l'intrus ne peut être complètement sûr de l'exécution d'un démon sshd (soyez prévenus, c'est de la sécurité par l'obscurité).

-> PermitEmptyPasswords no
Les mots de passe vides sont un affront au système de sécurité.

-> AllowUsers alex ref
Autorise seulement certains utilisateurs à avoir accès via ssh à cette machine. user@host peut également être utilisé pour n'autoriser l'accès qu'à un utilisateur donné depuis un hôte donné.

-> AllowGroups wheel admin
Autorise seulement certains membres de groupes à avoir accès via ssh à cette machine. AllowGroups et AllowUsers ont des directives équivalentes pour interdire l'accès à la machine. Sans surprise elles s'appellent « DenyUsers » et « DenyGroups ».
EOF

    read

    sudo vim /etc/ssh/sshd_config

    sudo service ssh restart
}

# This scrip also add to the current user a public key into its ssh authorized_keys.
add_trusted_key(){
    mkdir ~/.ssh
    echo "$1" >> ~/.ssh/authorized_keys
}

if [ "$1" = "addKey" ] ; then 
    if [ -n "$2" ] ; then
        add_trusted_key "$2"
    else 
        echo "I need a key ..."
    fi
fi

if [ "$1" = "configure" ] ; then 
    configure_sshd
fi

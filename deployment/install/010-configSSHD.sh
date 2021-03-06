#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF

$0: Configuration du serveur openSSH.
-------------------------------------

### description

Par défaut ce script ajoute les configurations suivante: 

 * authentication by public key only
 * No X11 forwarding
 * No known host trusting.

Permet la modification du fichier de conf sshd à la main.


### Tips

Voici plusieurs autres configuration qui pourrait être intéressant de mettre en place:

 * ListenAddress 192.168.0.1
Ne faites écouter ssh que sur une interface donnée, 

 * PermitRootLogin no
Essayez autant que possible de ne pas autoriser de connexion en tant que root.

 * Port 666 ou ListenAddress 192.168.0.1:666
Change le port d'écoute, ainsi l'intrus ne peut être complètement sûr de l'exécution d'un démon sshd (soyez prévenus, c'est de la sécurité par l'obscurité).

 * PermitEmptyPasswords no
Les mots de passe vides sont un affront au système de sécurité.

 * AllowUsers alex ref
Autorise seulement certains utilisateurs à avoir accès via ssh à cette machine. user@host peut également être utilisé pour n'autoriser l'accès qu'à un utilisateur donné depuis un hôte donné.

 * AllowGroups wheel admin
Autorise seulement certains membres de groupes à avoir accès via ssh à cette machine. AllowGroups et AllowUsers ont des directives équivalentes pour interdire l'accès à la machine. Sans surprise elles s'appellent « DenyUsers » et « DenyGroups ».

EOF

elif [ "$1" = exec ] ; then 
    SSHD_CFG=/etc/ssh/sshd_config

    echo "*** set RSAAuthentication to 	'no'"
    sudo sed -i -r '/RSAAuthentication/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG
    echo "*** set PublikeyAuthentication to 'yes'"
    sudo sed -i -r '/PubkeyAuthentication/ s/#?(.*) no$/\1 yes/g' $SSHD_CFG
    echo "*** set IgnoreUserKnownHosts to 'yes'"
    sudo sed -i -r '/IgnoreUserKnownHosts/ s/#?(.*) no$/\1 yes/g' $SSHD_CFG
    echo "*** set PasswordAuthentication to 'no'"
    sudo sed -i -r '/PasswordAuthentication/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG
    echo "*** set X11Forwarding to 'no'"
    sudo sed -i -r '/X11Forwarding/ s/#?(.*) yes$/\1 no/g' $SSHD_CFG

    read -p "Config the $SSHD_CFG (press return to continue)"
    sudo vim $SSHD_CFG
    sudo service ssh restart

else 
    echo "Parameter must be empty or 'exec'"
fi


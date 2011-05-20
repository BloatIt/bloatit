#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: Ajoute une clef public dans la liste des clef ssh
-------------------------------------------------

### Usage

    $0 [exec]
            utiliser le parametre "exec" pour executer le script.
    
### Explications

On vient de configurer ssh pour n'accepter que les connections autorisées par une clef public. 
On ajoute donc notre clef public dans la liste des clef autorisée.

EOF

elif [ "$1" = exec ] ; then 

    mkdir ~/.ssh
    read -p "Enter your public key: " _KEY
    echo "$_KEY" >> ~/.ssh/authorized_keys

else 
    echo "Parameter must be empty or 'exec'"
fi

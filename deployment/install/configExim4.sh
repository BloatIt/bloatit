#!/bin/bash

cat << EOF
We have to have a local mail server (For the mail reporting system etc.)
There are different possible choices, (procmail, qmail, sendmail, exim).
- Sendmail is old and complex (and not very secure)
- procmail is good but has a dependencie to python
- qmail is not realy maintained
- exim is the last one available...
So I choose exim4 (I think it's the default one on debian)

The objective is to collect all the mail sent locally and to transfer them to our google account.

So: No distant access is needed. We need to be able to do smtp. Google -> gpg would be good ...

EOF

install() {
    sudo apt-get install exim4
}

reconfigure() {

    ADDR_MAIL=noreply@elveos.org
    cat << EOF
    * Type de configuration: Envoi via relais (« smarthost ») - 
    * Nom de courriel du système: laisser la valeur proposée (nom de votre machine).
    *  Liste d'adresses IP où Exim sera en attente de connexions SMTP entrantes : 127.0.0.1 (pour que votre serveur ne soit pas utilisé par d'autres pc en réseau).
    * Autres destinations dont le courriel doit être accepté: Laisser par défaut.
    * Machines à relayer: Laisser vide.
    * Nom réseau ou adresse IP du système « smarthost »: Pour Gmail mettre: smtp.gmail.com::587 (Attention: le caractère ':' est répété deux fois). Pour un autre fournisseur, mettre l'adresse du serveur SMTP distant et éventuellement un numéro de port.

EOF
    sudo dpkg-reconfigure exim4-config

    cat << EOF 
    Ensuite il faut ajouter dans le fichier /etc/exim4/passwd.client lal ligne suivante:
    gmail-smtp-msa.l.google.com:$ADDR_MAIL:votre_motDePasse
EOF

    stty -echo
    read -p "Mot de passe pour $ADDR_MAIL: " _password
    echo
    stty echo

    sudo sh -c "echo \"gmail-smtp-msa.l.google.com:$ADDR_MAIL:$_password\" >> /etc/exim4/passwd.client"
}

install
reconfigure



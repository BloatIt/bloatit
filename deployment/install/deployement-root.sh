#!/bin/$SHELL

SHELL=bash
ROOT=./install

. $PWD/commons/includes.sh


echo
echo "This installer will install all the requiered dependencies to launch the Elveos server"

echo 
menu "Configuring the SSH server"
read -p "Do you want to upload a public key for auto-login ? (Y/n) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
   read -p "Paste your public key here: " key
   $SHELL $ROOT/deployement-ssh.sh addKey "$key"
fi
echo 
read -p "Do you want to restrict the rights of your ssh server ? (Y/n) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
   $SHELL $ROOT/deployement-ssh.sh configure
fi

echo 
menu "Configure filesystem"
read -p "Make the /tmp and /var noexec ? (y/N) " reponse
if [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
   $SHELL $ROOT/deployement-noexec.sh 
fi

echo 
menu "Install the missing package"
read -p "Install requiered packages ? (Y/n) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
   $SHELL $ROOT/deployement-install-packages.sh  install
fi 
read -p "Do you want to remove useless packages ? (y/N) " reponse
if [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
   $SHELL $ROOT/deployement-install-packages.sh purge
fi

USER=elveos
echo 
menu "Configure the 'elveos' user"
read -p "Create a elveos user ? (Y/n) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
   $SHELL $ROOT/deployement-add-elveos-user.sh -c $USER
else 
   read -p "Configure an other user ? (Y/n) " reponse
   if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
      read -p "Input the user name to use: " user
      if [ -n "$user" ] ; then
          USER=$user
      fi
      $SHELL $ROOT/deployement-add-elveos-user.sh -s $USER
   fi
fi 

echo 
menu "Configure postgres"
read -p "Configure the user access rights (Y/n) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
    read -p "Input the db user name: (default=$USER) " user
   if [ -n "$user" ] ; then
       USER=$user
   fi
    $SHELL $ROOT/deployement-postgres.sh right "$USER"
fi
conf_postgres(){
read -p "Add a database and a user into the postgre DB? (Y/n) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "y" ] || [ "$reponse" = "Y" ] ; then 
    read -p  "Input the db user name: (default=$USER) " user
   if [ -n "$user" ] ; then
       USER=$user
   fi
    stty -echo
    read -p "Input the password: " password ; echo
    stty echo
    if [ -n "$password" ] && [ -n "$USER" ] ; then 
        $SHELL $ROOT/deployement-postgres.sh addDB "$password" "$USER"
    else
        error "Password mustn't be empty !"
	conf_postgres
    fi
    password=
fi
}
conf_postgres

echo
menu "Configure lighttpd"
read -p "Skip this step ? (y/N) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "n" ] || [ "$reponse" = "N" ] ; then 
    read -e -p  "Where is the www directory ? " directory
    if [ -n "$directory" ] ; then 
        # Set the server document root.
        sudo sed -i -r "/server.document-root/ s#\\\"[a-zA-Z/]*\\\"#\\\"$directory\\\"#g" /etc/lighttpd/lighttpd.conf
    else 
        error "wwww directory is empty. skiping this step."
    fi
    echo "Adding the rewrite and fastcgi modules"
    sudo cp files/rewrite.lighttpd.conf /etc/lighttpd/conf-enabled/
    sudo cp files/fastcgi.conf /etc/lighttpd/conf-enabled/
    sudo service lighttpd restart
    success "done."
fi

echo
menu "Install the elveos start script"
read -p "Skip this step ? (y/N) " reponse
if [ -z "$reponse" ] || [ "$reponse" = "n" ] || [ "$reponse" = "N" ] ; then 
     sudo mv files/elveos /etc/init.d/
     success "done."
fi

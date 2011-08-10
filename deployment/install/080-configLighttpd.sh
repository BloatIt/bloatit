#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: Configure the lighttpd server.
-----------------------------

### Usage

You must export the FILE_DIR variable so that this script know where to find the default conf files (rewrite and fastcgi)

 - copy the generic rewrite file
 - copy the generic fastcgi file
 - copy the generic lighhtpd.conf file
 - create the certificat and ssl conf.
EOF

elif [ "$1" = exec ] ; then

    echo "*** configuring ssl (Creation of autosigned certificat) ..."
    sudo mkdir /etc/lighttpd/ssl/
    sudo cd /etc/lighttpd/ssl/
    sudo openssl req -new -x509 -keyout server.pem -out server.pem -days 3650 -nodes
    sudo chown www-data:www-data . -R

    echo "*** Adding the rewrite and fastcgi modules"
    sudo cp $FILE_DIR/rewrite.ssl.conf /etc/lighttpd/conf-enabled/
    sudo cp $FILE_DIR/fastcgi.conf /etc/lighttpd/conf-enabled/
    sudo cp $FILE_DIR/lighttpd.conf /etc/lighttpd/


    read -p "You can now edit the lighttpd.conf file. <press enter>"
    sudo vim /etc/lighttpd/lighttpd.conf

    sudo service lighttpd restart
    success "done."
fi



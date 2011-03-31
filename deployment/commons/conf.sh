#!/bin/sh

if [ -z $USER ] ; then
    echo YOU HAVE TO SET THE USER !! 
    exit 1
fi

# On the server, the file names are:
ROOT=/home/$USER/
UPLOAD_DIR=.upload
UP_RESSOURCES=$UPLOAD_DIR/ressources
UP_CONF_DIR=$UPLOAD_DIR/conf
UP_SHARE_DIR=$UPLOAD_DIR/share

LIGHTTPD=.lighttpd/
CONF_DIR=.config/bloatit/
SHARE_DIR=.local/share/bloatit/
DOC=wwwdoc/
CLASSES=java/
DEPEDENCIES=jars
WWW=www/


#!/bin/bash

if [ -z "$1" ] ; then 

cat << EOF
$0: Configure the lighttpd server.
-----------------------------

### Usage

You must export the FILE_DIR variable so that this script know where to find the default conf files (rewrite and fastcgi)

 - add the server.document.root informations
 - set the 404 error to /en/dopagenotfound (server.error-handler-404)
 - Add a redirect to redirect all wwww/ to ./
 - Activate the access logs (accesslog.filename)
 - copy the generic rewrite file
 - copy the generic fastcgi file

### Details:

    server.document.root = "/dir/..."
    server.error-handler-404 = "/en/dopagenotfound"
    
    # make an external redirect
    # from any www.host (with www.) to the host (without www.)
    $HTTP["host"] =~ "^www\.(.*)$" {
    url.redirect = ( "^/(.*)" => "http://%1/$1" )
    }
    
    # Access logs
    accesslog.filename   = "/var/log/lighttpd/access.log"

EOF

elif [ "$1" = exec ] ; then

    read -e -p  "Where is the www directory ? " directory
    if [ -n "$directory" ] ; then 
        # Set the server document root.
        sudo sed -i -r "/server.document-root/ s#\\\"[a-zA-Z/]*\\\"#\\\"$directory\\\"#g" /etc/lighttpd/lighttpd.conf
    else 
        error "wwww directory is empty. skiping this step."
    fi

    echo "Set the server.error-handler-404 value"
    sudo sed -i -r "/server.error-handler-404/ s#\\\"[a-zA-Z/]*\\\"#\\\"/en/dopagenotfound\\\"#g" /etc/lighttpd/lighttpd.conf

    echo "Set the redirect and the accesslog"
    sudo bash -c 'cat << EOF >> /etc/lighttpd/lighttpd.conf
# make an external redirect
# from any www.host (with www.) to the host (without www.)
\$HTTP["host"] =~ "^www\.(.*)$" {
  url.redirect = ( "^/(.*)" => "http://%1/$1" )
  }

# Access logs
accesslog.filename   = "/var/log/lighttpd/access.log"
EOF ' 

    echo "Adding the rewrite and fastcgi modules"
    sudo cp $FILE_DIR/rewrite.lighttpd.conf /etc/lighttpd/conf-enabled/
    sudo cp $FILE_DIR/fastcgi.conf /etc/lighttpd/conf-enabled/


    read -p "You can now edit the lighttpd.conf file. <press enter>"
    sudo vim /etc/lighttpd/lighttpd.conf

    sudo service lighttpd restart
    success "done."
fi



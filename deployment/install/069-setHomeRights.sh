
#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Set the correct rights on the $USER home folder
----------------------

### Usage 

You must export the USER variable. (export USER=...)

EOF

elif [ "$1" = exec ] ; then
    if [ -z "$USER" ] ; then 
        echo "You have to specify the user you want to use (export USER=...)"
        echo "Found error. Abording."
        exit
    fi

    sudo su -c '
    cd
    find . -type d -execdir chmod 700 {} \;
    find . -type f -execdir chmod 400 {} \;
    chmod 750 .
    find www -type d -execdir chmod 750 {} \;
    find www -type f -execdir chmod 440 {} \;
    find wwwdoc -type d -execdir chmod 750 {} \;
    find wwwdoc -type f -execdir chmod 440 {} \;
' $USER

fi

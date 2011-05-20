#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: add the www-data user in the specified user group.
----------------------

### Usage 

    $0 [username]: If *username* is specified then the script is launch for the user *username*

### Details

The www-data user will have to read some files into the home of the elveos user.
To set the rights correctly we have to allow the users in the group elveos to read those files, and to add www-data into the elveos group.

EOF

else
    USER="$1"
    sudo addgroup www-data $USER

fi



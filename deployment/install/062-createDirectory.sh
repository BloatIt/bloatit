#!/bin/bash

if [ -z "$1" ] ; then
    cat << EOF

$0: Create the needed directory for a elveos deamon to works.
----------------------

### Usage 

    $0 [username]: If *username* is specified then the script is launch for the user *username*

### Details

Create in the elveos home directory:

 * .upload
 * .local/share/bloatit/lucene
 * .local/share/bloatit/log
 * .config/bloatit
 * www

EOF

else
    USER="$1"

    sudo su -c '
    cd
    mkdir .upload
    mkdir -p .local/share/bloatit/lucene
    mkdir -p .local/share/bloatit/log
    mkdir -p .config/bloatit
    mkdir www
    ' $USER

fi

#!/bin/bash

cat << EOF
The default install of OVH vm does not configure the local correctly. 
To do so we have to reconfigure them.

Set the default to fr_FR.UTF8@euro
(add also a en_US.UTF8)

EOF

configureLocales(){
    sudo dpkg-reconfigure locales
}

configureLocales

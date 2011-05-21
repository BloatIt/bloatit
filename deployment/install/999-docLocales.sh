#!/bin/bash

cat << EOF
How to configure the local (for example when the charset is not wrong)
-----------------------------

The default install of OVH vm does not configure the local correctly. 
To do so we have to reconfigure them.

Set the default to fr_FR.UTF8@euro
(add also a en_US.UTF8)

     sudo dpkg-reconfigure locales

EOF


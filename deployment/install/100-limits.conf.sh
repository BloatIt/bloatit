#!/bin/bash

if [ -z "$1" ] ; then

    cat << EOF
$0: Configure the limits using /etc/security/limits.conf
-----------------------------

### Details

http://gerardnico.com/wiki/linux/limits.conf
http://www.debian.org/doc/manuals/securing-debian-howto/ch4.fr.html#s4.9

> elveos          hard    memlock         2500000
> www-data        hard    memlock         2500000
> postgres        hard    memlock         2500000
> 
> elveos          hard    nofile          65535
> www-data        hard    nofile          65535
> 
> elveos               hard    nproc           16384
> administrator        hard    nproc           16384

EOF

elif [ "$1" = "exec" ] ; then

    sudo su -c 'echo "
elveos          hard    memlock         2500000
www-data        hard    memlock         2500000
postgres        hard    memlock         2500000

elveos          hard    nofile          65535
www-data        hard    nofile          65535

elveos               hard    nproc           16384
administrator        hard    nproc           16384
" >> /etc/security/limits.conf
'
fi

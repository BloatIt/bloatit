#!/bin/bash

if [ -z "$1" ] ; then
cat << EOF

$0: configure the rsyslog deamon for elveos logging
-----------------------------

(You will have to do all the work by hand ...)

EOF

elif [ "$1" = "exec" ] ; then

   cat << EOF
Do not forget the activation of UDP:
$ModLoad imudp
$UDPServerRun 514
$UDPServerAddress 127.0.0.1

This should be your first rule (copy it):

#
# Elveos deamon specific rules:
#
if $syslogfacility-text == 'user'  \
   and $msg contains 'elveosJava'  \
   and $msg contains 'Access:' 	   \
   then 			-/var/log/elveos.access.log
& ~

user.warn			/var/log/elveos.error.log

if $syslogfacility-text == 'user'   \
   and $msg contains 'elveosJava'  \
   then 			-/var/log/elveos.log
& ~
# Here all elveos messages are discarded

EOF

    sudo vim /etc/rsyslog.conf
    sudo service rsyslog restart
fi

#!/bin/bash

if [ -z "$1" ] ; then
cat << EOF

$0: Install and configure cron-apt.
-----------------------------

### Usage 

    $0 [ exec ]: Use exec to launch the task.

### Description

To have a secure system we have to make frequent update.
So I choose to use the cron-apt package: 

cron-apt Contains a tool that is run by a cron job at regular intervals. By default it just updates the package list and download new packages without installing. You can instruct it to run anything that you can do with apt-get (or aptitude).

It can optionally sends mail to the system administrator on errors, log to syslog or a separate log file.

Observe that this tool may be a security risk, so you should not set it to do more than necessary. Automatic upgrade of all packages is NOT recommended unless you are in full control of the package repository.


You have to configure cron-apt in /etc/cron-apt 

 - set the mail address MAILTO="sysadmin@linkeos.com"
 - set MAILON=”always” 


Then configure the cron in /etc/cron.d/cron-apt

 - Use the dayly cron.

EOF

elif [ "$1" = "exec" ] ; then
    sudo apt-get install cron-apt

    echo "uses sysadmin@linkeos.com"
    read -p "You have to configure cron-apt (MAILTO and MAILON options) <hit return>"
    sudo vim /etc/cron-apt/config

    read -p "You have to configure cron for cron-apt <hit return>"
    sudo vim /etc/cron.d/cron-apt
fi

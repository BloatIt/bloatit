#!/bin/bash

cat << EOF

$0: This is a documentation only script.

Dhcp is one of the deamon the NSA suggests to remove.
But to do so we have to configure the connection in a static way.


First stop the dhcp deamon.
sudo ps -fe | grep dhclient
sudo kill <pid>

--- interfaces ---
This is done in the /etc/network/interfaces/

Remove the line : "iface eth0 inet dhcp"
And uses: (For example)

iface eth0 inet static
       address 192.168.0.14
       netmask 255.255.255.0
       network 192.168.0.0
       broadcast 192.168.0.255
       gateway 192.168.0.254"

The line "allow-hotplug eth0" means you manage the kernel event on new device plug in.

--- DNS ---
We also have to specify a dns server:
in /etc/resolv.conf

nameserver 212.27.40.240
nameserver 212.27.40.241

All the previous configurtion work well with a "freebox" ... 

--- Restart the network ---
If you are using ssh you cannot do this step ...

sudo service networking restart

EOF


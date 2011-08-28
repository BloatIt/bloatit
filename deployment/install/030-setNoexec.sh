#!/bin/bash

create_apt_conf(){
cat << "EOF" > /tmp/50remount
DPkg::Pre-Install-Pkgs {"/bin/mount -o remount,exec /tmp && /bin/mount -o remount,exec /var";};
DPkg::Post-Invoke {"/bin/mount -o remount /tmp  && /bin/mount -o remount /var";};
EOF
sudo mv /tmp/50remount /etc/apt/apt.conf.d/
}

update_fstab(){
sudo sed -i -r '/tmp.*ext4/ s/noexec,?//g' /etc/fstab
sudo sed -i -r '/tmp.*ext4/ s/(ext4 +)/\1noexec,/g' /etc/fstab
sudo sed -i -r '/var.*ext4/ s/noexec,?//g' /etc/fstab
sudo sed -i -r '/var.*ext4/ s/(ext4 +)/\1noexec,/g' /etc/fstab
}



if [ -z "$1" ] ; then 

cat << EOF
$0: remonte les partitions /var et /tmp en noexec par défaut.
-----------------------------------------------------

### Descritpion

 * modify the fstab
 * create a /etc/apt.conf.d/50remount file to remount with exec during apt execution.

### Explication

Les partitions /var et /tmp ne contiennent pas habituellement d'executable. 
Les monter avec l'option noexec permet de s'assurer qu'aucun fichier sur ces partitions ne peut avoir les droits d'execution.

> Attention: cette protection n'est efficace que contre les scripts automatiques. Elle est facilement contournable par une personne en ayant la volonté.

Apt a souvent besoin de pouvoir executer des fichiers dans /tmp ou /var. Ce script va donc configurer apt pour qu'il remonte les partitions en exec pendant son execution.

EOF

elif [ "$1" = exec ] ; then 

create_apt_conf && update_fstab

else 
    echo "Parameter must be empty or 'exec'"
fi

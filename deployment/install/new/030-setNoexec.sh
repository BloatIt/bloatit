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
$0: Make the /var and /tmp partitions noexec

- modify the fstab
- create a /etc/apt.conf.d/50remount file to remount with exec during apt execution.

We have to do this because there is no way to do it durring the install process.
Apt will remount with the exec option during its execution.
EOF

elif [ "$1" = exec ] ; then 

create_apt_conf && update_fstab

else 
    echo "Parameter must be empty or 'exec'"
fi

#!/bin/bash

# Make the /var and /tmp partitions noexec
# We have to do this because there is no way to do it durring the install process.
# Apt will remount with the exec option during its execution.

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

create_apt_conf && update_fstab

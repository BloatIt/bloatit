#!/bin/bash

create_apt_conf(){
cat << "EOF" > /tmp/50remount
DPkg::Pre-Install-Pkgs {"/bin/mount -o remount,exec /tmp && /bin/mount -o remount,exec /var";};
DPkg::Post-Invoke {"/bin/mount -o remount /tmp  && /bin/mount -o remount /var";};
EOF
sudo mv /tmp/50remount /etc/apt/apt.conf.d/
}

update_fstab(){
sudo sed -i -r '/tmp.*ext4/ s/(ext4 +)(.*)(noexec)/\1noexec,\2/g' /etc/fstab
sudo sed -i -r '/var.*ext4/ s/(ext4 +)(.*)(noexec)/\1noexec,\2/g' /etc/fstab
}

create_apt_conf && update_fstab

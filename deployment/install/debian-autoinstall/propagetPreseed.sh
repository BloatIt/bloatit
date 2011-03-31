#!/bin/sh

ISO=debian-6.0.0-amd64-netinst.iso
PRESEED=preseed.cfg

mkdir debian
bsdtar -C ./debian -xf $ISO

mkdir initrd
cd initrd

gunzip -c ../debian/install.amd/initrd.gz.org | cpio -id
cp ../$PRESEED .

find . | cpio --create --format='newc' | gzip  > ../initrd.gz

cd ..

cp initrd.gz debian/install.amd/

mkisofs -o  ./debian-6.0-netinstall-custom.iso -V di`date -u +%m%d%H%M%S` -r -J -no-emul-boot -boot-load-size 4 -boot-info-table -b isolinux/isolinux.bin -c isolinux/boot.cat ./debian

rm -rf debian/ initrf/

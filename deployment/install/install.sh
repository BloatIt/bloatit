#!/bin/$SHELL

SHELL=bash
# Context: Where is this script.
cd "$(dirname $0)"
ROOT=$PWD
cd -
COMMONS=$ROOT/../commons/
DEPLOYMENT_INSTALL_SCRIPT=install/install.sh
FILE_DIR=$ROOT/../files/

# Add the includes 
. $COMMONS/includes.sh

echo
echo "This installer will install all the requiered dependencies to launch the Elveos server"

export USER=elveos
export FILE_DIR=$ROOT/../files/

menu ======================================================================
echo 

for i in $ROOT/???-*.sh ; do
    $SHELL $i
    read -p "Skip this step ? (y/N) " reponse
    if [ -z "$reponse" ] || [ "$reponse" = "n" ] || [ "$reponse" = "N" ] ; then 
        $SHELL $i exec && success "Step done"
    fi

    echo 
    menu ======================================================================
    echo 
done


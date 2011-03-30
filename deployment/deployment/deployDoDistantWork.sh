#!/bin/sh

. $PWD/commons/includes.sh

usage(){
cat << EOF 
usage: $0 releaseVersion upConfDir confDir upShareDir shareDir upRessources classes

This scritp do the distant work in a deployment.

OPTIONS:
   host can be localhost or admin@192.168.0.13 ...
EOF
}

LOG_FILE=/dev/null
PREFIX=elveos
RELEASE_VERSION="$1"
UP_CONF_DIR="$2"
CONF_DIR="$3"
UP_SHARE_DIR="$4"
SHARE_DIR="$5"
UP_RESSOURCES="$6"
CLASSES="$7"

commitPrerelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION"

stopBloatitServer "$LOG_FILE"

propagateConfFiles "$LOG_FILE" "$UP_CONF_DIR" "$CONF_DIR" "$UP_SHARE_DIR" "$SHARE_DIR" "$UP_RESSOURCES" "$CLASSES"

migratingDB "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION" "$LIQUIBASE_DIR" "$USER"

commitRelease "$LOG_FILE" "$PREFIX" "$RELEASE_VERSION"


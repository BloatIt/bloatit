#!/bin/sh
# Make mo files
SCRIPT="msgfmt -c -v -o"
MO_FILE="LC_MESSAGES/bloatit.mo"

rm -rf ../locales/*
mkdir -p ../locales/fr/LC_MESSAGES
$SCRIPT ../locales/fr/LC_MESSAGES/bloatit.mo fr.po
mkdir -p ../locales/en/LC_MESSAGES
$SCRIPT ../locales/en/LC_MESSAGES/bloatit.mo en.po

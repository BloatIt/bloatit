#!/bin/bash


cat << EOF

Guide d'installation d'un server Elveos
================================

Bienvenu dans le système d'installation automatique d'un server Elveos.

General
========

Un serveur elveos doit être parfaitement sécurisé. 
Voici quelques documents qui pourrait aider à sécuriser un server linux.

- http://www.debian.org/doc/manuals/securing-debian-howto/
- http://www.nsa.gov/ia/_files/os/redhat/rhel5-guide-i731.pdf


Les scripts
===========

Ce guide d'installation est découper en multiple petits scripts. Chaque script fait une tache simple, et possède une partie documentations. Elle explique ce que fait le script, et propose des configurations ou ajoute d'autres informations pour customiser la tâche en cours.

 - Lancer le script sans paramêtre affiche la doc.
 - Lancer le script avec le paramêtre 'exec', ou un autre paramètre lorsque c'est spécifié, lance la tache.

EOF

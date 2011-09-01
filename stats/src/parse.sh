#!/bin/bash

export PYTHONPATH=$PYTHONPATH:$PWD

python bloatitstats/stats.py -d ~/.local/share/bloatit/stats.db 


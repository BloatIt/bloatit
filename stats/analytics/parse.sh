#!/bin/bash

export PYTHONPATH=$PYTHONPATH:$PWD/../src/

python ../src/bloatitstats/stats.py -d ~/.local/share/bloatit/stats.db 


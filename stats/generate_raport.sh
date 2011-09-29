#!/bin/bash

for i in "-m" "-d" "-a" 
do 
    for j in txconversion txrebond visits_member visits_non_member
    do
        ./generate_table.sh $i -s $j > "tables/$j$i.data"
    done
done

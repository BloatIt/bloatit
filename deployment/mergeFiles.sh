#!/bin/bash

if [ ! -e "$1" ] || [ ! -e "$2" ] ; then
    echo "Error. Waiting for 2 existing directories. exiting..."
    exit 1
fi

performMerge() {
echo "$1 and $2 have differences. What do you want to do ?"
echo "1 -> keep old ($2)"
echo "2 -> use new ($1) and erase $2"
echo "3 -> use vimdiff"
echo "4 -> merge the files by yourself (exit or ^D to finish)."
read _reponse
case $_reponse in
     1)
         echo "keeping $2. Do nothing."
         ;;
     2)
	 echo "cp $1 $2"
	 cp "$1" "$2"
         ;;
     3)
	 # TODO use modification time to know if merge done.
	 vimdiff "$1" "$2"
	 echo "I'm assuming you have made the merge."
         ;;
     4)
	 # TODO use modification time to know if merge done.
	 echo "You can use $2.new"
	 pushd "$(dirname "$2")"
	 bash -i 
         popd
	 echo "I'm assuming you have made the merge."
         ;;
     ?)
         performMerge "$1" "$2"
         exit
         ;;
esac
}

filesEqual(){
if [ ! -e "$1" ] || [ ! -e "$2" ] ; then
    echo false
    exit 1
fi
if [ $(md5sum "$1" | cut -d " " -f 1) = $(md5sum "$2" | cut -d " " -f 1) ] ; then
    echo true
    exit 0
else
    echo false
    exit 1
fi
}

tryMerge(){
local _new_files=( $(find $1 -type f) )
local _old_files=( ${_new_files[@]/#$1/$2} )

for (( i = 0; i < ${#_new_files[@]}; i++)) ; do
    local _new_file="${_new_files[$i]}"
    local _old_file="${_old_files[$i]}"
    echo "merging [$_new_file] to [$_old_file]"

    if [ -e "$_old_file" ] ; then
	
        if [ "$(filesEqual "$_old_file" "$_new_file")" = "true" ] ; then
	    echo "No modification in: $_old_file. Do nothing"
	elif [ "$(filesEqual "$_old_file.new" "$_new_file")" = "true" ] ; then
	    echo "No modification since prevous transfer. Do nothing"
	else
	    cp "$_new_file" "$_old_file.new"
	    performMerge "$_new_file" "$_old_file" 
	fi
    else
        echo "New file $_new_file is copied to $_old_file."
	if [ ! -e "$(dirname "$_old_file")" ] ; then
	    mkdir -p "$(dirname "$_old_file")"
	fi
	cp "$_new_file" "$_old_file" 
    fi
done
}

tryMerge "$1" "$2"

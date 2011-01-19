#!/bin/bash

if [ "$(id -u)" != 0 ] ; then
	echo You must be root
	exit 1
fi

apt-get install sd
apt-get install libnet-github-perl
apt-get install libpath-class-perl
echo "alias sd='git-sd'" >> ~/.bashrc

cat << 'END' > /etc/bash_completion.d/sd 
#!bash

_sd_complete() 
{
	local cur prev opts
	COMPREPLY=()
	cur="${COMP_WORDS[COMP_CWORD]}"
	prev="${COMP_WORDS[COMP_CWORD-1]}"
	always="-v --verbose"

	if [[ $COMP_CWORD -eq 1 ]] ; then
		opts="init clone setting ticket publish help push pull server"
	elif [[ $COMP_CWORD -eq 2 ]] ; then
		case $prev in
			"ticket" )
			opts="create update list edit search basics show history delete take give resolved comment attachment"
		;;
	esac
	fi
	COMPREPLY=( $(compgen -W "${opts} ${always}" -- ${cur}) )
}
complete -F _sd_complete sd
END

cat << END
To make sure you can use "sd" re-launch your terminal.
then : 
	sd config edit 
	sd setting edit
	cd bloatit
	sd pull -from github:BloatIt/bloatit

	sd tiket list
	sd tiket create

see http://syncwith.us/sd/using
END

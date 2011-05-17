
usage(){
    cat << EOF
    This script backup elveos.org
EOF
}

exit_fail(){
    echo failure !
    exit
}

bash ./db-backup.sh -b 
bash ./resources-backup.sh -b

git add -A
git commit -m "backup $(date)"



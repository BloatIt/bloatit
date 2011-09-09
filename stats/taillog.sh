#!/bin/sh

BLUE="\\033[1;34m"
GREEN="\\033[1;32m"
ORANGE="\\033[1;33m"
RED="\\033[1;31m"
IWHITE="\\033[1;97m"
NORMAL="\\033[0;39m"

echo=/bin/echo

ssh elveos@elveos.org '
tail --follow=name ~/.local/share/bloatit/log/infos.log' \
| sed -u -E "
s/^....-..-.. (..:..:..,...) \\[Thread-(..?)\\] INFO    Access\\:(Context|Request)\\:/\1 (\2)/g
s/(REQUEST_URI='[^']*')/$($echo -e $RED)\1$($echo -e $NORMAL)/g
s/(USER_ID='[0-9]*')/$($echo -e $GREEN)\1$($echo -e $NORMAL)/g
s/(REQUEST_URI='....resource[^']*')/$($echo -e $BLUE)\1$($echo -e $NORMAL)/g
s/(KEY='[^']*')/$($echo -e $IWHITE)\1$($echo -e $NORMAL)/g
s#(REFERER='[^']*')#$($echo -e $ORANGE)\1$($echo -e $NORMAL)#g
s#(REFERER='https://elveos.org[^']*')#$($echo -e $IWHITE)\1$($echo -e $NORMAL)#g
" | \
while read line ; do
GREPED="$($echo "$line" | grep -o -E "KEY='[^']*'")"
if [ -z "$GREPED" ] ; then
    echo "$line"
else
echo "$GREPED" | cksum | grep -o -R "[0-5]" | head -3 | \
     (
      read green
      read red
      read blue
      echo $line | sed -u -E "s/KEY='([^']*)'/$($echo -e "\\x1b[48;5;$(( 16 + ($red * 36) + ($green * 6) + $blue ))m")\1$($echo -e $NORMAL)/g"
     )
fi
done


#!/bin/bash


if [ -z "$4" ] ; then
    echo "usage: $0 date resolution during referer" 2>&1
    echo "example: $0 2011-11-02T12:00:00 hours 24 t.co" 2>&1
    exit -1
fi

FROM_DATE="$1"
RESOLUTION="$2"
DURING="$3"
REFERER="$4"

SQLITE="sqlite3"
TMP_FILE="/tmp/digg.sql"

echo ".separator ',	'" > "$TMP_FILE"

for i in $(seq 0 $DURING) ; do 
    cat << EOF >> $TMP_FILE
    SELECT quote(datetime('$FROM_DATE','+$i $RESOLUTION')), count(distinct(visit.id))
    FROM externalurl
    JOIN visit on externalurl.id=id_externalurl
    WHERE  begin_date > datetime('$FROM_DATE', '+$i $RESOLUTION') 
    AND begin_date < datetime('$FROM_DATE', '+$(( $i + 1 )) $RESOLUTION') 
    AND netloc LIKE '$REFERER'
    AND real=1;
EOF

done

$SQLITE -init "$TMP_FILE" "$HOME/.local/share/bloatit/stats.db" ""



#
# Creating environment
#

TABLE_DIR=tables
GLE_DIR=glefiles
OUTPUT_DIR=output

[ -e $TABLE_DIR ] || mkdir $TABLE_DIR
[ -e $GLE_DIR ] || mkdir $GLE_DIR
[ -e $OUTPUT_DIR ] || mkdir $OUTPUT_DIR

USER_AGENT=Browser
TEMP_FILE=/tmp/sqlrequest.sql
DB=~/.local/share/bloatit/stats.db

for USER_AGENT in "Browser" "unknown" "Robot" "Mobile Browser" ; do
#
# Nb visits by day hour
#

filename="visitsByDayHour-$USER_AGENT"
cat << EOF | sqlite3 "$DB" > "$TABLE_DIR/$filename.data"
.separator '	'
SELECT strftime('%H', begin_date) as date, count(visit.id) 
    FROM visit join useragent on id_useragent=useragent.id 
    WHERE typ='$USER_AGENT'
    GROUP BY date;
EOF

cat << EOF > "$GLE_DIR/$filename.gle"
size 40 28

begin graph
   title  "Répartition des visites sur une journée, venant de : '$USER_AGENT'"
   xtitle "Hour of day"
   ytitle "Nb visits"
   xticks off
   data   "../$TABLE_DIR/$filename.data"
   bar d1 fill navy
   xaxis min -0.5 max 23.5
end graph
EOF

gle -output "$OUTPUT_DIR/$filename.pdf" "$GLE_DIR/$filename.gle"

#
# Nb visit last 2 weeks
#
filename="2weeks-$USER_AGENT"
cat << EOF | sqlite3 "$DB" > "$TABLE_DIR/$filename.data"
.separator '	'
SELECT quote(
         case cast (strftime('%w', begin_date) as integer)
              when 0 then 'Sun'
              when 1 then 'Mon'
              when 2 then 'Tue'
              when 3 then 'Wed'
              when 4 then 'Thu'
              when 5 then 'Fri'
              else 'Sat' end || 
         strftime('%d', begin_date)) as date, count(visit.id) 
    FROM visit join useragent on id_useragent=useragent.id 
    WHERE typ='$USER_AGENT'
    AND begin_date BETWEEN date('now', '-14 days') AND date('now')
    GROUP BY strftime('%Y%m%d', begin_date)
    ORDER BY begin_date ASC;
EOF

cat << EOF > "$GLE_DIR/$filename.gle"
size 40 28

begin graph
   title  "2 weeks global bilan : '$USER_AGENT'"
   xtitle "day"
   ytitle "Nb visits"
   xticks off
   data   "../$TABLE_DIR/$filename.data"
   bar d1 fill Red
end graph
EOF

gle -output "$OUTPUT_DIR/$filename.pdf" "$GLE_DIR/$filename.gle"

done

filename="vistsByOS"
cat << EOF | sqlite3 "$DB" > "$TABLE_DIR/$filename.data"
.separator '	'
SELECT quote(os_family), count(visit.id) 
    FROM visit join useragent on id_useragent=useragent.id 
    GROUP BY os_family;
EOF

cat << EOF > "$GLE_DIR/$filename.gle"
size 40 28

begin graph
   title  "Nb visits by os"
   xtitle "os"
   ytitle "Nb visits"
   xticks off
   data   "../$TABLE_DIR/$filename.data"
   bar d1 fill Green
end graph
EOF

gle -output "$OUTPUT_DIR/$filename.pdf" "$GLE_DIR/$filename.gle"





#
# Creating environment
#

TABLE_DIR=tables
GLE_DIR=glefiles
OUTPUT_DIR=output
SQL_DIR=sql

[ -e $TABLE_DIR ] || mkdir $TABLE_DIR
[ -e $GLE_DIR ] || mkdir $GLE_DIR
[ -e $OUTPUT_DIR ] || mkdir $OUTPUT_DIR
[ -e $SQL_DIR ] || mkdir $SQL_DIR

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

nbdays=30
monthlyDateSelect="strftime('%Y-%m-%d 00:00:00', begin_date) as date ,"
#monthlyDateSelect="quote(
#         case cast (strftime('%w', begin_date) as integer)
#              when 0 then 'Sun'
#              when 1 then 'Mon'
#              when 2 then 'Tue'
#              when 3 then 'Wed'
#              when 4 then 'Thu'
#              when 5 then 'Fri'
#              else 'Sat' end || 
#              strftime('%d', begin_date)) as date,"
#
monthlyGroupBy="strftime('%Y%m%d', begin_date)"

#dailyDateSelect="strftime('%H', begin_date) as date,"
dailyDateSelect="strftime('%Y-%m-%d %H:00:00', begin_date) as date ,"
dailyGroupBy="strftime('%d%H', begin_date)"

for i in monthly daily; do 

if [ "$i" = "monthly" ] ; then
dateSelect="$monthlyDateSelect"
groupBy="$monthlyGroupBy"
nbdays=30

cat << EOF | sqlite3 "$DB"
CREATE TABLE IF NOT EXISTS mydates(time datetime);
BEGIN;
$(
for i in $(seq 0 29) ; do 
    echo "INSERT INTO mydates (time) VALUES (datetime('now', '-$nbdays days', 'start of day', '+$i days' ));"
done
)
COMMIT;
EOF

elif [ "$i" = "daily" ] ; then
dateSelect="$dailyDateSelect"
groupBy="$dailyGroupBy"
nbdays=2

cat << EOF | sqlite3 "$DB"
CREATE TABLE IF NOT EXISTS mydates(time datetime);
BEGIN;
$(
for i in $(seq 0 47) ; do 
    echo "INSERT INTO mydates (time) VALUES (datetime('now', '-$nbdays days', 'start of day', '+$i hours' ));"
done
)
COMMIT;
EOF
fi

filename="nbVisitsNonMember"
echo $filename
cat << EOF |tee $SQL_DIR/$filename-$i.sql| sqlite3 "$DB" > "$TABLE_DIR/$filename-$i.data"
.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
    SELECT $dateSelect count(visit.id) as nb  
    FROM visit 
      JOIN useragent ON id_useragent=useragent.id 
      JOIN visitor ON id_visitor=visitor.id
    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
    AND visitor.userid = -1
    AND begin_date > date('now', '-$nbdays days') 
    GROUP BY $groupBy ) 
ON time = date
WHERE time > date('now', '-$nbdays days');
EOF

filename="nbVisitsMember"
echo $filename
cat << EOF |tee $SQL_DIR/$filename-$i.sql| sqlite3 "$DB" > "$TABLE_DIR/$filename-$i.data"
.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT $dateSelect count(visit.id) as nb
    FROM visit 
      JOIN useragent ON id_useragent=useragent.id 
      JOIN visitor ON id_visitor=visitor.id
    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
    AND visitor.userid != -1
    AND visitor.userid > 16 AND visitor.userid != 43
    AND begin_date > date('now', '-$nbdays days') 
    GROUP BY $groupBy)
ON time = date
WHERE time > date('now', '-$nbdays days');
EOF

filename="nbInscriptions"
echo $filename
cat << EOF |tee $SQL_DIR/$filename-$i.sql| sqlite3 "$DB" > "$TABLE_DIR/$filename-$i.data"
.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT $dateSelect count(visit.id) as nb
    FROM visit 
      JOIN useragent ON id_useragent=useragent.id 
      JOIN visitor ON id_visitor=visitor.id
    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
    AND visitor.userid != -1
    AND visitor.userid > 16 AND visitor.userid != 43
    AND begin_date > date('now', '-$nbdays days') 
    GROUP BY $groupBy)
ON time = date
WHERE time > date('now', '-$nbdays days');
EOF

# visites d'une page
filename="txRebond"
echo $filename
cat << EOF |tee $SQL_DIR/$filename-$i.sql| sqlite3 "$DB" > "$TABLE_DIR/$filename-$i.data"
.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT $dateSelect COALESCE(count(nbRequests), 0) as nb
     FROM (  SELECT begin_date, count(*) as nbRequests 
            FROM request JOIN visit ON id_visit=visit.id  
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
	    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
	    AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
	    AND begin_date > date('now', '-$nbdays days') 
            GROUP BY visit.id) 
     WHERE nbRequests = 1
     GROUP BY nbRequests $( [ -n "$groupBy" ] && echo ,) $groupBy)
ON time = date
WHERE time > date('now', '-$nbdays days');
EOF

filename="txconversion"
echo $filename
cat << EOF |tee $SQL_DIR/$filename-$i.sql| sqlite3 "$DB" > "$TABLE_DIR/$filename-$i.data"
.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT $dateSelect count(request.id) as nb
    FROM request JOIN visit ON id_visit=visit.id  
    WHERE url like  '/__/payment/doautoresponse%' 
    -- user agent is unknown !! (mercanet response)
    -- (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser') 
    -- AND visitor.userid != -1
    -- AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
    AND begin_date > date('now', '-$nbdays days') 
    GROUP BY $groupBy)
ON time = date
WHERE time > date('now', '-$nbdays days');
EOF

sqlite3 "$DB" "delete from mydates;"

done




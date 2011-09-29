.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , count(visit.id) as nb
    FROM visit 
      JOIN useragent ON id_useragent=useragent.id 
      JOIN visitor ON id_visitor=visitor.id
    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
    AND visitor.userid != -1
    AND visitor.userid > 16 AND visitor.userid != 43
    AND begin_date > date('now', '-30 days') 
    GROUP BY strftime('%Y%m%d', begin_date))
ON time = date
WHERE time > date('now', '-30 days');

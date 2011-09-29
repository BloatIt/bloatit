.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , COALESCE(count(nbRequests), 0) as nb
     FROM (  SELECT begin_date, count(*) as nbRequests 
            FROM request JOIN visit ON id_visit=visit.id  
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
	    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
	    AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
	    AND begin_date > date('now', '-30 days') 
            GROUP BY visit.id) 
     WHERE nbRequests = 1
     GROUP BY nbRequests , strftime('%Y%m%d', begin_date))
ON time = date
WHERE time > date('now', '-30 days');

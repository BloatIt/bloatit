.separator '	'
SELECT time, coalesce(nb, 0) FROM mydates LEFT JOIN (
SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , count(request.id) as nb
    FROM request JOIN visit ON id_visit=visit.id  
    WHERE url like  '/__/payment/doautoresponse%' 
    -- user agent is unknown !! (mercanet response)
    -- (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser') 
    -- AND visitor.userid != -1
    -- AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
    AND begin_date > date('now', '-30 days') 
    GROUP BY strftime('%Y%m%d', begin_date))
ON time = date
WHERE time > date('now', '-30 days');

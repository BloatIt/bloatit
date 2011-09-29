.separator '	'
SELECT  count(nbRequests)  
    FROM (  SELECT begin_date, count(*) as nbRequests 
            FROM request 
            JOIN visit ON id_visit=visit.id  
            JOIN useragent ON id_useragent=useragent.id 
            JOIN visitor ON id_visitor=visitor.id
            WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
            AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            GROUP BY visit.id) 
    WHERE nbRequests = 1;

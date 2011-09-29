.separator '	'
SELECT  count(visit.id)
    FROM visit 
    JOIN useragent ON id_useragent=useragent.id 
    JOIN visitor ON id_visitor=visitor.id
    WHERE (useragent.typ = 'Browser' OR useragent.typ = 'Mobile Browser')
    AND visitor.userid = -1 ;


.separator '	'
SELECT  count(request.id)
    FROM request JOIN visit ON id_visit=visit.id  
    WHERE url like  '/__/payment/doautoresponse%';

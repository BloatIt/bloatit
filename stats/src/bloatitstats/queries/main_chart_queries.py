from queries import queries

# 
# Main chart Monthly
#
class main_chart_queries(queries):
    def __init__(self, cursor, output):
        super(main_chart_queries, self).__init__(cursor, output)

    def generate_main_chart_all(self):
         # Visit member
        self.cursor.execute('''
        SELECT count(visit.id)  
        FROM visit 
          JOIN useragent ON id_useragent=useragent.id 
          JOIN visitor ON id_visitor=visitor.id
        WHERE (useragent.typ = 'Browser' )
        AND visitor.userid > 16 AND visitor.userid != 43 ''')
        
        result = list()
        for line in self.cursor:
            result.append(line[0])

        # visit non member
        self.cursor.execute('''
        SELECT  count(visit.id)
        FROM visit 
        JOIN useragent ON id_useragent=useragent.id 
        JOIN visitor ON id_visitor=visitor.id
        WHERE (useragent.typ = 'Browser' )
        AND visitor.userid = -1 ''')
        for line in self.cursor:
            result.append(line[0])

        # tx rebond
        self.cursor.execute('''
        SELECT  count(nbRequests)  
        FROM (  SELECT begin_date, count(*) as nbRequests 
                FROM request 
                JOIN visit ON id_visit=visit.id  
                JOIN useragent ON id_useragent=useragent.id 
                JOIN visitor ON id_visitor=visitor.id
                WHERE (useragent.typ = 'Browser' )
                AND (visitor.userid > 16 ) AND visitor.userid != 43
                GROUP BY visit.id) 
        WHERE nbRequests = 1 ''')
        for line in self.cursor:
            result.append(line[0])

        self.cursor.execute('''
        SELECT  count(request.id)
            FROM request JOIN visit ON id_visit=visit.id  
            WHERE url like  '/__/payment/doautoresponse%' ''')
        for line in self.cursor:
            result.append(line[0])
        
        self._array_serialize(open(self.output + "/visits_all.js", "w"), result, "visits_all")
        
    # 
    # Main chart Monthly
    #

    def generate_main_chart_monthly(self):
        for i in range(1, 31) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (datetime('now', '-30 days', 'start of day', '+%i days'))''' % i )
        
        f = open(self.output + "/visits_monthly.js", "w")
        # Visit member
        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
        SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , count(visit.id) as nb
            FROM visit 
              JOIN request ON visit.id=request.id_visit
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND visitor.userid != -1
            AND visitor.userid > 16 AND visitor.userid != 43
            AND begin_date > date('now', '-30 days') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '%featurefeed%' 
              AND url NOT LIKE '%%softwarefeed%%' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '%/doactivate%' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%' 
            GROUP BY strftime('%Y%m%d', begin_date))
        ON time = date
        WHERE time > date('now', '-30 days', 'localtime') ''')
        result_member = list()
        for line in self.cursor:
            result_member.append([line[0], line[1]])
        self._double_array_serialize(f, result_member, "visit_member")

        # visit non member
        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
            SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , count(visit.id) as nb  
            FROM visit 
              JOIN request ON visit.id=request.id_visit
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND visitor.userid = -1
            AND begin_date > date('now', '-30 days', 'localtime') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%featurefeed%' 
              AND url NOT LIKE '%%softwarefeed%%' 
            AND url NOT LIKE '%/doactivate%' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%' 
            GROUP BY strftime('%Y%m%d', begin_date) ) 
        ON time = date
        WHERE time > date('now', '-30 days', 'localtime')
        ''')
        result_non_member = list()
        for line in self.cursor:
            result_non_member.append([line[0], line[1]])
        self._double_array_serialize(f, result_non_member, "visit_non_member")

        # tx rebond
        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
        SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , COALESCE(count(nbRequests), 0) as nb
             FROM ( SELECT begin_date, count(*) as nbRequests 
                    FROM request 
                      JOIN visit ON id_visit=visit.id  
                      JOIN visitor ON id_visitor=visitor.id
                      JOIN useragent ON id_useragent=useragent.id 
                      JOIN externalurl ON externalurl.id=id_externalurl
                    WHERE (useragent.typ = 'Browser' )
                    AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
                    AND begin_date > date('now', '-30 days', 'localtime') 
                    AND netloc NOT LIKE '%%elveos.org' 
                    AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
                    AND netloc NOT LIKE '%%.local'
                    AND url NOT LIKE '/__/resource%' 
                    AND url NOT LIKE '/rest/%' 
                    AND url NOT LIKE '/favicon.ico%' 
                    AND url NOT LIKE '%.png' 
                    AND url NOT LIKE '%.txt' 
                    AND url NOT LIKE '%featurefeed%' 
              AND url NOT LIKE '%%softwarefeed%%' 
                    AND url NOT LIKE '/__/%login' 
                    AND url NOT LIKE '%/doactivate%' 
                    AND url NOT LIKE '%resource%' 
                    GROUP BY visit.id) 
             WHERE nbRequests = 1
             GROUP BY nbRequests , strftime('%Y%m%d', begin_date))
        ON time = date
        WHERE time > date('now', '-30 days', 'localtime')
        ''')
        result = list()
        i = 0
        for line in self.cursor:
            nbMember = (result_non_member[i][1] + result_member[i][1])
            result.append([line[0], nbMember and (line[1] * 100) / nbMember])
            i += 1
        self._double_array_serialize(f, result, "txrebond")

        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
        SELECT strftime('%Y-%m-%d 00:00:00', begin_date) as date , count(request.id) as nb
            FROM request JOIN visit ON id_visit=visit.id  
            WHERE url like  '/__/payment/doautoresponse%' 
            -- user agent is unknown !! (mercanet response)
            -- (useragent.typ = 'Browser' ) 
            -- AND visitor.userid != -1
            -- AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            AND begin_date > date('now', '-30 days', 'localtime') 
            GROUP BY strftime('%Y%m%d', begin_date))
        ON time = date
        WHERE time > date('now', '-30 days', 'localtime')
        ''')
        result = list()
        i = 0
        for line in self.cursor:
            nbMember = (result_non_member[i][1] + result_member[i][1])
            result.append([line[0], nbMember and (line[1] * 1000) / nbMember])
            i += 1
        self._double_array_serialize(f, result, "txconversion")

        self.cursor.execute('''delete from mydates''')
    

    # 
    # Main chart DAILY
    #
       
    def generate_main_chart_daily(self):
        self.cursor.execute('''delete from mydates''')
        for i in range(0, 30) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (strftime('%%Y-%%m-%%d %%H:00:00', datetime('now', '-%i hours', 'localtime')))''' % i )

        f = open(self.output + "/visits_daily.js", "w")
        # Visit member
        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
        SELECT strftime('%Y-%m-%d %H:00:00', begin_date) as date , count(visit.id) as nb
            FROM visit 
              JOIN request ON visit.id=request.id_visit
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND visitor.userid != -1
            AND visitor.userid > 16 AND visitor.userid != 43
            AND begin_date > date('now', '-2 days', 'localtime') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%featurefeed%' 
              AND url NOT LIKE '%%softwarefeed%%' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%'
            GROUP BY strftime('%d%H', begin_date))
        ON time = date
        WHERE time > date('now', '-2 days', 'localtime')
        ''')
        result_member = list()
        for line in self.cursor:
            result_member.append([line[0], line[1]])
        self._double_array_serialize(f, result_member, "visit_member")

        # visit non member
        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
            SELECT strftime('%Y-%m-%d %H:00:00', begin_date) as date , count(visit.id) as nb  
            FROM visit 
              JOIN useragent ON id_useragent=useragent.id 
              JOIN request ON visit.id=request.id_visit
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND visitor.userid = -1
            AND begin_date > date('now', '-2 days', 'localtime') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '%featurefeed%' 
              AND url NOT LIKE '%%softwarefeed%%' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%'
            GROUP BY strftime('%d%H', begin_date) ) 
        ON time = date
        WHERE time > date('now', '-2 days', 'localtime')
        ''')
        result_non_member = list()
        for line in self.cursor:
            result_non_member.append([line[0], line[1]])
        self._double_array_serialize(f, result_non_member, "visit_non_member")

        # tx rebond
        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
        SELECT strftime('%Y-%m-%d %H:00:00', begin_date) as date , COALESCE(count(nbRequests), 0) as nb
             FROM (  SELECT begin_date, count(*) as nbRequests 
                    FROM request JOIN visit ON id_visit=visit.id  
                      JOIN useragent ON id_useragent=useragent.id 
                      JOIN visitor ON id_visitor=visitor.id
                      JOIN externalurl ON externalurl.id=id_externalurl
                WHERE (useragent.typ = 'Browser' )
                AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
                AND begin_date > date('now', '-2 days', 'localtime') 
                AND netloc NOT LIKE '%%elveos.org' 
                AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
                AND netloc NOT LIKE '%%.local'
                AND url NOT LIKE '%featurefeed%' 
              AND url NOT LIKE '%%softwarefeed%%' 
                AND url NOT LIKE '/__/%login' 
                AND url NOT LIKE '/__/resource%' 
                AND url NOT LIKE '/rest/%' 
                AND url NOT LIKE '/favicon.ico%' 
                AND url NOT LIKE '%.png' 
                AND url NOT LIKE '%.txt' 
                AND url NOT LIKE '%resource%'
                        GROUP BY visit.id) 
             WHERE nbRequests = 1
             GROUP BY nbRequests , strftime('%d%H', begin_date))
        ON time = date
        WHERE time > date('now', '-2 days', 'localtime')
        ''')
        result = list()
        i = 0

        for line in self.cursor:
            nbMember = (result_non_member[i][1] + result_member[i][1])
            result.append([line[0], nbMember and (line[1] * 100) / nbMember])
            i += 1
        self._double_array_serialize(f, result, "txrebond")

        self.cursor.execute('''
        SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
        SELECT strftime('%Y-%m-%d %H:00:00', begin_date) as date , count(request.id) as nb
            FROM request JOIN visit ON id_visit=visit.id  
            WHERE url like  '/__/payment/doautoresponse%' 
            -- user agent is unknown !! (mercanet response)
            -- (useragent.typ = 'Browser' ) 
            -- AND visitor.userid != -1
            -- AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            AND begin_date > date('now', '-2 days', 'localtime') 
            GROUP BY strftime('%d%H', begin_date))
        ON time = date
        WHERE time > date('now', '-2 days')
        ''')
        result = list()
        i = 0
        for line in self.cursor:
            nbMember = (result_non_member[i][1] + result_member[i][1])
            result.append([line[0], nbMember and (line[1] * 1000) / nbMember])
            i += 1
        self._double_array_serialize(f, result, "txconversion")

        self.cursor.execute('''delete from mydates''')
        

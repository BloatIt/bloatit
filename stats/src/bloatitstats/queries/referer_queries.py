from queries import queries

# 
# Requests on referer
#
class referer_queries(queries):        
    def __init__(self, cursor, output, refdate = 'now'):
        super(referer_queries, self).__init__(cursor, output, refdate)

    def nb_request_by_netloc_monthly(self):
        f = open(self.output + "/netloc_monthly.js", "w")
        self.cursor.execute('''delete from mydates''')
        for i in range(1, 31) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (datetime(?, '-30 days', 'start of day', '+%i days'))''' % i , (self.refdate,))

        result = []
        self._nb_request_by_netloc(30)
        self._double_array_serialize(f, self.cursor, "netloc_ticks")
        self._nb_request_by_netloc(30)
        for row in self.cursor:
            result.append(row)

        i = 0
        for row in result:
            self.cursor.execute('''
            SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
            SELECT strftime('%%Y-%%m-%%d 00:00:00', begin_date) as date , count(netloc) as nb
                FROM request
                  JOIN visit ON visit.id=request.id_visit
                  JOIN useragent ON id_useragent=useragent.id 
                  JOIN visitor ON id_visitor=visitor.id
                  JOIN externalurl ON externalurl.id=id_externalurl
                WHERE (useragent.typ = 'Browser' )
                AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
                AND begin_date > date(?, '-30 days', 'localtime') 
                AND url NOT LIKE '/__/resource%%' 
                AND url NOT LIKE '/rest/%%' 
                AND url NOT LIKE '/favicon.ico%%' 
                AND url NOT LIKE '%%.png' 
                AND url NOT LIKE '%%.txt' 
                AND url NOT LIKE '%%resource%%' 
                AND netloc = %s 
                GROUP BY strftime('%%Y%%m%%d', begin_date), netloc)
            ON time = date
            WHERE time > date(?, '-30 days', 'localtime') ''' % row[0], (self.refdate, self.refdate))
            self._double_array_serialize(f, self.cursor, "netloc_%i" % i)
            i += 1

    def nb_request_by_netloc_daily(self):
        f = open(self.output + "/netloc_daily.js", "w")
        self.cursor.execute('''delete from mydates''')
        for i in range(0, 30) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (strftime('%%Y-%%m-%%d %%H:00:00', datetime(?, '-%i hours', 'localtime')))''' % i , (self.refdate,))

        result = []
        self._nb_request_by_netloc(2)
        self._double_array_serialize(f, self.cursor, "netloc_ticks")
        self._nb_request_by_netloc(2)
        for row in self.cursor:
            result.append(row)

        i = 0
        for row in result:
            self.cursor.execute('''
            SELECT quote(time), coalesce(nb, 0) FROM mydates LEFT JOIN (
            SELECT strftime('%%Y-%%m-%%d %%H:00:00', begin_date) as date , count(netloc) as nb
                FROM request
                  JOIN visit ON visit.id=request.id_visit
                  JOIN useragent ON id_useragent=useragent.id 
                  JOIN visitor ON id_visitor=visitor.id
                  JOIN externalurl ON externalurl.id=id_externalurl
                WHERE (useragent.typ = 'Browser' )
                AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
                AND begin_date > date(?, '-2 days', 'localtime') 
                AND url NOT LIKE '/__/resource%%' 
                AND url NOT LIKE '/rest/%%' 
                AND url NOT LIKE '/favicon.ico%%' 
                AND url NOT LIKE '%%.png' 
                AND url NOT LIKE '%%.txt' 
                AND url NOT LIKE '%%resource%%' 
                AND netloc = %s 
                GROUP BY strftime('%%Y%%m%%d', begin_date), netloc)
            ON time = date
            WHERE time > date(?, '-2 days', 'localtime') ''' % row[0], (self.refdate, self.refdate))
            self._double_array_serialize(f, self.cursor, "netloc_%i" % i)
            i += 1



    def nb_request_by_netloc_all(self):
        self._nb_request_by_netloc(800)
        self._double_array_serialize(open(self.output + "/netloc_all.js", "w"), self.cursor, "netloc_all")

    def _nb_request_by_netloc(self, nbdays):
        self.cursor.execute('''
            SELECT quote(netloc), count(netloc)
            FROM externalurl 
            JOIN visit ON externalurl.id=id_externalurl
            WHERE netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net', '') 
            AND netloc NOT LIKE '%%.local' 
            AND begin_date > date(?, '-%i days', 'localtime')
            GROUP BY netloc 
            ORDER BY count(netloc) DESC 
            LIMIT 5 ''' % nbdays, (self.refdate,))

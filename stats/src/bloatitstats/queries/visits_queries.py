from queries import queries

#
# Requests on visits size
#
class visits_queries(queries):    
    def __init__(self, cursor, output, refdate = 'now'):
        super(visits_queries, self).__init__(cursor, output, refdate)

    def nb_visit_by_visit_size_monthly(self):
        self._nb_visit_by_visit_size(30)
        self._double_array_serialize(open(self.output + "/visit_size_monthly.js", "w"), self.cursor, "visit_size")
    def nb_visit_by_visit_size_daily(self):
        self._nb_visit_by_visit_size(2)
        self._double_array_serialize(open(self.output + "/visit_size_daily.js", "w"), self.cursor, "visit_size")
    def nb_visit_by_visit_size_all(self):
        self._nb_visit_by_visit_size(800)
        self._double_array_serialize(open(self.output + "/visit_size_all.js", "w"), self.cursor, "visit_size")
    def _nb_visit_by_visit_size(self, nbdays):
        self.cursor.execute('''
        SELECT CASE WHEN thecount >= 10 THEN (thecount/10)*10 ELSE thecount END AS grouped , count(*) 
        FROM (SELECT count(distinct(request.id)) as thecount 
              FROM request 
              JOIN visit ON id_visit=visit.id
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
              WHERE (useragent.typ = 'Browser' )
              AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
              AND begin_date > date(?, '-%i days', 'localtime')
              AND netloc NOT LIKE '%%elveos.org' 
              AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
              AND netloc NOT LIKE '%%.local'
              AND url NOT LIKE '/__/resource%%' 
              AND url NOT LIKE '/rest/%%' 
              AND url NOT LIKE '/favicon.ico%%' 
              AND url NOT LIKE '%%.png' 
              AND url NOT LIKE '%%featurefeed%%' 
              AND url NOT LIKE '%%softwarefeed%%' 
              AND url NOT LIKE '/__/%%login' 
              AND url NOT LIKE '%%/doactivate%%' 
              AND url NOT LIKE '%%.txt' 
              AND url NOT LIKE '%%resource%%' 
              GROUP BY id_visit) 
        GROUP BY grouped limit 20''' % nbdays, (self.refdate,))


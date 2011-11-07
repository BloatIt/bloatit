from queries import queries

class dashboard_queries(queries):    
    def __init__(self, cursor, output):
        super(dashboard_queries, self).__init__(cursor, output)

    def generate_visits_array(self):
        self.cursor.execute('''
        SELECT count(*)
            FROM visit 
              JOIN useragent ON id_useragent=useragent.id 
              JOIN request ON visit.id=request.id_visit
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            AND begin_date > date('now', '-30 days', 'localtime') 
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
        ''')
        nbvisits_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(*)
            FROM visit 
              JOIN useragent ON id_useragent=useragent.id 
              JOIN request ON visit.id=request.id_visit
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            AND begin_date > date('now', '-1 day', 'localtime') 
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
        ''')
        nbvisits_day = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(*)
            FROM visit 
              JOIN useragent ON id_useragent=useragent.id 
              JOIN request ON visit.id=request.id_visit
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            AND begin_date < date('now', '-30 days', 'localtime') 
            AND begin_date > date('now', '-60 days', 'localtime') 
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
        ''')
        nbvisits_last_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(url)) from request where url like '%member/doactivate%' 
            AND begin_date > date('now', '-1 day', 'localtime') 
        ''')
        nbinscription_day = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(url)) from request where url like '%member/doactivate%' 
            AND begin_date > date('now', '-30 days', 'localtime') 
        ''')
        nbinscription_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(url)) from request where url like '%member/doactivate%' 
            AND begin_date < date('now', '-30 days', 'localtime') 
            AND begin_date > date('now', '-60 days', 'localtime') 
        ''')
        nbinscription_last_month = self.cursor.fetchone()[0]
        f = open(self.output + "/tableau-bord.js", "w")
        f.write("var visits = [%i, %i, %i, %i];" % (nbvisits_month, nbvisits_day, nbvisits_month / 30, nbvisits_last_month))
        f.write("var insc = [%i, %i, %i, %i];" % (nbinscription_month, nbinscription_day, nbinscription_month / 30, nbinscription_last_month))

        conn = httplib.HTTPSConnection("localhost")
        conn.request("GET", "/rest/banktransactions?from=30&to=-1")
        reponse = conn.getResponse()
        data = reponse.read()
        dom_data = parseString(data)
       


        f.write("var conv = [%i, %i, %i, %i];" % )
        f.write("var moy = [%i, %i, %i, %i];" % )
        f.write("var tot = [%i, %i, %i, %i];" % )


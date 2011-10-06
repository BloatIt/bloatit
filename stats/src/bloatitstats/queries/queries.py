
import sys
import re

class queries:
    def __init__(self, cursor, output):
        self.cursor = cursor
        self.output = output
        self.cursor.execute("create table IF NOT EXISTS mydates (time datetime)")
        
    def _print_result(self):
        for row in self.cursor:
            print row
    #
    # Request on USER AGENT
    #
    
    def nb_request_by_ua_all(self):
        results = list()
        self.nb_request_by_ua_component('os_family', results, 800)
        self.nb_request_by_ua_component('ua_family', results, 800)
        self._double_array_serialize(open(self.output + "/ua_all.js", "w"), results, "ua_all")
    def nb_request_by_ua_daily(self):
        results = list()
        self.nb_request_by_ua_component('os_family', results, 2)
        self.nb_request_by_ua_component('ua_family', results, 2)
        self._double_array_serialize(open(self.output + "/ua_daily.js", "w"), results, "ua")
    def nb_request_by_ua_monthly(self):
        results = list()
        self.nb_request_by_ua_component('os_family', results, 30)
        self.nb_request_by_ua_component('ua_family', results, 30)
        self._double_array_serialize(open(self.output + "/ua_monthly.js", "w"), results, "ua")

    def nb_request_by_ua_component(self, component, results, nbdays):
        self.cursor.execute('''SELECT quote(useragent.%s), count(*)
                               FROM visit 
                               JOIN useragent ON useragent.id = id_useragent
                               WHERE begin_date > date('now', '-%i days')
                               GROUP BY useragent.%s
                               ORDER BY count(*) DESC
                               LIMIT 10 ''' % (component, nbdays, component))
        for row in self.cursor:
            results.append(row)
    
    # 
    # Requests on referer
    #
        
    def nb_request_by_netloc_monthly(self):
        f = open(self.output + "/netloc_monthly.js", "w")
        self.cursor.execute('''delete from mydates''')
        for i in range(1, 31) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (datetime('now', '-30 days', 'start of day', '+%i days'))''' % i )

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
                AND begin_date > date('now', '-30 days') 
                AND url NOT LIKE '/__/resource%%' 
                AND url NOT LIKE '/rest/%%' 
                AND url NOT LIKE '/favicon.ico%%' 
                AND url NOT LIKE '%%.png' 
                AND url NOT LIKE '%%.txt' 
                AND url NOT LIKE '%%resource%%' 
                AND netloc = %s 
                GROUP BY strftime('%%Y%%m%%d', begin_date), netloc)
            ON time = date
            WHERE time > date('now', '-30 days') ''' % row[0])
            self._double_array_serialize(f, self.cursor, "netloc_%i" % i)
            i += 1

    def nb_request_by_netloc_daily(self):
        f = open(self.output + "/netloc_daily.js", "w")
        self.cursor.execute('''delete from mydates''')
        for i in range(2, 32) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (strftime('%%Y-%%m-%%d %%H:00:00', datetime('now', '-%i hours')))''' % i )

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
                AND begin_date > date('now', '-2 days') 
                AND url NOT LIKE '/__/resource%%' 
                AND url NOT LIKE '/rest/%%' 
                AND url NOT LIKE '/favicon.ico%%' 
                AND url NOT LIKE '%%.png' 
                AND url NOT LIKE '%%.txt' 
                AND url NOT LIKE '%%resource%%' 
                AND netloc = %s 
                GROUP BY strftime('%%Y%%m%%d', begin_date), netloc)
            ON time = date
            WHERE time > date('now', '-2 days') ''' % row[0])
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
            AND begin_date > date('now', '-%i days')
            GROUP BY netloc 
            ORDER BY count(netloc) DESC 
            LIMIT 5 ''' % nbdays)
    #
    # Requests on visits size
    #
    
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
        FROM (SELECT count(*) as thecount 
              FROM request 
              JOIN visit ON id_visit=visit.id
              JOIN useragent ON id_useragent=useragent.id 
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
              WHERE (useragent.typ = 'Browser' )
              AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
              AND begin_date > date('now', '-%i days')
              AND netloc NOT LIKE '%%elveos.org' 
              AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
              AND netloc NOT LIKE '%%.local'
              AND url NOT LIKE '/__/resource%%' 
              AND url NOT LIKE '/rest/%%' 
              AND url NOT LIKE '/favicon.ico%%' 
              AND url NOT LIKE '%%.png' 
              AND url NOT LIKE '%%featurefeed%%' 
              AND url NOT LIKE '/__/%%login' 
              AND url NOT LIKE '%%/doactivate%%' 
              AND url NOT LIKE '%%.txt' 
              AND url NOT LIKE '%%resource%%' 
              GROUP BY id_visit) 
        GROUP BY grouped limit 20''' % nbdays)


    # 
    # Main chart Monthly
    #

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
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '%/doactivate%' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%' 
            GROUP BY strftime('%Y%m%d', begin_date))
        ON time = date
        WHERE time > date('now', '-30 days') ''')
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
            AND begin_date > date('now', '-30 days') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%featurefeed%' 
            AND url NOT LIKE '%/doactivate%' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%' 
            GROUP BY strftime('%Y%m%d', begin_date) ) 
        ON time = date
        WHERE time > date('now', '-30 days')
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
                    AND begin_date > date('now', '-30 days') 
                    AND netloc NOT LIKE '%%elveos.org' 
                    AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
                    AND netloc NOT LIKE '%%.local'
                    AND url NOT LIKE '/__/resource%' 
                    AND url NOT LIKE '/rest/%' 
                    AND url NOT LIKE '/favicon.ico%' 
                    AND url NOT LIKE '%.png' 
                    AND url NOT LIKE '%.txt' 
                    AND url NOT LIKE '%featurefeed%' 
                    AND url NOT LIKE '/__/%login' 
                    AND url NOT LIKE '%/doactivate%' 
                    AND url NOT LIKE '%resource%' 
                    GROUP BY visit.id) 
             WHERE nbRequests = 1
             GROUP BY nbRequests , strftime('%Y%m%d', begin_date))
        ON time = date
        WHERE time > date('now', '-30 days')
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
            AND begin_date > date('now', '-30 days') 
            GROUP BY strftime('%Y%m%d', begin_date))
        ON time = date
        WHERE time > date('now', '-30 days')
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
        for i in range(2, 32) :
            self.cursor.execute(''' 
            INSERT INTO mydates (time) 
            VALUES (strftime('%%Y-%%m-%%d %%H:00:00', datetime('now', '-%i hours')))''' % i )

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
            AND begin_date > date('now', '-2 days') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%featurefeed%' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%'
            GROUP BY strftime('%d%H', begin_date))
        ON time = date
        WHERE time > date('now', '-2 days')
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
            AND begin_date > date('now', '-2 days') 
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND url NOT LIKE '%featurefeed%' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%resource%'
            GROUP BY strftime('%d%H', begin_date) ) 
        ON time = date
        WHERE time > date('now', '-2 days')
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
                AND begin_date > date('now', '-2 days') 
                AND netloc NOT LIKE '%%elveos.org' 
                AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
                AND netloc NOT LIKE '%%.local'
                AND url NOT LIKE '%featurefeed%' 
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
        WHERE time > date('now', '-2 days')
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
            AND begin_date > date('now', '-2 days') 
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
        
    

    def _double_array_serialize(self, afile, array, varname):
        afile.write("var %s = [ " % varname)
        for row in array:
            afile.write("[")
            for cell in row:
                afile.write(str(cell))
                afile.write(",")
            afile.write("],")
        afile.write("];")

    def _array_serialize(self, afile, array, varname):
        afile.write("var %s = [ " % varname)
        for row in array:
                afile.write(str(row))
                afile.write(",")
        afile.write("];")

    def _json_serialize(self, afile, nodename, edges, varname):
        afile.write('''var %s = [''' % varname)
        self._print_tree(afile, nodename, 2)
        afile.write('''];''')

    def _print_tree(self,f, nodename, deepness, level=0):
        edges = self.graph[nodename] 
        weight_sum = 0
        mylevel = level
        for name in edges:
            weight_sum += edges[name]
        f.write(''' { 
            "id": "%s",
            "name": "%s",
            "data": {
                "$dim": %i,
            }, "adjacencies": [ ''' % (nodename + str(100000*(deepness+1) +mylevel), nodename, weight_sum / 100 or 1 ))

        if weight_sum < 80 or deepness == 0:
            f.write("]},")
            return


        for name in edges:
            mylevel += 1
            if edges[name] > 20:
                f.write(''' {
                "nodeTo": "%s",
                "data": {
                    //"$type":"arrow",
                    "weight": %i,
                    "$lineWidth": %i
                } }, ''' % (name + str(100000*deepness + mylevel), edges[name], edges[name]/100 or 1))
        f.write("]},")

        mylevel = level
        for name in edges:
            mylevel += 1
            self._print_tree(f, name, deepness -1, mylevel)    

    def generate_graph_daily(self):
        self._generate_graph(2)
        f = open(self.output + '/void_daily.js', 'w')
        self._json_serialize(f, "void", self.graph["void"], "void_tree")
        f = open(self.output + '/feature_daily.js', 'w')
        self._json_serialize(f, "feature", self.graph["feature"], "feature_tree")

    def generate_graph_monthly(self):
        self._generate_graph(30)
        f = open(self.output + '/void_monthly.js', 'w')
        self._json_serialize(f, "void", self.graph["void"], "void_tree")
        f = open(self.output + '/feature_monthly.js', 'w')
        self._json_serialize(f, "feature", self.graph["feature"], "feature_tree")

    def generate_graph_all(self):
        self._generate_graph(800)
        f = open(self.output + '/void_all.js', 'w')
        self._json_serialize(f, "void", self.graph["void"], "void_tree")
        f = open(self.output + '/feature_all.js', 'w')
        self._json_serialize(f, "feature", self.graph["feature"], "feature_tree")


    def _generate_graph(self, nbdays):
        self.graph=dict(void=dict())
        self.cursor.execute('''SELECT visit.id, pagename 
                            FROM request 
                            JOIN linkable on linkable.id=id_linkable
                            JOIN visit ON visit.id=id_visit
                            JOIN useragent ON useragent.id=id_useragent
                            JOIN visitor ON visitor.id=id_visitor
                            JOIN externalurl on externalurl.id=id_externalurl
                            WHERE typ = 'Browser' 
                            AND (visitor.userid > 16 OR visitor.userid = -1)
                            AND visitor.userid != 43 
                            AND begin_date > date('now', '-%i days') 
                            AND netloc NOT LIKE '%%elveos.org%%' 
                            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
                            AND netloc NOT LIKE '%%.local'
                            AND url NOT LIKE '%%featurefeed%%' 
                            AND url NOT LIKE '/__/resource%%' 
                            AND url NOT LIKE '/rest/%%' 
                            AND url NOT LIKE '/favicon.ico%%' 
                            AND url NOT LIKE '%%.png' 
                            AND url NOT LIKE '%%.txt' 
                            AND url NOT LIKE '%%resource%%'
                            ORDER BY visit.id, request.date ''' % nbdays)

        lastid=-1
        for line in self.cursor:
            if lastid != line[0]:
                target = self.graph["void"]
                lastid=line[0]

            if re.match("(/$)|(/..[?&#])|(/../?$)|(/../index)", line[1]):
                page = "index"
            else:
                match = re.match("/((../)|(default/))?([^/&?#]*)", line[1])
                if match:
                    page = match.group(4)
                else:
                    page = line[1]

            if page == "f":
                page = "feature"

            if page in target:
                target[page] = target[page] + 1
            else:
                target[page] = 1
            if page in self.graph:
                target=self.graph[page]
            else:
                self.graph[page]=dict()
                target=self.graph[page]




from queries import queries
import re

class graph_queries(queries):
    def __init__(self, cursor, output, refdate = 'now'):
        super(graph_queries, self).__init__(cursor, output, refdate)

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
        #f = open(self.output + '/feature_daily.js', 'w')
        #self._json_serialize(f, "feature", self.graph["feature"], "feature_tree")

    def generate_graph_monthly(self):
        self._generate_graph(30)
        f = open(self.output + '/void_monthly.js', 'w')
        self._json_serialize(f, "void", self.graph["void"], "void_tree")
#        f = open(self.output + '/feature_monthly.js', 'w')
#        self._json_serialize(f, "feature", self.graph["feature"], "feature_tree")

    def generate_graph_all(self):
        self._generate_graph(800)
        f = open(self.output + '/void_all.js', 'w')
        self._json_serialize(f, "void", self.graph["void"], "void_tree")
#        f = open(self.output + '/feature_all.js', 'w')
#        self._json_serialize(f, "feature", self.graph["feature"], "feature_tree")


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
                            AND begin_date > date(?, '-%i days', 'localtime') 
                            AND netloc NOT LIKE '%%elveos.org%%' 
                            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
                            AND netloc NOT LIKE '%%.local'
                            AND url NOT LIKE '%%featurefeed%%' 
              AND url NOT LIKE '%%softwarefeed%%' 
                            AND url NOT LIKE '/__/resource%%' 
                            AND url NOT LIKE '/rest/%%' 
                            AND url NOT LIKE '/favicon.ico%%' 
                            AND url NOT LIKE '%%.png' 
                            AND url NOT LIKE '%%.txt' 
                            AND url NOT LIKE '%%resource%%'
                            ORDER BY visit.id, request.date '''% nbdays, (self.refdate,) )

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



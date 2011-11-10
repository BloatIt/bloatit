from queries import queries


#
# Request on USER AGENT
#
class ua_queries(queries):
    def __init__(self, cursor, output, refdate='now'):
        super(ua_queries, self).__init__(cursor, output, refdate)

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
                               WHERE begin_date > date(?, '-%i days', 'localtime')
                               GROUP BY useragent.%s
                               ORDER BY count(*) DESC
                               LIMIT 10 ''' % (component, nbdays, component), (self.refdate,))
        for row in self.cursor:
            results.append(row)
    

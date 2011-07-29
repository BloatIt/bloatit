
class queries:
    def __init__(self, cursor):
        self.cursor = cursor
        
    def _print_result(self):
        for row in self.cursor:
            print row
    
    #
    # General
    #
    def nb_requests(self):
        self.cursor.execute('SELECT count(id) FROM request')
        self._print_result()
    
    def nb_visits(self):
        self.cursor.execute('SELECT count(id) FROM visit')
        self._print_result()
    
    #
    # Request on USER AGENT
    #
    
    def nb_request_by_os(self):
        self.nb_request_by_ua_component('os_family')
    def nb_request_by_os_name(self):
        self.nb_request_by_ua_component('os_name')
    def nb_request_by_ua_name(self):
        self.nb_request_by_ua_component('ua_name')
    def nb_request_by_ua(self):
        self.nb_request_by_ua_component('ua_family')
        
    def nb_request_by_ua_component(self, component):
        self.cursor.execute('''SELECT count(*), useragent.%s 
                               FROM request INNER JOIN useragent, visit
                               WHERE visit.id=id_visit AND  useragent.id=id_useragent
                               GROUP BY useragent.%s;''' % (component, component))
        self._print_result()
    
    def all_unknown_ua(self):
        self.cursor.execute('''SELECT useragent FROM useragent WHERE ua_family='unknown' ''')
        self._print_result()
        
    def all_unknown_os(self):
        self.cursor.execute('''SELECT useragent FROM useragent WHERE os_family='unknown' ''')
        self._print_result()
        
    # 
    # Requests on referer
    #
        
    def nb_request_by_netloc(self):
        self.nb_request_by_referer_component('netloc')
        
    def nb_request_by_referer_component(self, component):
        self.cursor.execute('''SELECT count(*), externalurl.%s 
                               FROM request INNER JOIN externalurl, visit
                               WHERE visit.id=id_visit AND externalurl.id=id_externalurl
                               GROUP BY externalurl.%s;''' % (component, component))
        self._print_result()
    
    #
    # Requests on visits
    #
    
    def nb_visit_by_visit_size(self):
        self.cursor.execute('''SELECT thecount, count(*) FROM (SELECT count(*) as thecount FROM request INNER JOIN visit
                              WHERE visit.id=id_visit
                              GROUP BY id_visit) group by thecount''')
        self._print_result()
        
        

from queries import queries
from datetime import datetime, date, time
import xml.parsers.expat
import httplib


class rest_request:
    def __init__(self, from_, to, refdate = 'now'):
        if refdate == 'now':
            self._from = from_
            self._to = to
        else:
            time = datetime.strptime(refdate, '%Y-%m-%dT%H:%M:%S')
            diff = (datetime.now() - time).days
            self._from = from_ + diff
            self._to = to + diff
            

    def run(self):
        self._run_rest_query()
        self._parse_xml()
    
    def _element_handler(self, name, attrs):
        if name == "banktransaction":
            self.chargedValue = float(attrs['chargedValue'])
            self.count = int(attrs['count'])
            self.paidValue = float(attrs['paidValue'])

    def _run_rest_query(self):
        conn = httplib.HTTPSConnection("elveos.org")
        conn.request("GET", "/rest/banktransactions?from=%i&to=%i" % (self._from, self._to))
        reponse = conn.getresponse()
        self.data = reponse.read()

    def _parse_xml(self):
        p = xml.parsers.expat.ParserCreate()
        p.StartElementHandler = self._element_handler
        p.Parse(self.data)
        

class dashboard_queries(queries):    
    def __init__(self, cursor, output, refdate = 'now'):
        super(dashboard_queries, self).__init__(cursor, output, refdate)
        self.nbvisits_last_month = 0
        self.nbvisits_month = 0
        self.nbvisits_day = 0
        self.nbinscription_last_month = 0
        self.nbinscription_month = 0
        self.nbinscription_day = 0
        
    def generate_dashboard(self):
        self._generate_visits_array()
        self._generate_visitors_array()
        self._generate_members_array()
        self._generate_inscription_array()
        self._get_rest_data()
        self._write_results()

    def _generate_visits_array(self):
        self.cursor.execute('''
        SELECT count(distinct(visit.id))
            FROM visit 
            WHERE begin_date > datetime(?, '-30 days', 'localtime') 
            AND real=1 
        ''', (self.refdate,))
        self.nbvisits_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(visit.id))
            FROM visit 
            WHERE begin_date > datetime(?, '-1 day', 'localtime') 
            AND real=1
        ''', (self.refdate,))
        self.nbvisits_day = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(visit.id))
            FROM visit 
            WHERE begin_date < datetime(?, '-30 days', 'localtime') 
            AND begin_date > datetime(?, '-60 days', 'localtime') 
            AND real=1
        ''', (self.refdate,self.refdate))
        self.nbvisits_last_month = self.cursor.fetchone()[0]

    def _generate_inscription_array(self):
        self.cursor.execute('''
        SELECT count(distinct(url)) from request where url like '%member/doactivate%' 
            AND date > datetime(?, '-1 day', 'localtime') 
        ''', (self.refdate,))
        self.nbinscription_day = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(url)) from request where url like '%member/doactivate%' 
            AND date > datetime(?, '-30 days', 'localtime') 
        ''', (self.refdate,))
        self.nbinscription_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(url)) from request where url like '%member/doactivate%' 
            AND date < datetime(?, '-30 days', 'localtime') 
            AND date > datetime(?, '-60 days', 'localtime') 
        ''', (self.refdate,self.refdate))
        self.nbinscription_last_month = self.cursor.fetchone()[0]

    def _percent(self, a, b):
        if b == 0:
            return 0
        return (a * 100) / b

    def _get_rest_data(self):
        r = rest_request(30, -1, self.refdate)
        r.run()
        self.conv_month = r.count
        self.moy_month = (r.count != 0) and (r.chargedValue / r.count) or 0
        self.tot_month = r.paidValue - r.chargedValue
        r = rest_request(1, -1, self.refdate)
        r.run()
        self.conv_day = self._percent(r.count, self.nbvisits_day)
        self.moy_day = (r.count != 0) and (r.chargedValue / r.count) or 0
        self.tot_day = r.paidValue - r.chargedValue
        r = rest_request(60, 30, self.refdate)
        r.run()
        self.conv_last_month = self._percent(r.count, self.nbvisits_last_month)
        self.moy_last_month = (r.count != 0) and (r.chargedValue / r.count) or 0
        self.tot_last_month = r.paidValue - r.chargedValue
       
        
    def _write_results(self):
        f = open(self.output + "/dashboard.js", "w")
        f.write("var visits = [%i, %i, %f, %i];" 
        % (self.nbvisits_month, self.nbvisits_day, self.nbvisits_month / 30, self.nbvisits_last_month))
        f.write("var visitors = [%i, %i, %f, %i];" 
        % (self.nbvisitors_month, self.nbvisitors_day, self.nbvisitors_month / 30, self.nbvisitors_last_month))
        f.write("var members = [%i, %i, %f, %i];" 
        % (self.nbmembers_month, self.nbmembers_day, self.nbmembers_month / 30, self.nbmembers_last_month))

        f.write("var insc = [%i, %f, %f, %f];" % 
            (self.nbinscription_month, 
            self._percent(self.nbinscription_day, self.nbvisits_day), 
            self._percent(self.nbinscription_day, self.nbvisits_day) / 30 , 
            self._percent(self.nbinscription_last_month, self.nbvisits_last_month)))

        f.write("var conv = [%i, %i, %f, %i];" 
        % (self.conv_month, self.conv_day, self.conv_month / 30, self.conv_last_month))
        f.write("var moy = [%i, %i, %f, %i];" 
        % (self.moy_month, self.moy_day, self.moy_month / 30, self.moy_last_month))
        f.write("var tot = [%i, %i, %f, %i];" 
        % (self.tot_month, self.tot_day, self.tot_month / 30, self.tot_last_month))

    def _generate_members_array(self):
        self.cursor.execute('''
            SELECT COUNT(DISTINCT(visitor.id)) 
            FROM visitor LEFT JOIN visit on visitor.id = id_visitor 
            WHERE visitor.userid != -1
            AND real=1
            AND begin_date > datetime(?, '-30 days', 'localtime') 
        ''', (self.refdate,))
        self.nbmembers_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
            SELECT COUNT(DISTINCT(visitor.id)) 
            FROM visitor LEFT JOIN visit on visitor.id = id_visitor 
            WHERE visitor.userid != -1
            AND real=1
            AND begin_date > datetime(?, '-1 day', 'localtime') 
        ''', (self.refdate,))
        self.nbmembers_day = self.cursor.fetchone()[0]

        self.cursor.execute('''
            SELECT COUNT(DISTINCT(visitor.id)) 
            FROM visitor LEFT JOIN visit on visitor.id = id_visitor 
            WHERE visitor.userid != -1
            AND real=1
            AND begin_date < datetime(?, '-30 days', 'localtime') 
            AND begin_date > datetime(?, '-60 days', 'localtime') 
        ''', (self.refdate,self.refdate))
        self.nbmembers_last_month = self.cursor.fetchone()[0]

    def _generate_visitors_array(self):
        self.cursor.execute('''
        SELECT count(distinct(visitor.id))
            FROM visit 
            JOIN visitor on visitor.id=id_visitor
            WHERE real=1
            AND begin_date > datetime(?, '-30 days', 'localtime') 
        ''', (self.refdate,))
        self.nbvisitors_month = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(visitor.id))
            FROM visit 
            JOIN visitor on visitor.id=id_visitor
            WHERE real=1
            AND begin_date > datetime(?, '-1 day', 'localtime') 
        ''', (self.refdate,))
        self.nbvisitors_day = self.cursor.fetchone()[0]

        self.cursor.execute('''
        SELECT count(distinct(visitor.id))
            FROM visit 
            JOIN visitor on visitor.id=id_visitor
            WHERE real=1
            AND begin_date < datetime(?, '-30 days', 'localtime') 
            AND begin_date > datetime(?, '-60 days', 'localtime') 
        ''', (self.refdate,self.refdate))
        self.nbvisitors_last_month = self.cursor.fetchone()[0]



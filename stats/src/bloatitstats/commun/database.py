import sqlite3

class database:
    
    def __init__(self, file):
        self.conn = sqlite3.connect(file)
        self.cursor = self.conn.cursor()
    
    def create_table(self):
        # Create table
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS linkable(id INTEGER PRIMARY KEY,
                                  pagename TEXT, 
                                  isAction BOOLEAN, 
                                  isRest BOOLEAN, 
                                  isadmin BOOLEAN);
        ''')
        
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS visitor(id INTEGER PRIMARY KEY,
                                 key TEXT,
                                 userid INTEGER,
                                 date_first_seen DATE);
        ''')
        
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS useragent(id INTEGER PRIMARY KEY,
                                   ua_name TEXT,
                                   os_company TEXT,
                                   os_name TEXT,
                                   ua_family TEXT,
                                   ua_company TEXT,
                                   os_url TEXT,
                                   typ TEXT,
                                   ua_company_url TEXT,
                                   ua_url TEXT,
                                   os_family TEXT,
                                   os_company_url TEXT,
                                   useragent TEXT);
        ''')
        
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS externalurl(id INTEGER PRIMARY KEY,
                                     scheme TEXT,
                                     netloc TEXT,
                                     path TEXT,
                                     params TEXT,
                                     query TEXT,
                                     external_url TEXT,
                                     fragment TEXT);
        ''')
        
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS locale(id INTEGER PRIMARY KEY,
                                lang TEXT,
                                country TEXT,
                                currency TEXT);
        ''')
        
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS visit(id INTEGER PRIMARY KEY,
                               id_visitor INTEGER REFERENCES visitor(id),
                               id_useragent INTEGER REFERENCES useragent(id),
                               id_externalurl INTEGER REFERENCES externalurl(id),
                               begin_date DATE,
                               end_date DATE,
                               duration LONG,
                               real BOOLEAN);
        ''')
        
        self.cursor.execute('''
            CREATE TABLE IF NOT EXISTS request(id INTEGER PRIMARY KEY, 
                                 id_linkable INTEGER REFERENCES linkable(id),
                                 id_visit INTEGER REFERENCES visit(id),
                                 id_referer INTEGER REFERENCES request(id),
                                 locale TEXT,
                                 request_method TEXT,
                                 remote_addr TEXT,
                                 server_protocol TEXT,
                                 server_addr TEXT,
                                 accepted_languages TEXT,
                                 date DATE,
                                 key TEXT,
                                 url TEXT);
        ''')
        self.cursor.execute('CREATE TABLE IF NOT EXISTS meta(id INTEGER PRIMARY KEY, last_parsed_entry_date DATE);')
        self.cursor.execute('CREATE INDEX IF NOT EXISTS ua_index ON useragent (useragent);')
        self.cursor.execute('CREATE INDEX IF NOT EXISTS remote_index ON request (remote_addr);')
        self.cursor.execute('CREATE INDEX IF NOT EXISTS visitor_index ON visit (id_visitor);')
        
    def close_connection(self):
        
        self.cursor.execute('''
        UPDATE visit SET real='1' 
        WHERE visit.id IN 
        (SELECT distinct visit.id
            FROM visit 
              JOIN useragent ON id_useragent=useragent.id 
              JOIN request ON visit.id=request.id_visit
              JOIN visitor ON id_visitor=visitor.id
              JOIN externalurl ON externalurl.id=id_externalurl
            WHERE (useragent.typ = 'Browser' )
            AND (visitor.userid > 16 OR visitor.userid = -1) AND visitor.userid != 43
            AND netloc NOT LIKE '%%elveos.org' 
            AND netloc NOT IN ('127.0.0.1', 'localhost', 'mercanet.bnpparibas.net') 
            AND netloc NOT LIKE '%%.local'
            AND remote_addr NOT IN ('96.44.143.234','96.44.142.250','178.238.232.13','193.34.144.193',
            '178.238.232.234','68.169.86.222','178.238.232.158','68.169.86.220','46.17.96.220','109.73.67.120')
            AND url NOT LIKE '%featurefeed%' 
            AND url NOT LIKE '%%softwarefeed%%' 
            AND url NOT LIKE '/__/%login' 
            AND url NOT LIKE '/__/resource%' 
            AND url NOT LIKE '/rest/%' 
            AND url NOT LIKE '/favicon.ico%' 
            AND url NOT LIKE '%.png' 
            AND url NOT LIKE '%.txt' 
            AND url NOT LIKE '%/doactivate%' 
            AND url NOT LIKE '%resource%')
        ''')
        self.cursor.execute('SELECT max(date) FROM request')
        date = self.cursor.fetchone()
        if date:
            self.cursor.execute('INSERT INTO meta (last_parsed_entry_date) VALUES (datetime(?))', (date[0],))
        
        # Save (commit) the changes
        self.conn.commit()
        # We can also close the cursor if we are done with it
        self.cursor.close()

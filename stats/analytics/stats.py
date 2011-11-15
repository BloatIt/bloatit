#!/usr/bin/python
import getopt, sys
import sqlite3
from datetime import datetime, date, time

version='0.1'

def usage():
    print '''
    Print some stats using a database
    
-h --help              Show this help.
-v --version           Print the version number and exit.
-d --database FILE     Use 'FILE' as a database 
-o --output file       The name of the csv file

-D --date DATE         Use 'DATE' as a starting date. Use 'date +%Y-%m-%dT%H:%M:%S' format.
-r --referer REF       A referer (for example '%linuxfr%')
-f --frequence HOURS   The frequence of the analysis of the event (nb hours)
-u --during HOURS      The number of hours for the analysis of the event

-g --groupby days      For the cohort analysis
'''

def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], 
        "hvd:D:o:r:f:u:g:", 
        ["help", "version", 'database', 'date', 'output', 'referer', 'frequence', 'during', 'groupby'])
    except getopt.GetoptError, err:
        # print help information and exit:
        print str(err) # will print something like "option -a not recognized"
        usage()
        sys.exit(2)

        
    database = '/home/thomas/.local/share/bloatit/stats.db'
    output = './output.csv'
    date= ""
    referer = ""
    frequence =  0
    during = 0
    groupby = 0
    for o, a in opts:
        if o in ('-v', '--version'):
            print 'version: ' + version
            sys.exit()
        elif o in ("-h", "--help"):
            usage()
            sys.exit()
        elif o in ("-o", "--output"):
            output = a
        elif o in ("-d", "--database"):
            database = a
        elif o in ("-D", "--date"):
            date = a
        elif o in ("-r", "--referer"):
            referer = a
        elif o in ("-f", "--frequence"):
            frequence = int(a)
        elif o in ("-u", "--during"):
            during = int(a)
        elif o in ("-g", "--groupby"):
            groupby = int(a)
        else:
            print 'ERROR: unknown option'
            usage()
            sys.exit(2)


    if not date or not referer or not frequence or not during or not groupby:
        print "ERROR"
        usage()
        sys.exit(2)

    conn = sqlite3.connect(database)
    cursor = conn.cursor()

    result=list()
    result.append(('date',
                   'visit', 
                   'visit from ' + referer, 
                   'visitor', 
                   'visitor from ' + referer,
                   "member",
                   "member from " + referer,
                   "inscription", 
                   "inscription from " + referer))

    for i in range(0, during, frequence):
        # Nb visits
        cursor.execute('''  
                SELECT datetime( ?, '+{0} hours'), count(distinct(visit.id)) FROM visit 
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND real=1;
                '''.format(i, i + frequence),  (date, date, date))
        subresult = cursor.fetchone()

        # Nb visit from ref
        cursor.execute('''  
                SELECT count(distinct(visit.id)) FROM externalurl JOIN visit on externalurl.id=id_externalurl
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND netloc = ?  AND real=1;
                '''.format(i, i + frequence),  (date, date, referer))
        subresult += cursor.fetchone()

        # Nb visitor 
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM visit JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND real=1;
                '''.format(i, i + frequence),  (date, date))
        subresult += cursor.fetchone()

        # Nb visitor from ref
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND netloc = ?  AND real=1;
                '''.format(i, i + frequence),  (date, date, referer))
        subresult += cursor.fetchone()

        # Nb member 
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM visit JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND real=1 AND userid != -1
                '''.format(i, i + frequence),  (date, date))
        subresult += cursor.fetchone()

        #nb member from ref
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND netloc = ?  AND real=1 AND userid != -1
                '''.format(i, i + frequence),  (date, date, referer))
        subresult += cursor.fetchone()

        # nb inscription
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM visit
                JOIN visitor on visitor.id=id_visitor JOIN request on visit.id=id_visit
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND request.url like '%doactivate%'
                AND real=1
                '''.format(i, i + frequence),  (date, date))
        subresult += cursor.fetchone()

        # nb inscription from ref
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl
                JOIN visitor on visitor.id=id_visitor
                JOIN request on visit.id=id_visit
                WHERE  begin_date > datetime(? , '+{0} hours') AND begin_date < datetime(?, '+{1} hours') 
                AND request.url like '%doactivate%'
                AND netloc = ?  AND real=1
                '''.format(i, i + frequence),  (date, date, referer))
        subresult += cursor.fetchone()

        result.append(subresult)

    cursor.execute('''
        SELECT distinct visitor.id FROM visitor
        JOIN visit on visitor.id = id_visitor JOIN externalurl on externalurl.id=id_externalurl
        WHERE begin_date > datetime(?) AND   begin_date < datetime(?, '+{0} hours')
        AND netloc = ?  AND real=1'''.format(during), (date, date, referer) )

    ids = ""
    for row in cursor:
        ids += str(row[0]) + ", "
    ids = ids.rstrip(", ")

    result.append(('date',
                   'visit', 
                   'visit from ' + referer, 
                   'visit from group',
                   'visitor', 
                   'visitor from ' + referer,
                   'visitor from group',
                   "member",
                   'member from ' + referer,
                   "member from group",
                   "inscription", 
                   "inscription from " + referer,
                   "inscription from group"
                   ))

    time = datetime.strptime(date, '%Y-%m-%dT%H:%M:%S')
    diff = (datetime.now() - time).days
    for i in range(0, diff, groupby):
         # Nb visits
        cursor.execute('''  
                SELECT datetime( ?, '+{0} days'), count(distinct(visit.id)) FROM visit 
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND real=1;
                '''.format(i, i + groupby),  (date, date, date))
        subresult = cursor.fetchone()

        # Nb visit from ref
        cursor.execute('''  
                SELECT count(distinct(visit.id)) FROM externalurl JOIN visit on externalurl.id=id_externalurl
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND netloc = ?  AND real=1;
                '''.format(i, i + groupby),  (date, date, referer))
        subresult += cursor.fetchone()

        # Nb visit in the first group
        cursor.execute('''  
                SELECT count(distinct(visit.id)) FROM visit 
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND real=1
                AND id_visitor in ({2})
                '''.format(i, i + groupby, ids),  (date, date))
        subresult += cursor.fetchone()

        # Nb visitor 
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM visit JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND real=1;
                '''.format(i, i + groupby),  (date, date))
        subresult += cursor.fetchone()

        # Nb visitor from ref
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND netloc = ?  AND real=1;
                '''.format(i, i + groupby),  (date, date, referer))
        subresult += cursor.fetchone()        
        
        # Nb visitor from group
        cursor.execute('''  
                SELECT count(distinct(id_visitor)) FROM visit
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND real=1 
                AND id_visitor IN ({2})
                '''.format(i, i + groupby, ids),  (date, date))
        subresult += cursor.fetchone()

        # Nb member 
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM visit JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND real=1 AND userid != -1
                '''.format(i, i + groupby),  (date, date))
        subresult += cursor.fetchone()

        #nb member from ref
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND netloc = ?  AND real=1 AND userid != -1
                '''.format(i, i + groupby),  (date, date, referer))
        subresult += cursor.fetchone()

        #nb member from group
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl JOIN visitor on visitor.id=id_visitor
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND real=1 AND userid != -1
                AND id_visitor IN ({2})
                '''.format(i, i + groupby, ids),  (date, date))
        subresult += cursor.fetchone()

        # nb inscription
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM visit
                JOIN visitor on visitor.id=id_visitor JOIN request on visit.id=id_visit
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND request.url like '%doactivate%'
                AND real=1
                '''.format(i, i + groupby),  (date, date))
        subresult += cursor.fetchone()

        # nb inscription from ref
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl
                JOIN visitor on visitor.id=id_visitor
                JOIN request on visit.id=id_visit
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND request.url like '%doactivate%'
                AND netloc = ?  AND real=1
                '''.format(i, i + groupby),  (date, date, referer))
        subresult += cursor.fetchone()

        # nb inscription from group
        cursor.execute('''  
                SELECT count(distinct(visitor.id)) FROM externalurl
                JOIN visit on externalurl.id=id_externalurl
                JOIN visitor on visitor.id=id_visitor
                JOIN request on visit.id=id_visit
                WHERE  begin_date > datetime(? , '+{0} days') AND begin_date < datetime(?, '+{1} days') 
                AND request.url like '%doactivate%'
                AND real=1
                AND id_visitor IN ({2})
                '''.format(i, i + groupby, ids),  (date, date))
        subresult += cursor.fetchone()

        result.append(subresult)

    for row in result:
        for cell in row:
            sys.stdout.write(str(cell) + ", ")
        print

    conn.commit()
    cursor.close()
        

if __name__ == "__main__":
    main()

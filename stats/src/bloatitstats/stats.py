#!python
import getopt, sys
from bloatitstats.commun.database import database
from queries.queries import queries

def print_stats(datafile, output):
    base = database(datafile)
    base.create_table()
    q = queries(base.cursor, output)
    print "generate graph"
    q.generate_graph_all()
    q.generate_graph_monthly()
    q.generate_graph_daily()
    print "generate main chart"
    q.generate_main_chart_daily()
    q.generate_main_chart_monthly()
    q.generate_main_chart_all()
    print "generate nb request by ua"
    q.nb_request_by_ua_all()
    q.nb_request_by_ua_daily()
    q.nb_request_by_ua_monthly()
    print "generate nb request by netloc"
    q.nb_request_by_netloc_all()
    q.nb_request_by_netloc_daily()
    q.nb_request_by_netloc_monthly()
    print "generate nb visit by visit size"
    q.nb_visit_by_visit_size_all()
    q.nb_visit_by_visit_size_daily()
    q.nb_visit_by_visit_size_monthly()

version='0.1'

def usage():
    print '''
    Print some stats using a database
    
-h --help              Show this help.
-v --version           Print the version number and exit.
-d --database FILE     Use 'FILE' as a database (default is ./stats.db)
-o --output FOLDER     Output the js files in the 'FOLDER'

'''

def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hvd:", ["help", "version", 'database'])
    except getopt.GetoptError, err:
        # print help information and exit:
        print str(err) # will print something like "option -a not recognized"
        usage()
        sys.exit(2)
        
    database = './stats.db'
    output = './js'
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
        else:
            print 'ERROR: unknown option'
            usage()
            sys.exit()
            
    print_stats(database, output)
            

if __name__ == "__main__":
    main()

#!python
import getopt, sys
from bloatitstats.commun.database import database
from queries.ua_queries import ua_queries
from queries.referer_queries import referer_queries
from queries.main_chart_queries import main_chart_queries
from queries.visits_queries import visits_queries
from queries.graph_queries import graph_queries
from queries.dashboard_queries import dashboard_queries

def print_stats(datafile, output):
    base = database(datafile)
    base.create_table()

    print "generate dashboard"
    q = dashboard_queries(base.cursor, output)
    q.generate_dashboard()

    print "generate graph"
    q = graph_queries(base.cursor, output)
    q.generate_graph_daily()
    q.generate_graph_monthly()

    print "generate main chart"
    q = main_chart_queries(base.cursor, output)
    q.generate_main_chart_daily()
    q.generate_main_chart_monthly()

    print "generate nb request by ua"
    q = ua_queries(base.cursor, output)
    q.nb_request_by_ua_daily()
    q.nb_request_by_ua_monthly()

    print "generate nb request by netloc"
    q = referer_queries(base.cursor, output)
    q.nb_request_by_netloc_daily()
    q.nb_request_by_netloc_monthly()

    print "generate nb visit by visit size"
    q = visits_queries(base.cursor, output)
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

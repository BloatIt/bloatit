#!python
import getopt, sys
from bloatitstats.commun.database import database
from queries.queries import queries

def print_stats(datafile):
    base = database(datafile)
    base.create_table()
    q = queries(base.cursor)
    
    print 'Query on USER AGENT'
    print '\nNb requests'
    q.nb_requests()
    print '\nNb visits'
    q.nb_request_by_os()
    
    print '\n\nQuery on USER AGENT'
    print '\nNb request by os family'
    q.nb_request_by_os()
    print '\nNb request by os'
    q.nb_request_by_os_name()
    print '\nNb request by user agent name'
    q.nb_request_by_ua_name()
    print '\nNb request by user agent family'
    q.nb_request_by_ua()
    print '\nshow the unknown user agents'
    q.all_unknown_ua()
    print '\nshow the unknown operating systems'
    q.all_unknown_os()
    
    print '\n\nQuery on USER AGENT'
    print '\nNb request by referer netloc'
    q.nb_request_by_netloc()
    
    print '\n\nQuery on Visits'
    print '\nNb visits by visit size (size is the nb of requests in a visit)'
    q.nb_visit_by_visit_size()

version='0.1'

def usage():
    print '''
    Print some stats using a database
    
-h --help              Show this help.
-v --version           Print the version number and exit.
-d --database FILE     Use 'FILE' as a database (default is ./stats.db)

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
    for o, a in opts:
        if o in ('-v', '--version'):
            print 'version: ' + version
            sys.exit()
        elif o in ("-h", "--help"):
            usage()
            sys.exit()
        elif o in ("-d", "--database"):
            database = a
        else:
            print 'ERROR: unknown option'
            usage()
            sys.exit()
            
    print_stats(database)
            

if __name__ == "__main__":
    main()
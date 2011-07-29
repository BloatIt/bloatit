from entry_processor import entry_processor
from bloatitstats.commun.database import database
import sys

def parse_value(parameter):
    (trash, value) = parameter.split("=", 1)
    return value.strip("'")
    
class request:
    def __init__(self, line):
        (trash, line_end) = line.split("REQUEST_URI='", 1)
        (self.request_uri, line_end) = line_end.split("'; REQUEST_METHOD='", 1)
        (self.request_method, line_end) = line_end.split("'; USER_AGENT='", 1)
        (self.user_agent, line_end) = line_end.split("'; ACCEPT_LANGUAGES='", 1)
        (self.accept_languages, line_end) = line_end.split("'; HTTP_REFERER='", 1)
        (self.http_referer, line_end) = line_end.split("'; REMOTE_ADDR='", 1)
        (self.remote_addr, line_end) = line_end.split("'; SERVER_PROTOCOL='", 1)
        (self.server_protocol, line_end) = line_end.split("'; SERVER_ADDR='", 1)
        self.server_addr = line_end.strip("' \n")

class context:
    def __init__(self, line):
        (trash, line_end) = line.split("USER_ID='", 1)
        (self.user_id, line_end) = line_end.split("'; KEY='", 1)
        (self.key, line_end) = line_end.split("'; LANG='", 1)
        self.lang = line_end.strip("' \n")


class logparser:
    def __init__(self, logfile):
        if logfile:
            self.file = open(logfile, 'r')
        else:
            self.file = sys.stdin
        
    def readline(self):
        line = self.file.readline()
        if not line:
            return False
        
        values = line.replace('\t', ' ').replace('  ', ' ').strip(' ').split(' ')
        if len(values) < 4:
            print 'skipping malformed line : ' + line
            return self.readline()
        
        self.date = values[0]
        self.thread = values[1].strip('[]Thread-')
        self.level = values[2]
        if values[3] == 'Access:Request:':
            self.is_request = True
            self.request = request(line)
        elif values[3] == 'Access:Context:':
            self.is_request = False
            self.context = context(line)
        else:
            print 'skipping malformed line : ' + line
            return self.readline()
        
        return True
    
    
    

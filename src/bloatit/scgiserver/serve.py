#!/usr/bin/python3
# -*- coding: UTF-8 -*-

from bloatit.server.dispatchserver import DispatchServer
from urllib.parse import parse_qs
from scgi.scgi_server import SCGIHandler
from scgi.scgi_server import SCGIServer
import logging
import traceback
import sys

LOG_FILENAME = '/tmp/bloatit_scgi.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG, format="%(asctime)s - %(name)s - %(levelname)s - %(message)s")

class HtmlHandler(SCGIHandler):
	
    
    def produce(self, env, bodysize, input, output):
        logging.info("HtmlHandler : produce page begin")
        
        try:
            query = parse_qs(env["QUERY_STRING"])
            preferred_langs = env["HTTP_ACCEPT_LANGUAGE"].split(",")
            page = DispatchServer(query, preferred_langs)
            output.write("Content-Type: text/html\r\n\r\n")
            html = page.process()
            output.write(html)
            
        except Exception as e:
            exc_type, exc_value, exc_traceback = sys.exc_info()

            output.write("Content-Type: text/text\r\n\r\n")
            #TODO:disable in prod
            logging.error("Fail to generate page : "+str(e))
            logging.error('Error Type: ' + str(exc_type))
            logging.error(repr(traceback.format_tb(exc_traceback)).replace("\\n", "\n"))
            logging.error('Error Value: ' + str(exc_value))


            output.write("Fail to generate page : "+str(e)+"\n")
            output.write('Error Type: ' + str(exc_type)+"\n")
            output.write(repr(traceback.format_tb(exc_traceback)).replace("\\n", "\n")+"\n")
            output.write('Error Value: ' + str(exc_value)+"\n")

            print('Error Type: ' + str(exc_type))
            traceback.print_tb(exc_traceback)
            print('Error Value: ' + str(exc_value))

        logging.info("HtmlHandler : produce page end")

try:
    logging.info("Start scgi server")
    SCGIServer(handler_class=HtmlHandler).serve()
except Exception as e:

    logging.critical("Crash : "+str(e))


logging.info("Stop scgi server")

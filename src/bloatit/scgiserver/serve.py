#!/usr/bin/python3
# -*- coding: utf-8 -*-

# Copyright (C) 2010 BloatIt.
#
# This file is part of BloatIt.
#
# BloatIt is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# BloatIt is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with BloatIt. If not, see <http://www.gnu.org/licenses/>.

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
            post = parse_qs(input.read(bodysize))

            preferred_langs = env["HTTP_ACCEPT_LANGUAGE"].split(",")

            cookies = {}
            if "HTTP_COOKIE" in env:
                cookie_string = env["HTTP_COOKIE"].split(";")
                for cookie in cookie_string:
                    parts = cookie.split("=")
                    if len(parts) == 2 :
                        cookies[parts[0].strip()] = parts[1].strip()
                        
            page = DispatchServer(query, post, cookies, preferred_langs)
            result = page.process()
            
            output.write(result)
            
        except Exception as e:
            exc_type, exc_value, exc_traceback = sys.exc_info()

            output.write("Content-Type: text/html\r\n\r\n")
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

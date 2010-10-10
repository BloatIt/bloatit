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
import unittest

class TestUrl(unittest.TestCase):
    def test_url_parsing(self):
        # Empty page & no parameter
        self.assertEqual(DispatchServer._parse_query_string(''), ('',{}))
        self.assertEqual(DispatchServer._parse_query_string('/'), ('',{}))
        self.assertEqual(DispatchServer._parse_query_string('////////'), ('',{}))

        # Non empty page & no parameter
        self.assertEqual(DispatchServer._parse_query_string('index'), ('index',{}))
        self.assertEqual(DispatchServer._parse_query_string('/index'), ('index',{}))
        self.assertEqual(DispatchServer._parse_query_string('index/'), ('index',{}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop'), ('index/plop',{}))
        self.assertEqual(DispatchServer._parse_query_string('index/////plop'), ('index/plop',{}))

        # empty page with parameter
        self.assertEqual(DispatchServer._parse_query_string('plop-plop'), ('',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('/plop-plop'), ('',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('////////plop-plop'), ('',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('plop-plop/test'), ('',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('plop-plop/test/pataplop-pataplop'), ('',{'plop':'plop', 'pataplop':'pataplop'}))

        # non empty page and non empty parameters
        self.assertEqual(DispatchServer._parse_query_string('index/plop-plop'), ('index',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('/index/plop-plop'), ('index',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop-plop/hello-hello'), ('index',{'plop':'plop', 'hello': 'hello'}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop-plop/hello-hello-hihi'), ('index',{'plop':'plop', 'hello': 'hello'}))
        self.assertEqual(DispatchServer._parse_query_string('/index/plop-plop/hello-hello'), ('index',{'plop':'plop', 'hello': 'hello'}))
        self.assertEqual(DispatchServer._parse_query_string('/index/plop-plop/hello-hello-hihi'), ('index',{'plop':'plop', 'hello': 'hello'}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop-plop/hello'), ('index',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop-plop/hello/pataplop-pataplop'), ('index',{'plop':'plop', 'pataplop': 'pataplop'}))
        self.assertEqual(DispatchServer._parse_query_string('///index//hello//////plop-plop/////hello////pataplop-pataplop'), ('index/hello',{'plop':'plop', 'pataplop': 'pataplop'}))

        # special characters
        self.assertEqual(DispatchServer._parse_query_string('index#{[#/plop-plop'), ('index#{[#',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop@\#é-plop@\#é'), ('index',{'plop@\#é':'plop@\#é'}))
        self.assertEqual(DispatchServer._parse_query_string('/index#{[#/plop-plop'), ('index#{[#',{'plop':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('/index/plop@\#é-plop@\#é'), ('index',{'plop@\#é':'plop@\#é'}))

        # few strange cases
        self.assertEqual(DispatchServer._parse_query_string('index/-plop'), ('index',{'':'plop'}))
        self.assertEqual(DispatchServer._parse_query_string('index/plop-'), ('index',{'plop':''}))
        self.assertEqual(DispatchServer._parse_query_string('index/-'), ('index',{'':''}))

if __name__ == '__main__':
    unittest.main()
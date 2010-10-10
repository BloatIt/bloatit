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


from bloatit.web.htmlrenderer.htmltools import HtmlTools
import unittest

class TestHttpTools(unittest.TestCase):
    def test_compress_karma(self):
        self.assert_(HtmlTools.compress_karma(1) == "1")
        self.assert_(HtmlTools.compress_karma(20) == "20")
        self.assertEqual(HtmlTools.compress_karma(1000), "1k")
        self.assertEqual(HtmlTools.compress_karma(1500), "1.5k")
        self.assertEqual(HtmlTools.compress_karma(160000), "160k")
        self.assertEqual(HtmlTools.compress_karma(2600000), "2.6M")
        self.assertEqual(HtmlTools.compress_karma(27000000), "27M")
        self.assertEqual(HtmlTools.compress_karma(5000000000), "5T")
        self.assertEqual(HtmlTools.compress_karma(67000000000), "67T")
        self.assertEqual(HtmlTools.compress_karma(671000000000), "671T")
        self.assertEqual(HtmlTools.compress_karma(6710000000000), "∞")
        
if __name__ == '__main__':
    unittest.main()
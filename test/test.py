from htmltest.testtest import TestTest
from htmltest.testhttptools import TestHttpTools
from htmltest.testurl import TestUrl
import unittest


suite = unittest.TestSuite()
suite.addTest(unittest.TestLoader().loadTestsFromTestCase(TestTest))
suite.addTest(unittest.TestLoader().loadTestsFromTestCase(TestHttpTools))
suite.addTest(unittest.TestLoader().loadTestsFromTestCase(TestUrl))
unittest.TextTestRunner(verbosity=2).run(suite)


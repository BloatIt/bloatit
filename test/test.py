from htmltest.testtest import TestTest
from htmltest.testhttptools import TestHttpTools
import unittest


suite = unittest.TestSuite()
suite.addTest(unittest.TestLoader().loadTestsFromTestCase(TestTest))
suite.addTest(unittest.TestLoader().loadTestsFromTestCase(TestHttpTools))
unittest.TextTestRunner(verbosity=2).run(suite)


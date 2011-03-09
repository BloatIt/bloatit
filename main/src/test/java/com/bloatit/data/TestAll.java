package com.bloatit.data;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all of
 * the tests within its package as well as within any subpackages of its
 * package.
 *
 * @generatedBy CodePro at 27/01/11 17:27
 * @author tom
 * @version $Revision: 1.0 $
 */
public class TestAll {

    /**
     * Create a test suite that can run all of the test cases in this package
     * and all subpackages.
     *
     * @return the test suite that was created
     * @generatedBy CodePro at 27/01/11 17:27
     */
    public static Test suite() {
        TestSuite suite;

        suite = new TestSuite("Tests in package com.bloatit.data");
        suite.addTestSuite(DaoAccountTest.class);
        suite.addTestSuite(DaoTranslationTest.class);
        suite.addTestSuite(DaoGroupMemberTest.class);
        suite.addTestSuite(DaoKudosableTest.class);
        suite.addTestSuite(DaoDemandTest.class);
        suite.addTestSuite(DaoUserContentTest.class);
        suite.addTestSuite(DaoMemberCreationTest.class);
        suite.addTestSuite(DaoTransactionTest.class);
        suite.addTestSuite(DaoCommentTest.class);
        suite.addTestSuite(DaoDescriptionTest.class);
        suite.addTestSuite(DaoGroupCreationTest.class);
        suite.addTest(com.bloatit.data.queries.TestAll.suite());
        return suite;
    }
}

package com.bloatit.model.data;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        final TestSuite suite = new TestSuite(AllTests.class.getName());
        // $JUnit-BEGIN$
        suite.addTest(GroupMemberTest.suite());
        suite.addTestSuite(DemandTest.class);
        suite.addTestSuite(DescriptionTranslatableTest.class);
        suite.addTestSuite(CommentTest.class);
        suite.addTestSuite(AccountTest.class);
        suite.addTestSuite(TransactionTest.class);
        suite.addTestSuite(KudosableTest.class);
        suite.addTestSuite(UserContentTest.class);
        // $JUnit-END$
        return suite;
    }

}

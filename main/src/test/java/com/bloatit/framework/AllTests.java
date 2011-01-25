package com.bloatit.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite(AllTests.class.getName());
        //$JUnit-BEGIN$
        suite.addTestSuite(MemberTest.class);
        suite.addTestSuite(KudosableTest.class);
        //$JUnit-END$
        return suite;
    }

}

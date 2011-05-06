/*
 * 
 */
package com.bloatit.model;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all of
 * the tests within its package as well as within any subpackages of its
 * package.
 * 
 * @generatedBy CodePro at 27/01/11 17:30
 * @author tom
 * @version $Revision: 1.0 $
 */

@RunWith(Suite.class)
@SuiteClasses(value = { KudosableTest.class,
                       MemberTest.class,
                       ActorTest.class,
                       AccountTest.class,
                       BankTransactionTest.class,
                       FeatureImplementationTest.class, })
public class TestAll {
    // nothing
    @Before
    public void before() {
        System.out.println("--------------------------");
        System.out.println("Begin tests of Model layer");
    }

    @After
    public void after() {
        System.out.println("End tests of Model layer");
        System.out.println("--------------------------");
    }
}

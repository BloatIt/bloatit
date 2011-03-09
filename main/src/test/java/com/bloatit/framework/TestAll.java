package com.bloatit.framework;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.bloatit.framework.webserver.mime.MultipartMimeParserTest;
import com.bloatit.framework.webserver.mime.decoders.MimeBase64DecoderTest;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all of
 * the tests within its package as well as within any subpackages of its
 * package.
 */
public class TestAll {

    /**
     * Create a test suite that can run all of the test cases in this package
     * and all subpackages.
     */
    public static Test suite() {
        TestSuite suite;

        suite = new TestSuite("Tests in package com.bloatit.framework");
        suite.addTestSuite(MimeBase64DecoderTest.class);
        suite.addTestSuite(MultipartMimeParserTest.class);
        return suite;
    }

}

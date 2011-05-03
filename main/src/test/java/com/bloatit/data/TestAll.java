//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.data;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The class <code>TestAll</code> builds a suite that can be used to run all of
 * the tests within its package as well as within any subpackages of its
 * package.
 * 
 * @generatedBy CodePro at 27/01/11 17:27
 * @author tom
 * @version $Revision: 1.0 $
 */
@RunWith(Suite.class)
@SuiteClasses(value = { DaoMemberCreationTest.class,
                       DaoGroupCreationTest.class,
                       DaoGroupMemberTest.class,
                       DaoAccountTest.class,
                       DaoTranslationTest.class,
                       DaoFeatureTest.class,
                       DaoUserContentTest.class,
                       DaoTransactionTest.class,
                       DaoCommentTest.class,
                       DaoDescriptionTest.class,
                       com.bloatit.data.queries.TestAll.class,
// DaoKudosableTest.class,
              })
public class TestAll {
    // nothing
}

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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.bloatit.framework.exceptions.lowlevel.NonOptionalParameterException;
import com.bloatit.framework.utils.i18n.Language;

public class DaoTranslationTest extends DataTestUnit {

    @Test
    public void testDaoTranslationDaoMemberDaoDescriptionLocaleStringString() {
        final DaoDescription description = DaoDescription.createAndPersist(yo, null, Language.EN, "English title", "English body");

        new DaoTranslation(fred, null, description, Language.FR, "plop", "plip");
        try {
            new DaoTranslation(fred, null, description, Language.FR, "plop", "");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, Language.FR, "", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, Language.FR, "plop", null);
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, Language.FR, null, "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, description, null, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(fred, null, null, Language.FR, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
        try {
            new DaoTranslation(null, null, description, Language.FR, "plop", "plip");
            fail();
        } catch (final NonOptionalParameterException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testUniqunessOfTranslation() {
        final DaoDescription description = DaoDescription.createAndPersist(yo, null, Language.EN, "English title", "English body");

        description.addTranslation(fred, null, Language.FR, "plop", "plip");
        try {
            description.addTranslation(yo, null, Language.FR, "plup", "plyp");

            SessionManager.endWorkUnitAndFlush();
            fail();
        } catch (final Exception e) {
            assertTrue(true);
            SessionManager.beginWorkUnit();
        }
    }
}
